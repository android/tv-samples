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

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.playback.PlaybackStateMachine
import com.android.tv.reference.shared.playback.VideoPlaybackState
import timber.log.Timber

/**
 * On [VideoPlaybackState.Load], loads the watch progress for a video and updates the state to
 * [VideoPlaybackState.Prepare].
 */
class LoadPlaybackStateListener(
    private val stateMachine: PlaybackStateMachine,
    private val watchProgressRepository: WatchProgressRepository,
    private val lifecycleOwner: LifecycleOwner
) : Observer<VideoPlaybackState> {

    private var watchProgress: LiveData<WatchProgress>? = null
    private var video: Video? = null
    private val watchProgressObserver = Observer<WatchProgress> { newWatchProgress ->

        val savedPosition = newWatchProgress?.startPosition ?: 0L
        val startPosition =
            if (newWatchProgress != null &&
                (savedPosition > 0 && !video!!.isAfterEndCreditsPosition(savedPosition))
            ) {
                Timber.v("WatchProgress start position loaded: ${newWatchProgress.startPosition}")
                newWatchProgress.startPosition
            } else {
                Timber.v(
                    /*  ktlint-disable max-line-length */
                    "No valid WatchProgress or previous position is after end credits, start from the beginning"
                )
                0
            }
        stateMachine.onStateChange(VideoPlaybackState.Prepare(video!!, startPosition))
    }

    override fun onChanged(state: VideoPlaybackState) {
        if (state !is VideoPlaybackState.Load) {
            // Prevent getting notified as the watch progress is updated. This could trigger an
            // infinite loop as this listener will trigger the Prepare state every time the progress
            // is updated. When preparing the video, we seek to the starting position. Seeking
            // triggers updating the watch progress. And thus the infinite loop.
            // Load -> Observe WatchProgress -> Prepare -> Seek -> Update WatchProgress -> Observer
            // Notified -> Prepare -> Cycle Detected
            watchProgress?.removeObserver(watchProgressObserver)
            return
        }

        Timber.d("Loading watch progress for video ${state.video.name}")
        video = state.video
        watchProgress = watchProgressRepository.getWatchProgressByVideoId(state.video.id).apply {
            observe(lifecycleOwner, watchProgressObserver)
        }
    }
}
