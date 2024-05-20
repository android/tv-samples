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

package com.google.jetstream.presentation.screens.favourites

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.jetstream.data.entities.MovieList
import com.google.jetstream.presentation.common.Loading
import com.google.jetstream.presentation.screens.dashboard.rememberChildPadding

@Composable
fun FavouritesScreen(
    onMovieClick: (movieId: String) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    isTopBarVisible: Boolean,
    favouriteScreenViewModel: FavouriteScreenViewModel = hiltViewModel()
) {
    val uiState by favouriteScreenViewModel.uiState.collectAsStateWithLifecycle()
    when (val s = uiState) {
        is FavouriteScreenUiState.Loading -> {
            Loading(modifier = Modifier.fillMaxSize())
        }
        is FavouriteScreenUiState.Ready -> {
            Catalog(
                favouriteMovieList = s.favouriteMovieList,
                onMovieClick = onMovieClick,
                onScroll = onScroll,
                isTopBarVisible = isTopBarVisible,
                modifier = Modifier.fillMaxSize(),
                filterList = FavouriteScreenViewModel.filterList,
                selectedFilterList = s.selectedFilterList,
                onSelectedFilterListUpdated = favouriteScreenViewModel::updateSelectedFilterList
            )
        }
    }
}

@Composable
private fun Catalog(
    favouriteMovieList: MovieList,
    filterList: FilterList,
    selectedFilterList: FilterList,
    onMovieClick: (movieId: String) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    onSelectedFilterListUpdated: (FilterList) -> Unit,
    isTopBarVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    val childPadding = rememberChildPadding()
    val filteredMoviesGridState = rememberLazyGridState()
    val shouldShowTopBar by remember {
        derivedStateOf {
            filteredMoviesGridState.firstVisibleItemIndex == 0 &&
                filteredMoviesGridState.firstVisibleItemScrollOffset < 100
        }
    }

    LaunchedEffect(shouldShowTopBar) {
        onScroll(shouldShowTopBar)
    }
    LaunchedEffect(isTopBarVisible) {
        if (isTopBarVisible) filteredMoviesGridState.animateScrollToItem(0)
    }

    val chipRowTopPadding by animateDpAsState(
        targetValue = if (shouldShowTopBar) 0.dp else childPadding.top, label = ""
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = childPadding.start)
    ) {
        MovieFilterChipRow(
            filterList = filterList,
            selectedFilterList = selectedFilterList,
            modifier = Modifier.padding(top = chipRowTopPadding),
            onSelectedFilterListUpdated = onSelectedFilterListUpdated
        )
        FilteredMoviesGrid(
            state = filteredMoviesGridState,
            movieList = favouriteMovieList,
            onMovieClick = onMovieClick
        )
    }
}
