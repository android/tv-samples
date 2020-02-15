/*
 * Copyright 2020 Google LLC
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

package com.android.tv.reference.playback

import android.net.Uri
import android.os.Bundle
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.PlaybackTransportControlGlue
import com.android.tv.reference.R
import com.android.tv.reference.shared.datamodel.Video
import com.google.android.exoplayer2.ExoPlayer
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

    private lateinit var exoplayer: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        video = PlaybackFragmentArgs.fromBundle(requireArguments()).video

        preparePlayer()
        prepareGlue()
        prepareMediaSession()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoplayer.release()
    }

    private fun preparePlayer() {
        val dataSourceFactory = DefaultDataSourceFactory(requireContext(), Util.getUserAgent(requireContext(), getString(
            R.string.app_name)))
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(video.videoUri))
        exoplayer = SimpleExoPlayer.Builder(requireContext()).build().apply {
            prepare(mediaSource)
            playWhenReady = true
        }
    }

    private fun prepareGlue() {
        PlaybackTransportControlGlue(requireContext(), LeanbackPlayerAdapter(requireContext(), exoplayer, PLAYER_UPDATE_INTERVAL_MILLIS)).apply {
            host = VideoSupportFragmentGlueHost(this@PlaybackFragment)
            title = video.name
        }
    }

    private fun prepareMediaSession() {
        // TODO
    }

    companion object {
        // How often to update the player UI
        private const val PLAYER_UPDATE_INTERVAL_MILLIS = 250
    }
}