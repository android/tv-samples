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

import androidx.compose.animation.Crossfade
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.ImmersiveList
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.jetstream.R
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.common.ImmersiveListMoviesRow
import com.google.jetstream.presentation.common.ItemDirection
import com.google.jetstream.presentation.screens.dashboard.rememberChildPadding

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun Top10MoviesList(
    modifier: Modifier = Modifier,
    moviesState: List<Movie>,
    onMovieClick: (movie: Movie) -> Unit
) {
    var currentItemIndex by remember { mutableStateOf(0) }
    var isListFocused by remember { mutableStateOf(false) }
    var currentYCoord: Float? by remember { mutableStateOf(null) }

    ImmersiveList(
        modifier = modifier.onGloballyPositioned { currentYCoord = it.positionInWindow().y },
        background = { _, listHasFocus ->
            isListFocused = listHasFocus
            val gradientColor = MaterialTheme.colorScheme.surface
            AnimatedVisibility(
                visible = isListFocused,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
                modifier = Modifier
                    .height(432.dp)
                    .gradientOverlay(gradientColor)
            ) {
                val movie = remember(moviesState, currentItemIndex) {
                    moviesState[currentItemIndex]
                }

                Crossfade(
                    targetState = movie.posterUri,
                    label = "posterUriCrossfade"
                ) { posterUri ->
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(432.dp),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(posterUri)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }

            }
        },
        list = {
            Column {
                // TODO this causes the whole vertical list to jump
                if (isListFocused) {
                    val movie = remember(moviesState, currentItemIndex) {
                        moviesState[currentItemIndex]
                    }
                    Column(
                        modifier = Modifier.padding(
                            start = rememberChildPadding().start,
                            bottom = 32.dp
                        )
                    ) {
                        Text(text = movie.name, style = MaterialTheme.typography.displaySmall)
                        Spacer(modifier = Modifier.padding(top = 8.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            text = movie.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                            fontWeight = FontWeight.Light
                        )
                    }
                }
                ImmersiveListMoviesRow(
                    itemDirection = ItemDirection.Horizontal,
                    movies = moviesState,
                    title = if (isListFocused) null
                    else stringResource(R.string.top_10_movies_title),
                    showItemTitle = !isListFocused,
                    onMovieClick = onMovieClick,
                    showIndexOverImage = true,
                    focusedItemIndex = { focusedIndex -> currentItemIndex = focusedIndex }
                )
            }
        }
    )
}

fun Modifier.gradientOverlay(gradientColor: Color) = this then drawWithCache {
    val horizontalGradient = Brush.horizontalGradient(
        colors = listOf(
            gradientColor,
            Color.Transparent
        ),
        startX = size.width.times(0.2f),
        endX = size.width.times(0.7f)
    )
    val verticalGradient = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            gradientColor
        ),
        endY = size.width.times(0.3f)
    )
    val linearGradient = Brush.linearGradient(
        colors = listOf(
            gradientColor,
            Color.Transparent
        ),
        start = Offset(
            size.width.times(0.2f),
            size.height.times(0.5f)
        ),
        end = Offset(
            size.width.times(0.9f),
            0f
        )
    )

    onDrawWithContent {
        drawContent()
        drawRect(horizontalGradient)
        drawRect(verticalGradient)
        drawRect(linearGradient)
    }
}
