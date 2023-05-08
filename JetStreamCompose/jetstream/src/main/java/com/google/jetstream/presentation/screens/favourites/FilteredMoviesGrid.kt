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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvGridItemSpan
import androidx.tv.foundation.lazy.grid.TvLazyGridState
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.material3.Border
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardLayoutDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.StandardCardLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.theme.JetStreamBottomListPadding
import com.google.jetstream.presentation.theme.JetStreamCardShape

@Composable
fun FilteredMoviesGrid(
    state: TvLazyGridState,
    movies: List<Movie>,
    movieListRange: SnapshotStateList<Int>,
    onMovieClick: (movieId: String) -> Unit,
) {
    TvLazyVerticalGrid(
        state = state,
        modifier = Modifier.fillMaxSize(),
        columns = TvGridCells.Fixed(6),
        content = {
            movies
                .filterIndexed { index, _ ->
                    if (movieListRange.isEmpty()) true
                    else movieListRange.contains(index)
                }
                .forEachIndexed { index, movie ->
                    item {
                        key(movie.id) {
                            StandardCardLayout(
                                modifier = Modifier
                                    .aspectRatio(1 / 1.5f)
                                    .padding(vertical = 8.dp)
                                    .padding(end = if ((index + 1) % 6 == 0) 0.dp else 16.dp),
                                imageCard = {
                                    CardLayoutDefaults.ImageCard(
                                        onClick = { onMovieClick(movie.id) },
                                        shape = CardDefaults.shape(shape = JetStreamCardShape),
                                        scale = CardDefaults.scale(focusedScale = 1f),
                                        border = CardDefaults.border(
                                            focusedBorder = Border(
                                                border = BorderStroke(
                                                    width = 2.dp,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                ),
                                                shape = JetStreamCardShape
                                            )
                                        ),
                                        interactionSource = it
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(movie.posterUri)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = StringConstants
                                                .Composable
                                                .ContentDescription
                                                .moviePoster(movie.name),
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                },
                                title = {}
                            )
                        }
                    }
                }
            item(span = { TvGridItemSpan(currentLineSpan = 6) }) {
                Spacer(modifier = Modifier.padding(bottom = JetStreamBottomListPadding))
            }
        }
    )
}
