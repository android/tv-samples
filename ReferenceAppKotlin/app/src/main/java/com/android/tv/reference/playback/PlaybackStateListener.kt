/*
 * Copyright 2021 Google LLC
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

/**
 * Listens to changes in [VideoPlaybackState].
 */
interface PlaybackStateListener {

    /**
     * Called with a [VideoPlaybackState] whenever the playback state machine changes states.
     */
    fun onChanged(state: VideoPlaybackState)

    /**
     * Called when the listener is being destroyed and should clean up any references.
     */
    fun onDestroy() {}
}