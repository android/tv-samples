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
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.tv.reference.shared.watchprogress.WatchProgress
import com.android.tv.reference.shared.watchprogress.WatchProgressDatabase
import com.android.tv.reference.shared.watchprogress.WatchProgressRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for playback that allows for reading and writing watch progress
 */
class PlaybackViewModel(application: Application, videoId: String) : AndroidViewModel(application) {

    private val repository: WatchProgressRepository

    var watchProgress: LiveData<WatchProgress>

    init {
        val watchProgressDao = WatchProgressDatabase.getDatabase(application).watchProgressDao()
        repository = WatchProgressRepository(watchProgressDao)
        watchProgress = repository.getWatchProgressByVideoId(videoId)
    }

    /**
     * Updates the watch progress
     */
    fun update(watchProgress: WatchProgress) = viewModelScope.launch {
        repository.insert(watchProgress)
    }
}

/**
 * A ViewModelFactory to instantiate the PlaybackViewModel with a specific video ID
 */
class PlaybackViewModelFactory(private val application: Application, private val videoId: String) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PlaybackViewModel(application, videoId) as T
    }
}
