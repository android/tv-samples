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

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvGridItemSpan
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.material3.Border
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardLayoutDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.StandardCardLayout
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.LocalMovieRepository
import com.google.jetstream.presentation.screens.Screens
import com.google.jetstream.presentation.screens.dashboard.rememberChildPadding
import com.google.jetstream.presentation.theme.JetStreamBorderWidth
import com.google.jetstream.presentation.theme.JetStreamBottomListPadding
import com.google.jetstream.presentation.theme.JetStreamCardShape
import com.google.jetstream.presentation.theme.JetStreamTheme

object CategoryMovieListScreen {
    const val CategoryIdBundleKey = "categoryId"
}

@Composable
fun CategoryMovieListScreen(
    categoryId: String,
    parentNavController: NavController,
    onBackPressed: () -> Unit,
) {
    val childPadding = rememberChildPadding()

    val movieRepository = LocalMovieRepository.current!!
    val categoryDetails =
        remember { movieRepository.getMovieCategoryDetails(categoryId = categoryId) }

    BackHandler(onBack = onBackPressed)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = categoryDetails.name,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(
                vertical = childPadding.top.times(3.5f)
            )
        )
        TvLazyVerticalGrid(columns = TvGridCells.Fixed(6), content = {
            categoryDetails.movies.forEach { movie ->
                item {
                    key(movie.id) {
                        StandardCardLayout(
                            modifier = Modifier
                                .aspectRatio(1 / 1.5f)
                                .padding(8.dp),
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
                                        ),
                                    ),
                                    scale = CardDefaults.scale(focusedScale = 1f),
                                    onClick = {
                                        parentNavController.navigate(
                                            Screens.MovieDetails.withArgs(movie.id)
                                        )
                                    },
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
                            title = {},
                        )
                    }
                }
            }
            item(span = { TvGridItemSpan(currentLineSpan = 6) }) {
                Spacer(modifier = Modifier.padding(bottom = JetStreamBottomListPadding))
            }
        })
    }
}

@Preview
@Composable
private fun CategoryMovieListScreenPreview() {
    JetStreamTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            CategoryMovieListScreen(
                categoryId = "1",
                parentNavController = rememberNavController(),
                onBackPressed = {}
            )
        }
    }
}
