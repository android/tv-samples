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

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.view.View
import androidx.fragment.app.viewModels
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.media3.common.ForwardingPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Player.EVENT_IS_PLAYING_CHANGED
import androidx.media3.common.Player.EVENT_PLAYER_ERROR
import androidx.media3.common.Player.EVENT_POSITION_DISCONTINUITY
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.ui.leanback.LeanbackPlayerAdapter
import androidx.navigation.fragment.findNavController
import com.android.tv.reference.MainActivity
import com.android.tv.reference.shared.datamodel.Video
import com.google.android.gms.cast.tv.CastReceiverContext
import java.time.Duration
import timber.log.Timber

/** Fragment that plays video content with ExoPlayer. */
class PlaybackFragment : VideoSupportFragment() {

    private lateinit var video: Video

    private val viewModel: PlaybackViewModel by viewModels()

    private val uiPlaybackStateListener = object : PlaybackStateListener {
        override fun onChanged(state: VideoPlaybackState) {
            // While a video is playing, the screen should stay on and the device should not go to
            // sleep. When in any other state such as if the user pauses the video, the app should
            // not prevent the device from going to sleep.
            view?.keepScreenOn = state is VideoPlaybackState.Play

            when (state) {
                is VideoPlaybackState.Prepare -> startPlaybackFromWatchProgress(state.startPosition)
                is VideoPlaybackState.End -> {
                    // To get to playback, the user always goes through browse first. Deep links for
                    // directly playing a video also go to browse before playback. If playback
                    // finishes the entire video, the PlaybackFragment is popped off the back stack
                    // and the user returns to browse.
                    findNavController().popBackStack()
                }
                is VideoPlaybackState.Error ->
                    findNavController().navigate(
                        PlaybackFragmentDirections
                            .actionPlaybackFragmentToPlaybackErrorFragment(
                                state.video,
                                state.exception
                            )
                    )
                else -> {
                    // Do nothing.
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the video data.
        video = PlaybackFragmentArgs.fromBundle(requireArguments()).video

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.addPlaybackStateListener(uiPlaybackStateListener)
    }

    override fun onStart() {
        super.onStart()
        createMediaSession()
    }

    override fun onStop() {
        super.onStop()
        releaseMediaSession()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removePlaybackStateListener(uiPlaybackStateListener)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun createMediaSession() {
        if (mediaSession == null) {
            val exoplayer = ExoPlayer.Builder(requireContext()).build().apply { addListener(PlayerEventListener()) }
            val forwardingPlayer = object : ForwardingPlayer(exoplayer) {
                override fun stop() {
                    // Treat stop commands as pause, this keeps ExoPlayer, MediaSession, etc.
                    // in memory to allow for quickly resuming. This also maintains the playback
                    // position so that the user will resume from the current position when backing
                    // out and returning to this video
                    Timber.v("Playback stopped at $currentPosition")
                    // This both prevents playback from starting automatically and pauses it if
                    // it's already playing
                    playWhenReady = false
                }
            }
            val activityPendingIntent = PendingIntent.getActivity(
                requireContext(),
                0,
                Intent(requireContext(), MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            mediaSession =
                MediaSession.Builder(/* context= */ requireContext(), forwardingPlayer)
                    .setId(MEDIA_SESSION_TAG)
                    .setSessionActivity(activityPendingIntent)
                    .build()
            CastReceiverContext.getInstance().mediaManager.setSessionCompatToken(
                mediaSession!!.sessionCompatToken as MediaSessionCompat.Token)

            val mediaMetadata =
                MediaMetadata.Builder()
                    .setArtworkUri(Uri.parse(video.thumbnailUri))
                    .setTitle(video.name)
                    .setDescription(video.description)
                    .build()
            val mediaItem =
                MediaItem.Builder()
                    .setUri(video.videoUri)
                    .setMediaId(video.id)
                    .setMediaMetadata(mediaMetadata)
                    .build()
            exoplayer.setMediaItem(mediaItem)
            exoplayer.prepare()
            prepareGlue(exoplayer)
            viewModel.onStateChange(VideoPlaybackState.Load(video))
        }
    }

    private fun releaseMediaSession() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
            // TODO Remove comment after b/218604167 has been release in the Cast TV library.
            // CastReceiverContext.getInstance().mediaManager.setSessionCompatToken(null)
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun prepareGlue(localPlayer: Player) {
        ProgressTransportControlGlue(
            requireContext(),
            LeanbackPlayerAdapter(
                requireContext(),
                localPlayer,
                PLAYER_UPDATE_INTERVAL_MILLIS.toInt()
            ),
            onProgressUpdate
        ).apply {
            host = VideoSupportFragmentGlueHost(this@PlaybackFragment)
            title = video.name
            // Enable seek manually since PlaybackTransportControlGlue.getSeekProvider() is null,
            // so that PlayerAdapter.seekTo(long) will be called during user seeking.
            // TODO(gargsahil@): Add a PlaybackSeekDataProvider to support video scrubbing.
            isSeekEnabled = true
        }
    }

    private fun startPlaybackFromWatchProgress(startPosition: Long) {
        Timber.v("Starting playback from $startPosition")
        mediaSession?.apply {
            player.seekTo(startPosition)
            player.playWhenReady = true
        }
    }

    private val onProgressUpdate: () -> Unit = {
        // TODO(benbaxter): Calculate when end credits are displaying and show the next episode for
        //  episodic content.
    }

    @SuppressLint("UnsafeOptInUsageError")
    inner class PlayerEventListener : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            if (events.contains(EVENT_IS_PLAYING_CHANGED)
                || events.contains(EVENT_POSITION_DISCONTINUITY)) {
                when {
                    player.isPlaying -> viewModel.onStateChange(
                        VideoPlaybackState.Play(video))
                    player.playbackState == Player.STATE_ENDED -> viewModel.onStateChange(
                        VideoPlaybackState.End(video))
                    else -> viewModel.onStateChange(
                        VideoPlaybackState.Pause(video, player.currentPosition))
                }
            }
            if (events.contains(EVENT_PLAYER_ERROR)) {
                Timber.w(player.playerError, "Playback error")
                viewModel.onStateChange(VideoPlaybackState.Error(video, player.playerError!!))
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

        private var mediaSession: MediaSession? = null
    }
}
