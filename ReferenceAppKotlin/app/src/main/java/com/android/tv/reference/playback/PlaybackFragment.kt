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
import android.util.Log
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.tv.reference.R
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.watchprogress.WatchProgress
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

/**
 * Fragment that plays video content with ExoPlayer
 */
class PlaybackFragment : VideoSupportFragment() {

    private lateinit var video: Video
    private lateinit var watchProgress: WatchProgress
    private lateinit var handler: Handler

    // Self-posting Runnable that posts an updated watch progress
    private val updateWatchProgressRunnable = Runnable {
        saveUpdatedWatchProgress()
        scheduleWatchProgressUpdate()
    }

    private lateinit var exoplayer: ExoPlayer
    private lateinit var viewModel: PlaybackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the video data
        video = PlaybackFragmentArgs.fromBundle(requireArguments()).video

        // Create the handler for posting watch progress updates
        handler = Handler()

        // Create a WatchProgress object to update as the user watches the video
        watchProgress = WatchProgress(video.videoUri, 0)

        // Load the ViewModel for this specific video
        viewModel = ViewModelProvider(
            this,
            PlaybackViewModelFactory(requireActivity().application, video.videoUri)
        ).get(PlaybackViewModel::class.java)
        viewModel.watchProgress.observe(
            this,
            object : Observer<WatchProgress> {
                override fun onChanged(newWatchProgress: WatchProgress?) {
                    // Stop listening now that the starting point is known
                    viewModel.watchProgress.removeObserver(this)

                    if (newWatchProgress == null) {
                        Log.v(TAG, "No existing WatchProgress, start from the beginning")
                    } else {
                        Log.v(
                            TAG,
                            "WatchProgress start position loaded: " + newWatchProgress.startPosition
                        )
                        watchProgress.startPosition = newWatchProgress.startPosition
                    }
                    startPlaybackFromWatchProgress()
                }
            }
        )

        // Prepare the player and related pieces
        preparePlayer()
        prepareGlue()
        prepareMediaSession()
    }

    private fun saveUpdatedWatchProgress() {
        watchProgress.startPosition = exoplayer.currentPosition
        Log.v(TAG, "Saving updated WatchProgress position: ${watchProgress.startPosition}")
        viewModel.update(watchProgress)
    }

    override fun onPause() {
        super.onPause()
        cancelWatchProgressUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoplayer.release()
    }

    private fun preparePlayer() {
        val dataSourceFactory = DefaultDataSourceFactory(
            requireContext(),
            Util.getUserAgent(requireContext(), getString(R.string.app_name))
        )
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(video.videoUri))
        exoplayer = SimpleExoPlayer.Builder(requireContext()).build().apply {
            prepare(mediaSource, false, true)
        }
        exoplayer.addListener(object : Player.EventListener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    scheduleWatchProgressUpdate()
                } else {
                    cancelWatchProgressUpdates()
                    saveUpdatedWatchProgress()
                }
            }

            override fun onSeekProcessed() {
                saveUpdatedWatchProgress()
            }
        })
    }

    private fun prepareGlue() {
        PlaybackTransportControlGlue(
            requireContext(),
            LeanbackPlayerAdapter(requireContext(), exoplayer, PLAYER_UPDATE_INTERVAL_MILLIS)
        ).apply {
            host = VideoSupportFragmentGlueHost(this@PlaybackFragment)
            title = video.name
        }
    }

    private fun prepareMediaSession() {
        // TODO
    }

    private fun startPlaybackFromWatchProgress() {
        Log.v(TAG, "Starting playback from ${watchProgress.startPosition}")
        exoplayer.seekTo(watchProgress.startPosition)
        exoplayer.playWhenReady = true
    }

    private fun scheduleWatchProgressUpdate() {
        Log.v(TAG, "Scheduling watch progress updates")
        handler.postDelayed(updateWatchProgressRunnable, WATCH_PROGRESS_SAVE_INTERVAL_MILLIS)
    }

    private fun cancelWatchProgressUpdates() {
        Log.v(TAG, "Canceling watch progress updates")
        handler.removeCallbacks(updateWatchProgressRunnable)
    }

    companion object {
        // How often to update the player UI
        private const val PLAYER_UPDATE_INTERVAL_MILLIS = 50

        // Logging tag
        private const val TAG = "PlaybackFragment"

        // How often to save watch progress to the database
        private const val WATCH_PROGRESS_SAVE_INTERVAL_MILLIS = 10 * 1000L
    }
}
