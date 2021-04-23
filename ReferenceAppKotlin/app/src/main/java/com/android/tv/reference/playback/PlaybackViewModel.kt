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

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.tv.reference.shared.watchprogress.LoadPlaybackStateListener
import com.android.tv.reference.shared.watchprogress.WatchProgressDatabase
import com.android.tv.reference.shared.watchprogress.WatchProgressPlaybackStateListener
import com.android.tv.reference.shared.watchprogress.WatchProgressRepository
import com.android.tv.reference.watchnext.WatchNextPlaybackStateListener
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

/**
 * ViewModel for playback that allows for reading and writing watch progress.
 */
class PlaybackViewModel(application: Application) :
    AndroidViewModel(application),
    PlaybackStateMachine {

    private val repository: WatchProgressRepository

    private val playbackStateListeners = arrayListOf<PlaybackStateListener>()

    init {
        val watchProgressDao = WatchProgressDatabase.getDatabase(application).watchProgressDao()
        repository = WatchProgressRepository(watchProgressDao)
        playbackStateListeners.addAll(createNonUiPlaybackStateListeners())
    }

    /**
     * Adds a [PlaybackStateListener] to be notified of [VideoPlaybackState] changes.
     */
    fun addPlaybackStateListener(listener: PlaybackStateListener) {
        playbackStateListeners.add(listener)
    }

    /**
     * Removes the [PlaybackStateListener] so it receives no further [VideoPlaybackState] changes.
     */
    fun removePlaybackStateListener(listener: PlaybackStateListener) {
        playbackStateListeners.remove(listener)
    }

    override fun onStateChange(state: VideoPlaybackState) {
        Timber.d("Playback state machine updated to $state")
        playbackStateListeners.forEach {
            it.onChanged(state)
        }
    }

    override fun onCleared() {
        playbackStateListeners.forEach { it.onDestroy() }
    }

    /**
     * Creates and returns a new List of non-UI [PlaybackStateListener] objects to register with the
     * state machine.
     */
    private fun createNonUiPlaybackStateListeners(): List<PlaybackStateListener> {
        return listOf(
            LoadPlaybackStateListener(
                    stateMachine = this,
                    watchProgressRepository = repository,
            ),
            WatchNextPlaybackStateListener(getApplication()),
            WatchProgressPlaybackStateListener(
                    watchProgressRepository = repository,
                    coroutineScope = viewModelScope,
                    coroutineDispatcher = Dispatchers.IO
            )
        )
    }
}
