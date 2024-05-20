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

package com.google.jetstream.presentation.screens.categories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetstream.data.entities.MovieCategoryDetails
import com.google.jetstream.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class CategoryMovieListScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    movieRepository: MovieRepository
) : ViewModel() {

    val uiState =
        savedStateHandle.getStateFlow<String?>(
            CategoryMovieListScreen.CategoryIdBundleKey,
            null
        ).map { id ->
            if (id == null) {
                CategoryMovieListScreenUiState.Error
            } else {
                val categoryDetails = movieRepository.getMovieCategoryDetails(id)
                CategoryMovieListScreenUiState.Done(categoryDetails)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CategoryMovieListScreenUiState.Loading
        )
}

sealed interface CategoryMovieListScreenUiState {
    object Loading : CategoryMovieListScreenUiState
    object Error : CategoryMovieListScreenUiState
    data class Done(val movieCategoryDetails: MovieCategoryDetails) : CategoryMovieListScreenUiState
}
