/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.presentation.screens.favourites

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetstream.data.entities.MovieList
import com.google.jetstream.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavouriteScreenViewModel @Inject constructor(
    movieRepository: MovieRepository
) : ViewModel() {


    private val selectedFilterList = MutableStateFlow(FilterList())

    val uiState = combine(
        selectedFilterList,
        movieRepository.getFavouriteMovies()
    ) { filterList, movieList ->
        val condition = filterList.toFilterCondition()
        val filtered = movieList.filterIndexed { index, _ ->
            condition.idList.contains(index)
        }
        FavouriteScreenUiState.Ready(MovieList(filtered), filterList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FavouriteScreenUiState.Loading
    )

    fun updateSelectedFilterList(filterList: FilterList) {
        selectedFilterList.value = filterList
    }

    companion object {
        val filterList = FilterList(
            listOf(
                FilterCondition.Movies,
                FilterCondition.TvShows,
                FilterCondition.AddedLastWeek,
                FilterCondition.AvailableIn4K
            )
        )
    }
}

sealed interface FavouriteScreenUiState {
    data object Loading : FavouriteScreenUiState
    data class Ready(val favouriteMovieList: MovieList, val selectedFilterList: FilterList) :
        FavouriteScreenUiState

}

@Immutable
data class FilterList(val items: List<FilterCondition> = emptyList()) {
    fun toFilterCondition(): FilterCondition {
        if (items.isEmpty()) {
            return FilterCondition.None
        }
        val list: List<Int> = items.asSequence().map {
            it.idList
        }.fold(emptyList()) { acc, ints ->
            acc + ints
        }
        return FilterCondition(list)
    }
}

@Immutable
data class FilterCondition(val idList: List<Int>) {

    companion object {
        val None = FilterCondition((0..28).toList())

        val Movies = FilterCondition((0..9).toList())

        val TvShows = FilterCondition((10..17).toList())

        val AddedLastWeek = FilterCondition((18..23).toList())

        val AvailableIn4K = FilterCondition((24..28).toList())
    }

}