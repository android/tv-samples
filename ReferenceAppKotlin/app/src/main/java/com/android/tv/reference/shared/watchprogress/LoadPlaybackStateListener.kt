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
package com.android.tv.reference.shared.watchprogress

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.android.tv.reference.playback.PlaybackStateListener
import com.android.tv.reference.playback.PlaybackStateMachine
import com.android.tv.reference.playback.VideoPlaybackState
import com.android.tv.reference.shared.datamodel.Video
import timber.log.Timber

/**
 * On [VideoPlaybackState.Load], loads the watch progress for a video and updates the state to
 * [VideoPlaybackState.Prepare].
 */
class LoadPlaybackStateListener(
    private val stateMachine: PlaybackStateMachine,
    private val watchProgressRepository: WatchProgressRepository,
) : PlaybackStateListener {

    private var startPosition: Long? = null

    private var watchProgress: LiveData<WatchProgress>? = null
    private var video: Video? = null
    private val watchProgressObserver = object: Observer<WatchProgress> {
        override fun onChanged(newWatchProgress: WatchProgress?) {
            startPosition = newWatchProgress?.startPosition ?: 0L
            if (video!!.isAfterEndCreditsPosition(startPosition!!)) {
                // Restart from the beginning.
                Timber.v("WatchProgress position is after end credits; start over")
                startPosition = 0
            }

            // Before updating the state to Prepare, stop observing watch progress updates, so
            // future updates will not trigger this observer again.
            watchProgress?.removeObserver(this)

            // Now that watch progress updates aren't being observed, send the Prepare event
            // to allow the video to seek to the correct place once buffered.
            stateMachine.onStateChange(VideoPlaybackState.Prepare(video!!, startPosition!!))
        }
    }

    override fun onChanged(state: VideoPlaybackState) {
        if (state !is VideoPlaybackState.Load) {
            if (state is VideoPlaybackState.Pause) {
                // When the video is paused, store the timestamp. This can be used to skip loading
                // from the watch progress repository if another Load is triggered. This assumes
                // the listener is only ever used for one video, which is currently the case. If
                // the listener is reused across videos (for example, if the user finishes episode
                // 1 and episode 2 starts with the same listener), then it needs to be updated to
                // also track the video ID.
                startPosition = state.position
            }
            return
        }
        video = state.video

        if (startPosition == null) {
            // Unknown start position, load it from the watch progress repository.
            Timber.d("Loading watch progress for video ${state.video.name}")
            watchProgress = watchProgressRepository.getWatchProgressByVideoId(state.video.id)
            watchProgress?.observeForever(watchProgressObserver)
        } else {
            // Start position already known, just use it directly.
            Timber.d("Using in-memory position to start video at $startPosition ms")
            stateMachine.onStateChange(VideoPlaybackState.Prepare(video!!, startPosition!!))
        }
    }

    override fun onDestroy() {
        watchProgress?.removeObserver(watchProgressObserver)
    }
}
