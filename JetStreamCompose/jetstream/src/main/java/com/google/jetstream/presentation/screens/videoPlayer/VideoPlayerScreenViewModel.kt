/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.presentation.screens.videoPlayer

import androidx.annotation.OptIn
import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


/**
 * A [VideoPlayerScreenViewModel] for the [VideoPlayerScreen]
 */
@UnstableApi
@HiltViewModel
@OptIn(UnstableApi::class)
class VideoPlayerScreenViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository,
    private val playerManager: VideoPlayerManager
) : ViewModel() {

    private val movieIdFlow = savedStateHandle.getStateFlow<String?>(
        VideoPlayerScreen.MovieIdBundleKey,
        null
    )

    val uiState: StateFlow<VideoPlayerScreenUiState> = movieIdFlow
        .map { id ->
            if (id == null) {
                VideoPlayerScreenUiState.Error
            } else {

                try {
                    val details = repository.getMovieDetails(id)
                    playerManager.load(details)
                    VideoPlayerScreenUiState.Done(details)
                } catch (e: kotlinx.coroutines.CancellationException) {
                    throw e
                } catch (e: Exception) {
                    VideoPlayerScreenUiState.Error
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = VideoPlayerScreenUiState.Loading
        )

    val player: ExoPlayer get() = playerManager.player

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}


@Immutable
sealed class VideoPlayerScreenUiState {
    data object Loading : VideoPlayerScreenUiState()
    data object Error : VideoPlayerScreenUiState()
    data class Done(val movieDetails: MovieDetails) : VideoPlayerScreenUiState()
}
