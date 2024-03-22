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

package com.google.jetstream.presentation.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.PivotOffsets
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.itemsIndexed
import androidx.tv.material3.Border
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardLayoutDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.ImmersiveListScope
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.StandardCardLayout
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.screens.dashboard.rememberChildPadding
import com.google.jetstream.presentation.theme.JetStreamBorderWidth
import com.google.jetstream.presentation.theme.JetStreamCardShape
import com.google.jetstream.presentation.utils.createInitialFocusRestorerModifiers
import com.google.jetstream.presentation.utils.ifElse

enum class ItemDirection(val aspectRatio: Float) {
    Vertical(10.5f / 16f),
    Horizontal(16f / 9f);
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MoviesRow(
    modifier: Modifier = Modifier,
    itemDirection: ItemDirection = ItemDirection.Vertical,
    startPadding: Dp = rememberChildPadding().start,
    endPadding: Dp = rememberChildPadding().end,
    title: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.headlineLarge.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 30.sp
    ),
    showItemTitle: Boolean = true,
    showIndexOverImage: Boolean = false,
    focusedItemIndex: (index: Int) -> Unit = {},
    movies: List<Movie>,
    onMovieClick: (movie: Movie) -> Unit = {}
) {
    Column(
        modifier = modifier.focusGroup()
    ) {
        title?.let { nnTitle ->
            Text(
                text = nnTitle,
                style = titleStyle,
                modifier = Modifier
                    .alpha(1f)
                    .padding(start = startPadding)
                    .padding(vertical = 16.dp)
            )
        }

        AnimatedContent(
            targetState = movies,
            label = "",
        ) { movieState ->
            val focusRestorerModifiers = createInitialFocusRestorerModifiers()

            TvLazyRow(
                modifier = Modifier
                    .then(focusRestorerModifiers.parentModifier),
                pivotOffsets = PivotOffsets(parentFraction = 0.07f),
                contentPadding = PaddingValues(
                    start = startPadding,
                    end = endPadding,
                ),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                itemsIndexed(movieState, key = { _, movie -> movie.id }) { index, movie ->
                    MoviesRowItem(
                        modifier = Modifier
                            .ifElse(
                                index == 0,
                                focusRestorerModifiers.childModifier
                            )
                            .weight(1f),
                        focusedItemIndex = focusedItemIndex,
                        index = index,
                        itemDirection = itemDirection,
                        onMovieClick = onMovieClick,
                        movie = movie,
                        showItemTitle = showItemTitle,
                        showIndexOverImage = showIndexOverImage
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalTvMaterial3Api::class)
@Composable
fun ImmersiveListScope.ImmersiveListMoviesRow(
    modifier: Modifier = Modifier,
    itemDirection: ItemDirection = ItemDirection.Vertical,
    startPadding: Dp = rememberChildPadding().start,
    endPadding: Dp = rememberChildPadding().end,
    title: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.headlineLarge.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 30.sp
    ),
    showItemTitle: Boolean = true,
    showIndexOverImage: Boolean = false,
    focusedItemIndex: (index: Int) -> Unit = {},
    movies: List<Movie>,
    onMovieClick: (movie: Movie) -> Unit = {}
) {
    Column(
        modifier = modifier.focusGroup()
    ) {
        title?.let { nnTitle ->
            Text(
                text = nnTitle,
                style = titleStyle,
                modifier = Modifier
                    .alpha(1f)
                    .padding(start = startPadding)
                    .padding(vertical = 16.dp)
            )
        }

        AnimatedContent(
            targetState = movies,
            label = "",
        ) { movieState ->
            TvLazyRow(
                modifier = Modifier.focusRestorer(),
                pivotOffsets = PivotOffsets(parentFraction = 0.07f),
                contentPadding = PaddingValues(start = startPadding, end = endPadding),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                movieState.forEachIndexed { index, movie ->
                    item {
                        key(movie.id) {
                            MoviesRowItem(
                                modifier = Modifier
                                    .weight(1f)
                                    .immersiveListItem(index),
                                focusedItemIndex = focusedItemIndex,
                                index = index,
                                itemDirection = itemDirection,
                                onMovieClick = onMovieClick,
                                movie = movie,
                                showItemTitle = showItemTitle,
                                showIndexOverImage = showIndexOverImage
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class, ExperimentalTvMaterial3Api::class)
private fun MoviesRowItem(
    modifier: Modifier = Modifier,
    focusedItemIndex: (index: Int) -> Unit,
    index: Int,
    itemDirection: ItemDirection,
    onMovieClick: (movie: Movie) -> Unit,
    movie: Movie,
    showItemTitle: Boolean,
    showIndexOverImage: Boolean
) {
    var isItemFocused by remember { mutableStateOf(false) }

    StandardCardLayout(
        modifier = Modifier
            .onFocusChanged {
                isItemFocused = it.isFocused
                if (isItemFocused) {
                    focusedItemIndex(index)
                }
            }
            .focusProperties {
                if (index == 0) {
                    left = FocusRequester.Cancel
                }
            }
            .then(modifier),
        title = {
            MoviesRowItemText(
                showItemTitle = showItemTitle,
                isItemFocused = isItemFocused,
                movie = movie
            )
        },
        imageCard = {
            CardLayoutDefaults.ImageCard(
                onClick = { onMovieClick(movie) },
                shape = CardDefaults.shape(JetStreamCardShape),
                border = CardDefaults.border(
                    focusedBorder = Border(
                        border = BorderStroke(
                            width = JetStreamBorderWidth,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = JetStreamCardShape
                    )
                ),
                scale = CardDefaults.scale(focusedScale = 1f),
                interactionSource = it
            ) {
                MoviesRowItemImage(
                    modifier = Modifier.aspectRatio(itemDirection.aspectRatio),
                    showIndexOverImage = showIndexOverImage,
                    movie = movie,
                    index = index
                )
            }
        },
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun MoviesRowItemImage(
    showIndexOverImage: Boolean,
    movie: Movie,
    index: Int,
    modifier: Modifier = Modifier,
) {
    Box(contentAlignment = Alignment.CenterStart) {
        AsyncImage(
            modifier = modifier
                .fillMaxWidth()
                .drawWithContent {
                    drawContent()
                    if (showIndexOverImage) {
                        drawRect(
                            color = Color.Black.copy(
                                alpha = 0.1f
                            )
                        )
                    }
                },
            model = ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(movie.posterUri)
                .build(),
            contentDescription = "movie poster of ${movie.name}",
            contentScale = ContentScale.Crop
        )
        if (showIndexOverImage) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "#${index.inc()}",
                style = MaterialTheme.typography.displayLarge
                    .copy(
                        shadow = Shadow(
                            offset = Offset(0.5f, 0.5f),
                            blurRadius = 5f
                        ),
                        color = Color.White
                    ),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun MoviesRowItemText(
    showItemTitle: Boolean,
    isItemFocused: Boolean,
    movie: Movie,
    modifier: Modifier = Modifier
) {
    if (showItemTitle) {
        val movieNameAlpha by animateFloatAsState(
            targetValue = if (isItemFocused) 1f else 0f,
            label = "",
        )
        Text(
            text = movie.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center,
            modifier = modifier
                .alpha(movieNameAlpha)
                .fillMaxWidth()
                .padding(top = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
