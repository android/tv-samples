/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.tv.reference.playback

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.session.MediaSessionCompat
import androidx.fragment.app.viewModels
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.android.tv.reference.R
import com.android.tv.reference.castconnect.CastHelper
import com.android.tv.reference.playnext.PlayNextHelper
import com.android.tv.reference.playnext.PlayNextWorker
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.playback.VideoPlaybackState
import com.android.tv.reference.shared.watchprogress.WatchProgress
import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.cast.tv.CastReceiverContext
import timber.log.Timber

/**
 * Fragment that plays video content with ExoPlayer
 */
class PlaybackFragment : VideoSupportFragment() {

    private lateinit var video: Video
    private lateinit var handler: Handler

    // Self-posting Runnable that posts an updated watch progress
    private val updateWatchProgressRunnable = Runnable {
        saveUpdatedWatchProgress()
        scheduleWatchProgressUpdate()
    }

    private var exoplayer: ExoPlayer? = null
    private val viewModel: PlaybackViewModel by viewModels()
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    private val uiPlaybackStateListener = Observer<VideoPlaybackState> { state ->
        Timber.v("State: %s", state)
        if (state is VideoPlaybackState.Prepare) {
            startPlaybackFromWatchProgress(state.startPosition)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the video data.
        video = PlaybackFragmentArgs.fromBundle(requireArguments()).video

        // Create the handler for posting watch progress updates.
        handler = Handler()

        // Create the MediaSession that will be used throughout the lifecycle of this Fragment.
        createMediaSession()

        // Load the ViewModel for this specific video.
        viewModel.registerStateListeners(owner = this)
        viewModel.playbackState.observe(/* owner= */ this, uiPlaybackStateListener)
        viewModel.onStateChange(VideoPlaybackState.Load(video))
    }

    private fun saveUpdatedWatchProgress() {
        if (exoplayer == null) {
            Timber.w("Warning : ExoPlayer is null. Cannot update watch progress")
            return
        }
        Timber.v("Saving updated WatchProgress position: ${exoplayer!!.contentPosition}")
        viewModel.update(WatchProgress(video.id, startPosition = exoplayer!!.currentPosition))
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onPause() {
        super.onPause()
        // TODO(mayurikhin@) :  Find valid data points to debate both cases when to notify Play Next
        // (Either Player callback states or lifecycle Pause events)
        if (exoplayer == null) {
            Timber.w("Warning : ExoPlayer is null. Cannot update in onPause()")
            return
        }
        notifyPlayNext(PlayNextHelper.PLAY_STATE_PAUSED)
        Timber.v("Playback Paused. Add last known position ${exoplayer!!.currentPosition}")
    }

    override fun onStop() {
        super.onStop()
        cancelWatchProgressUpdates()
        destroyPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Releasing the mediaSession due to inactive playback and setting token for cast to null.
        mediaSession.release()
        CastHelper.setMediaSessionTokenForCast(
            /* mediaSession =*/ null,
            CastReceiverContext.getInstance().mediaManager
        )
    }

    private fun initializePlayer() {
        val dataSourceFactory = DefaultDataSourceFactory(
            requireContext(),
            Util.getUserAgent(requireContext(), getString(R.string.app_name))
        )
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(video.videoUri))
        exoplayer = SimpleExoPlayer.Builder(requireContext()).build().apply {
            prepare(
                /* mediaSource= */ mediaSource,
                /* resetPosition= */ false,
                /* resetState= */ true
            )
            addListener(PlayerEventListener())
            prepareGlue(this)
            mediaSessionConnector.setPlayer(this)
            mediaSession.isActive = true
        }
    }

    private fun destroyPlayer() {
        mediaSession.isActive = false
        mediaSessionConnector.setPlayer(null)
        exoplayer?.release()
        exoplayer = null
    }

    private fun prepareGlue(localExoplayer: ExoPlayer) {
        PlaybackTransportControlGlue(
            requireContext(),
            LeanbackPlayerAdapter(requireContext(), localExoplayer, PLAYER_UPDATE_INTERVAL_MILLIS)
        ).apply {
            host = VideoSupportFragmentGlueHost(this@PlaybackFragment)
            title = video.name
        }
    }

    private fun createMediaSession() {
        mediaSession = MediaSessionCompat(requireContext(), MEDIA_SESSION_TAG)

        mediaSessionConnector = MediaSessionConnector(mediaSession).apply {
            setQueueNavigator(SingleVideoQueueNavigator(video, mediaSession))
            setControlDispatcher(object : DefaultControlDispatcher() {
                override fun dispatchStop(player: Player, reset: Boolean): Boolean {
                    // Treat stop commands as pause, this keeps ExoPlayer, MediaSession, etc.
                    // in memory to allow for quickly resuming. This also maintains the playback
                    // position so that the user will resume from the current position when backing
                    // out and returning to this video
                    Timber.v("Playback stopped at ${player.currentPosition}")
                    // This both prevents playback from starting automatically and pauses it if
                    // it's already playing
                    player.playWhenReady = false
                    return true
                }
            })
        }

        CastHelper.setMediaSessionTokenForCast(
            mediaSession,
            CastReceiverContext.getInstance().mediaManager
        )
    }

    private fun startPlaybackFromWatchProgress(startPosition: Long) {
        Timber.v("Starting playback from $startPosition")
        exoplayer?.apply {
            seekTo(startPosition)
            playWhenReady = true
        }
    }

    private fun scheduleWatchProgressUpdate() {
        Timber.v("Scheduling watch progress updates")
        handler.postDelayed(updateWatchProgressRunnable, WATCH_PROGRESS_SAVE_INTERVAL_MILLIS)
    }

    private fun cancelWatchProgressUpdates() {
        Timber.v("Canceling watch progress updates")
        handler.removeCallbacks(updateWatchProgressRunnable)

        // Store the last progress update
        saveUpdatedWatchProgress()
    }

    private fun hasContentFinishedPlaying(): Boolean {
        val playerState = exoplayer?.playbackState ?: 0
        return playerState == Player.STATE_ENDED
    }

    inner class PlayerEventListener : Player.EventListener {

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) {
                scheduleWatchProgressUpdate()
            } else {
                cancelWatchProgressUpdates()

                // To get to playback, the user always goes through browse first. Deep links
                // for directly playing a video also go to browse before playback. If
                // playback finishes the entire video, the PlaybackFragment is popped off
                // the back stack and the user returns to browse.
                if (hasContentFinishedPlaying()) {
                    Timber.v("Finished playing content")
                    findNavController().popBackStack()
                }
            }
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            super.onPlayerStateChanged(playWhenReady, playbackState)
            if (playbackState == Player.STATE_ENDED) {
                notifyPlayNext(PlayNextHelper.PLAY_STATE_ENDED)
            }
        }

        override fun onSeekProcessed() {
            saveUpdatedWatchProgress()
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            // TODO(b/158233485): Display an error dialog with retry/stop options
            Timber.w(error, "Playback error")
        }
    }

    /**
     * Notifies the video state and relevant metadata for adding/removing to Play Next Channel.
     * Used for adding un-finished/next content & removing finished content from Play Next.
     */
    private fun notifyPlayNext(state: String) {
        Timber.v("Notify to play next from playback. State is $state")

        // If the exoplayer is invalid, throw a warning that video cant be added to Play next.
        if (exoplayer == null) {
            Timber.w("Warning : ExoPlayer is null. Cannot add to Play Next")
            return
        }

        // Set relevant data about playback state and video.
        val watchData = Data.Builder().apply {
            putString(PlayNextHelper.VIDEO_ID, video.id)
            putLong(PlayNextHelper.CURRENT_POSITION, exoplayer!!.currentPosition)
            putLong(PlayNextHelper.DURATION, exoplayer!!.duration)
            putString(PlayNextHelper.PLAYER_STATE, state)
        }

        // Run on a background thread to process playback states and do relevant operations for
        // Play Next.
        WorkManager.getInstance(requireContext()).enqueue(
            OneTimeWorkRequest.Builder(PlayNextWorker::class.java)
                .setInputData(watchData.build())
                .build()
        )
    }

    companion object {
        // How often to update the player UI
        private const val PLAYER_UPDATE_INTERVAL_MILLIS = 50

        // How often to save watch progress to the database
        private const val WATCH_PROGRESS_SAVE_INTERVAL_MILLIS = 10 * 1000L

        // A short name to identify the media session when debugging.
        private const val MEDIA_SESSION_TAG = "ReferenceAppKotlin"
    }
}
