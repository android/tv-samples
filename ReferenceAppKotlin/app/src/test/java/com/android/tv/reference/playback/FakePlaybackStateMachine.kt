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

/**
 * Fake [PlaybackStateMachine] to be used for testing. To confirm the state, use
 * [FakePlaybackStateMachine.playbackState] to access the current state. To check a progression of
 * states, use [FakePlaybackStateMachine.playbackStateHistory].
 */
object FakePlaybackStateMachine : PlaybackStateMachine {

    var playbackState: VideoPlaybackState? = null

    private val mutablePlaybackStateHistory = mutableListOf<VideoPlaybackState>()
    val playbackStateHistory: List<VideoPlaybackState> = mutablePlaybackStateHistory

    override fun onStateChange(state: VideoPlaybackState) {
        playbackState = state
        mutablePlaybackStateHistory.add(state)
    }

    /** In @After methods, call [reset] to tear down the state machine. */
    fun reset() {
        playbackState = null
        mutablePlaybackStateHistory.clear()
    }
}
