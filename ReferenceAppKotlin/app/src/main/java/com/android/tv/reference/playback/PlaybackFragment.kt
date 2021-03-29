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
import android.support.v4.media.session.MediaSessionCompat
import androidx.fragment.app.viewModels
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.PlaybackGlue
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.tv.reference.R
import com.android.tv.reference.castconnect.CastHelper
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.playback.VideoPlaybackState
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
import java.time.Duration

/** Fragment that plays video content with ExoPlayer. */
class PlaybackFragment : VideoSupportFragment() {

    private lateinit var video: Video

    private var exoplayer: ExoPlayer? = null
    private val viewModel: PlaybackViewModel by viewModels()
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    private val uiPlaybackStateListener = Observer<VideoPlaybackState> { state ->
        Timber.v("State: %s", state)

        // While a video is playing, the screen should stay on and the device should not go to
        // sleep. When in any other state such as if the user pauses the video, the app should not
        // prevent the device from going to sleep.
        view?.keepScreenOn = state is VideoPlaybackState.Play

        when (state) {
            is VideoPlaybackState.Prepare -> startPlaybackFromWatchProgress(state.startPosition)
            is VideoPlaybackState.End ->
                // To get to playback, the user always goes through browse first. Deep links for
                // directly playing a video also go to browse before playback. If playback finishes
                // the entire video, the PlaybackFragment is popped off the back stack and the user
                // returns to browse.
                findNavController().popBackStack()
            is VideoPlaybackState.Error ->
                findNavController().navigate(
                    PlaybackFragmentDirections
                        .actionPlaybackFragmentToPlaybackErrorFragment(state.video, state.exception)
                )
            else -> {
                // Do nothing.
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the video data.
        video = PlaybackFragmentArgs.fromBundle(requireArguments()).video

        // Create the MediaSession that will be used throughout the lifecycle of this Fragment.
        createMediaSession()

        // Load the ViewModel for this specific video.
        viewModel.registerStateListeners(owner = this)
        viewModel.playbackState.observe(/* owner= */ this, uiPlaybackStateListener)
        viewModel.onStateChange(VideoPlaybackState.Load(video))
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onPause() {
        super.onPause()
        // TODO(mayurikhin@):  Find valid data points to debate both cases when to notify Watch Next
        // (Either Player callback states or lifecycle Pause events)
        if (exoplayer == null) {
            Timber.w("Warning : ExoPlayer is null. Cannot update in onPause()")
            return
        }
        Timber.v("Playback Paused. Add last known position ${exoplayer!!.currentPosition}")
        viewModel.onStateChange(VideoPlaybackState.Pause(video, exoplayer!!.currentPosition))
    }

    override fun onStop() {
        super.onStop()
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
        ProgressTransportControlGlue(
            requireContext(),
            LeanbackPlayerAdapter(
                requireContext(),
                localExoplayer,
                PLAYER_UPDATE_INTERVAL_MILLIS.toInt()
            ),
            onProgressUpdate
        ).apply {
            host = VideoSupportFragmentGlueHost(this@PlaybackFragment)
            title = video.name
            // Using the glue's callback allows the fragment to be player agnostic as the callback
            // abstracts a lot of details from the the player into a simple API. Note that similar
            // methods are available in exoplayer's EventListener and should not be used otherwise
            // the fragment receives duplicate events.
            addPlayerCallback(PlaybackGlueCallback())
            // Enable seek manually since PlaybackTransportControlGlue.getSeekProvider() is null,
            // so that PlayerAdapter.seekTo(long) will be called during user seeking.
            // TODO(gargsahil@): Add a PlaybackSeekDataProvider to support video scrubbing.
            isSeekEnabled = true
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

    private val onProgressUpdate: () -> Unit = {
        // TODO(benbaxter): Calculate when end credits are displaying and show the next episode for
        //  episodic content.
    }

    inner class PlayerEventListener : Player.EventListener {
        override fun onPlayerError(error: ExoPlaybackException) {
            Timber.w(error, "Playback error")
            viewModel.onStateChange(VideoPlaybackState.Error(video, error))
        }
    }

    inner class PlaybackGlueCallback : PlaybackGlue.PlayerCallback() {

        override fun onPlayCompleted(glue: PlaybackGlue) {
            super.onPlayCompleted(glue)
            Timber.v("Finished playing content")
            viewModel.onStateChange(VideoPlaybackState.End(video))
        }

        override fun onPlayStateChanged(glue: PlaybackGlue) {
            super.onPlayStateChanged(glue)
            Timber.v("Is playing: %b", glue.isPlaying)
            if (glue.isPlaying) {
                viewModel.onStateChange(VideoPlaybackState.Play(video))
            } else {
                // In onStop(), we remove the fragment's reference to the player yet during the
                // player's cleanup/release, the play state changed callback is called. So we need
                // to guard with a null-check. The pause state is already triggered from onPause(),
                // in the fragment's lifecycle, while we have a reference to the player, so the
                // state machine does not need this particular event to be triggered during
                // onStop().
                exoplayer?.let {
                    viewModel.onStateChange(
                        VideoPlaybackState.Pause(video, it.currentPosition)
                    )
                }
            }
        }
    }

    companion object {
        // Update the player UI fairly often. The frequency of updates affects several UI components
        // such as the smoothness of the progress bar and time stamp labels updating. This value can
        // be tweaked for better performance.
        private val PLAYER_UPDATE_INTERVAL_MILLIS = Duration.ofMillis(50).toMillis()

        // A short name to identify the media session when debugging.
        private const val MEDIA_SESSION_TAG = "ReferenceAppKotlin"
    }
}
