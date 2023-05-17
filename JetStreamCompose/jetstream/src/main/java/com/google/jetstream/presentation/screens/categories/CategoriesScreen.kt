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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.itemsIndexed
import androidx.tv.foundation.lazy.grid.rememberTvLazyGridState
import androidx.tv.material3.Border
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardLayoutDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.StandardCardLayout
import androidx.tv.material3.Text
import com.google.jetstream.presentation.LocalMovieRepository
import com.google.jetstream.presentation.screens.dashboard.rememberChildPadding
import com.google.jetstream.presentation.theme.JetStreamBorderWidth
import com.google.jetstream.presentation.theme.JetStreamCardShape
import com.google.jetstream.presentation.utils.GradientBg

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CategoriesScreen(
    gridColumns: Int = 4,
    onCategoryClick: (categoryId: String) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit
) {
    val childPadding = rememberChildPadding()
    val tvLazyGridState = rememberTvLazyGridState()
    val shouldShowTopBar by remember {
        derivedStateOf {
            tvLazyGridState.firstVisibleItemIndex == 0 &&
                    tvLazyGridState.firstVisibleItemScrollOffset < 100
        }
    }
    val movieRepository = LocalMovieRepository.current!!
    val movieCategories = remember {
        movieRepository.getMovieCategories()
    }

    LaunchedEffect(shouldShowTopBar) {
        onScroll(shouldShowTopBar)
    }

    AnimatedContent(
        targetState = movieCategories,
        modifier = Modifier
            .padding(horizontal = childPadding.start)
            .padding(top = childPadding.top),
        label = "",
    ) { it ->
        TvLazyVerticalGrid(
            state = tvLazyGridState,
            modifier = Modifier.fillMaxSize(),
            columns = TvGridCells.Fixed(gridColumns),
            content = {
                itemsIndexed(it) { index, movieCategory ->
                    var isFocused by remember { mutableStateOf(false) }
                    StandardCardLayout(
                        imageCard = {
                            CardLayoutDefaults.ImageCard(
                                shape = CardDefaults.shape(shape = JetStreamCardShape),
                                border = CardDefaults.border(
                                    focusedBorder = Border(
                                        border = BorderStroke(
                                            width = JetStreamBorderWidth,
                                            color = MaterialTheme.colorScheme.onSurface
                                        ),
                                        shape = JetStreamCardShape
                                    ),
                                    pressedBorder = Border(
                                        border = BorderStroke(
                                            width = JetStreamBorderWidth,
                                            color = MaterialTheme.colorScheme.border
                                        ),
                                        shape = JetStreamCardShape
                                    )
                                ),
                                scale = CardDefaults.scale(focusedScale = 1f),
                                onClick = { onCategoryClick(movieCategory.id) },
                                interactionSource = it
                            ) {
                                val itemAlpha by animateFloatAsState(
                                    targetValue = if (isFocused) .6f else 0.2f,
                                    label = ""
                                )
                                val textColor = if (isFocused) Color.White else Color.White

                                Box(contentAlignment = Alignment.Center) {
                                    Box(modifier = Modifier.alpha(itemAlpha)) {
                                        GradientBg()
                                    }
                                    Text(
                                        text = movieCategory.name,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = textColor,
                                        )
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .aspectRatio(16 / 9f)
                            .onFocusChanged {
                                isFocused = it.isFocused || it.hasFocus
                            }
                            .focusProperties {
                                if (index % gridColumns == 0) {
                                    left = FocusRequester.Cancel
                                }
                            },
                        title = {}
                    )
                }
            }
        )
    }
}
