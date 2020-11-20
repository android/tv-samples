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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.tv.reference.shared.playback.PlaybackStateMachine
import com.android.tv.reference.shared.playback.VideoPlaybackState
import com.android.tv.reference.shared.watchprogress.LoadPlaybackStateListener
import com.android.tv.reference.shared.watchprogress.WatchProgress
import com.android.tv.reference.shared.watchprogress.WatchProgressDatabase
import com.android.tv.reference.shared.watchprogress.WatchProgressRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for playback that allows for reading and writing watch progress
 */
class PlaybackViewModel(application: Application) :
    AndroidViewModel(application),
    PlaybackStateMachine {

    private val repository: WatchProgressRepository

    private val _playbackState = MutableLiveData<VideoPlaybackState>()
    val playbackState: LiveData<VideoPlaybackState> = _playbackState

    init {
        val watchProgressDao = WatchProgressDatabase.getDatabase(application).watchProgressDao()
        repository = WatchProgressRepository(watchProgressDao)
    }

    /**
     * Updates the watch progress
     */
    fun update(watchProgress: WatchProgress) = viewModelScope.launch {
        repository.insert(watchProgress)
    }

    /**
     * Registers non-UI related listeners to the playback state machine. UI-related listeners should
     * directly register themselves on the live data object. In lei of a DI technology, this method
     * acts as manual DI.
     *
     * The state machine is agnostic of the Android Lifecycle process. Since the lifecycle process
     * is a detail of the fragment/ViewModel, this method acts as the entry point of the lifecycle
     * process into the state machine for the listeners that observe LiveData.
     */
    fun registerStateListeners(owner: LifecycleOwner) {
        playbackState.observe(
            owner,
            LoadPlaybackStateListener(
                stateMachine = this,
                watchProgressRepository = repository,
                lifecycleOwner = owner
            )
        )
    }

    override fun onStateChange(state: VideoPlaybackState) {
        _playbackState.postValue(state)
    }
}
