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

package com.google.jetstream.presentation.screens.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.PivotOffsets
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.rememberTvLazyListState
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.LocalMovieRepository
import com.google.jetstream.presentation.common.MoviesRow
import com.google.jetstream.presentation.screens.dashboard.rememberChildPadding

@Composable
fun HomeScreen(
    onMovieClick: (movie: Movie) -> Unit,
    goToVideoPlayer: () -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    isTopBarVisible: Boolean
) {
    val childPadding = rememberChildPadding()
    val tvLazyListState = rememberTvLazyListState()
    val shouldShowTopBar by remember {
        derivedStateOf {
            tvLazyListState.firstVisibleItemIndex == 0 &&
                    tvLazyListState.firstVisibleItemScrollOffset < 300
        }
    }

    val movieRepository = LocalMovieRepository.current!!

    val featuredMovies = remember { movieRepository.getFeaturedMovies() }
    val trendingMovies = remember { movieRepository.getTrendingMovies() }
    val top10Movies = remember { movieRepository.getTop10Movies() }
    val nowPlayingMovies = remember { movieRepository.getNowPlayingMovies() }

    LaunchedEffect(shouldShowTopBar) {
        onScroll(shouldShowTopBar)
    }
    LaunchedEffect(isTopBarVisible) {
        if (isTopBarVisible) tvLazyListState.animateScrollToItem(0)
    }

    var immersiveListHasFocus by remember { mutableStateOf(false) }
    TvLazyColumn(
        modifier = Modifier.fillMaxSize(),
        pivotOffsets = if (immersiveListHasFocus) PivotOffsets(0f, 0f) else PivotOffsets(),
        state = tvLazyListState,
        contentPadding = PaddingValues(
            bottom = LocalConfiguration.current.screenHeightDp.dp.times(0.19f)
        )
    ) {
        item(contentType = "FeaturedMoviesCarousel") {
            FeaturedMoviesCarousel(
                movies = featuredMovies,
                padding = childPadding,
                goToVideoPlayer = goToVideoPlayer
            )
        }
        item(contentType = "MoviesRow") {
            MoviesRow(
                modifier = Modifier.padding(top = 16.dp),
                movies = trendingMovies,
                title = StringConstants.Composable.HomeScreenTrendingTitle,
                onMovieClick = onMovieClick
            )
        }
        item(contentType = "Top10MoviesList") {
            Top10MoviesList(
                modifier = Modifier.onFocusChanged {
                    immersiveListHasFocus = it.hasFocus
                },
                moviesState = top10Movies,
                onMovieClick = onMovieClick
            )
        }
        item(contentType = "MoviesRow") {
            MoviesRow(
                modifier = Modifier.padding(top = 16.dp),
                movies = nowPlayingMovies,
                title = StringConstants.Composable.HomeScreenNowPlayingMoviesTitle,
                onMovieClick = onMovieClick
            )
        }
    }
}
