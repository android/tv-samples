/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tv.classics.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.RatingCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.leanback.app.PlaybackSupportFragment
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.PlaybackGlue
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.PlaybackControlsRow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import coil.Coil
import coil.api.get
import com.android.tv.classics.R
import com.android.tv.classics.models.TvMediaDatabase
import com.android.tv.classics.models.TvMediaMetadata
import com.android.tv.classics.utils.TvLauncherUtils
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min


/** A fragment representing the current metadata item being played */
class NowPlayingFragment : VideoSupportFragment() {

    /** AndroidX navigation arguments */
    private val args: NowPlayingFragmentArgs by navArgs()

    private lateinit var player: SimpleExoPlayer
    private lateinit var database: TvMediaDatabase

    /** Allows interaction with transport controls, volume keys, media buttons  */
    private lateinit var mediaSession: MediaSessionCompat

    /** Glue layer between the player and our UI */
    private lateinit var playerGlue: MediaPlayerGlue

    /**
     * Connects a [MediaSessionCompat] to a [Player] so transport controls are handled automatically
     */
    private lateinit var mediaSessionConnector: MediaSessionConnector

    /** Custom implementation of [PlaybackTransportControlGlue] */
    private inner class MediaPlayerGlue(context: Context, adapter: LeanbackPlayerAdapter) :
            PlaybackTransportControlGlue<LeanbackPlayerAdapter>(context, adapter) {

        private val actionRewind = PlaybackControlsRow.RewindAction(context)
        private val actionFastForward = PlaybackControlsRow.FastForwardAction(context)
        private val actionClosedCaptions = PlaybackControlsRow.ClosedCaptioningAction(context)

        fun skipForward(millis: Long = SKIP_PLAYBACK_MILLIS) =
                // Ensures we don't advance past the content duration (if set)
                player.seekTo(if (player.contentDuration > 0) {
                    min(player.contentDuration, player.currentPosition + millis)
                } else {
                    player.currentPosition + millis
                })

        fun skipBackward(millis: Long = SKIP_PLAYBACK_MILLIS) =
                // Ensures we don't go below zero position
                player.seekTo(max(0, player.currentPosition - millis))

        override fun onCreatePrimaryActions(adapter: ArrayObjectAdapter) {
            super.onCreatePrimaryActions(adapter)
            // Append rewind and fast forward actions to our player, keeping the play/pause actions
            // created by default by the glue
            adapter.add(actionRewind)
            adapter.add(actionFastForward)
            adapter.add(actionClosedCaptions)
        }

        override fun onActionClicked(action: Action) = when (action) {
            actionRewind -> skipBackward()
            actionFastForward -> skipForward()
            else -> super.onActionClicked(action)
        }

        /** Custom function used to update the metadata displayed for currently playing media */
        fun setMetadata(metadata: TvMediaMetadata) {
            // Displays basic metadata in the player
            title = metadata.title
            subtitle = metadata.author
            lifecycleScope.launch(Dispatchers.IO) {
                metadata.artUri?.let { art = Coil.get(it) }
            }

            // Prepares metadata playback
            val dataSourceFactory = DefaultDataSourceFactory(
                    requireContext(), getString(R.string.app_name))
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(metadata.contentUri)
            player.prepare(mediaSource, false, true)
        }
    }

    /** Updates last know playback position */
    private val updateMetadataTask: Runnable = object : Runnable {
        override fun run() {

            // Make sure that the view has not been destroyed
            view ?: return

            // The player duration is more reliable, since metadata.playbackDurationMillis has the
            //  "official" duration as per Google / IMDb which may not match the actual media
            val contentDuration = player.duration
            val contentPosition = player.currentPosition

            // Updates metadata state
            val metadata = args.metadata.apply {
                playbackPositionMillis = contentPosition
            }

            // Marks as complete if 95% or more of video is complete
            if (player.playbackState == SimpleExoPlayer.STATE_ENDED ||
                    (contentDuration > 0 && contentPosition > contentDuration * 0.95)) {
                val programUri = TvLauncherUtils.removeFromWatchNext(requireContext(), metadata)
                if (programUri != null) lifecycleScope.launch(Dispatchers.IO) {
                    database.metadata().update(metadata.apply { watchNext = false })
                }

                // If playback is not done, update the state in watch next row with latest time
            } else {
                val programUri = TvLauncherUtils.upsertWatchNext(requireContext(), metadata)
                lifecycleScope.launch(Dispatchers.IO) {
                    database.metadata().update(
                            metadata.apply { if (programUri != null) watchNext = true })
                }
            }

            // Schedules the next metadata update in METADATA_UPDATE_INTERVAL_MILLIS milliseconds
            Log.d(TAG, "Media metadata updated successfully")
            view?.postDelayed(this, METADATA_UPDATE_INTERVAL_MILLIS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backgroundType = PlaybackSupportFragment.BG_NONE
        database = TvMediaDatabase.getInstance(requireContext())
        val metadata = args.metadata

        // Adds this program to the continue watching row, in case the user leaves before finishing
        val programUri = TvLauncherUtils.upsertWatchNext(requireContext(), metadata)
        if (programUri != null) lifecycleScope.launch(Dispatchers.IO) {
            database.metadata().update(metadata.apply { watchNext = true })
        }

        // Initializes the video player
        player = ExoPlayerFactory.newSimpleInstance(requireContext())
        mediaSession = MediaSessionCompat(requireContext(), getString(R.string.app_name))
        mediaSessionConnector = MediaSessionConnector(mediaSession)

        // Listen to media session events. This is necessary for things like closed captions which
        // can be triggered by things outside of our app, for example via Google Assistant
        mediaSessionConnector.setCaptionCallback(object : MediaSessionConnector.CaptionCallback {
            override fun onCommand(player: Player, controlDispatcher: ControlDispatcher, command: String, extras: Bundle?, cb: ResultReceiver?): Boolean {
                return false
            }

            override fun hasCaptions(player: Player): Boolean {
                // TODO(owahltinez): handle captions
                return true
            }

            override fun onSetCaptioningEnabled(player: Player, enabled: Boolean) {
                // TODO(owahltinez): handle captions
                Log.d(TAG, "onSetCaptioningEnabled() enabled=$enabled")
            }
        })
        mediaSessionConnector.setRatingCallback(object : MediaSessionConnector.RatingCallback {
            override fun onCommand(player: Player, controlDispatcher: ControlDispatcher, command: String, extras: Bundle?, cb: ResultReceiver?): Boolean {
                return false
            }

            override fun onSetRating(player: Player, rating: RatingCompat) {
                // TODO(plammer): handle ratings
                Log.d(TAG, "onSetRating() rating=$rating")
            }

            override fun onSetRating(player: Player, rating: RatingCompat, extras: Bundle) {
                // TODO(plammer): handle ratings
                Log.d(TAG, "onSetRating() rating=$rating")
            }
        })

        // Links our video player with this Leanback video playback fragment
        val playerAdapter = LeanbackPlayerAdapter(
                requireContext(), player, PLAYER_UPDATE_INTERVAL_MILLIS)

        // Enables pass-through of transport controls to our player instance
        playerGlue = MediaPlayerGlue(requireContext(), playerAdapter).apply {
            host = VideoSupportFragmentGlueHost(this@NowPlayingFragment)

            // Adds playback state listeners
            addPlayerCallback(object : PlaybackGlue.PlayerCallback() {

                override fun onPreparedStateChanged(glue: PlaybackGlue?) {
                    super.onPreparedStateChanged(glue)
                    if (glue?.isPrepared == true) {
                        // When playback is ready, skip to last known position
                        val startingPosition = metadata.playbackPositionMillis ?: 0
                        Log.d(TAG, "Setting starting playback position to $startingPosition")
                        seekTo(startingPosition)
                    }
                }

                override fun onPlayCompleted(glue: PlaybackGlue?) {
                    super.onPlayCompleted(glue)

                    // Don't forget to remove irrelevant content from the continue watching row
                    TvLauncherUtils.removeFromWatchNext(requireContext(), args.metadata)

                    // When playback is finished, go back to the previous screen
                    val navController = Navigation.findNavController(
                            requireActivity(), R.id.fragment_container)
                    navController.currentDestination?.id?.let {
                        navController.popBackStack(it, true)
                    }
                }
            })

            // Begins playback automatically
            playWhenPrepared()

            // Displays the current item's metadata
            setMetadata(metadata)
        }

        // Setup the fragment adapter with our player glue presenter
        adapter = ArrayObjectAdapter(playerGlue.playbackRowPresenter).apply {
            add(playerGlue.controlsRow)
        }

        // Adds key listeners
        playerGlue.host.setOnKeyInterceptListener { view, keyCode, event ->

            // Early exit: if the controls overlay is visible, don't intercept any keys
            if (playerGlue.host.isControlsOverlayVisible) return@setOnKeyInterceptListener false

            // TODO(owahltinez): This workaround is necessary for navigation library to work with
            //  Leanback's [PlaybackSupportFragment]
            if (!playerGlue.host.isControlsOverlayVisible &&
                    keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                Log.d(TAG, "Intercepting BACK key for fragment navigation")
                val navController = Navigation.findNavController(
                        requireActivity(), R.id.fragment_container)
                navController.currentDestination?.id?.let { navController.popBackStack(it, true) }
                return@setOnKeyInterceptListener true
            }

            // Skips ahead when user presses DPAD_RIGHT
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.action == KeyEvent.ACTION_DOWN) {
                playerGlue.skipForward()
                preventControlsOverlay(playerGlue)
                return@setOnKeyInterceptListener true
            }

            // Rewinds when user presses DPAD_LEFT
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.action == KeyEvent.ACTION_DOWN) {
                playerGlue.skipBackward()
                preventControlsOverlay(playerGlue)
                return@setOnKeyInterceptListener true
            }

            false
        }
    }

    /** Workaround used to prevent controls overlay from showing and taking focus */
    private fun preventControlsOverlay(playerGlue: MediaPlayerGlue) = view?.postDelayed({
        playerGlue.host.showControlsOverlay(false)
        playerGlue.host.hideControlsOverlay(false)
    }, 10)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(Color.BLACK)
    }

    override fun onResume() {
        super.onResume()

        mediaSessionConnector.setPlayer(player)
        mediaSession.isActive = true

        // Kick off metadata update task which runs periodically in the main thread
        view?.postDelayed(updateMetadataTask, METADATA_UPDATE_INTERVAL_MILLIS)
    }

    /**
     * Deactivates and removes callbacks from [MediaSessionCompat] since the [Player] instance is
     * destroyed in onStop and required metadata could be missing.
     */
    override fun onPause() {
        super.onPause()

        playerGlue.pause()
        mediaSession.isActive = false
        mediaSessionConnector.setPlayer(null)

        view?.post {
            // Launch metadata update task one more time as the fragment becomes paused to ensure
            //  that we have the most up-to-date information
            updateMetadataTask.run()

            // Cancel all future metadata update tasks
            view?.removeCallbacks(updateMetadataTask)
        }
    }

    /** Do all final cleanup in onDestroy */
    override fun onDestroy() {
        super.onDestroy()
        mediaSession.release()
    }

    companion object {
        private val TAG = NowPlayingFragment::class.java.simpleName

        /** How often the player refreshes its views in milliseconds */
        private const val PLAYER_UPDATE_INTERVAL_MILLIS: Int = 100

        /** Time between metadata updates in milliseconds */
        private val METADATA_UPDATE_INTERVAL_MILLIS: Long = TimeUnit.SECONDS.toMillis(10)

        /** Default time used when skipping playback in milliseconds */
        private val SKIP_PLAYBACK_MILLIS: Long = TimeUnit.SECONDS.toMillis(10)
    }
}