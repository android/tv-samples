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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyRow
import com.google.jetstream.presentation.utils.FocusGroup

@Suppress("EnumEntryName")
enum class ChipListItems {
    Movies, TV_Shows, Added_Last_Week, Available_in_4K; // ktlint-disable enum-entry-name-case

    operator fun invoke() = this.name.replace("_", " ")
}

@Composable
fun MovieFilterChipRow(
    movieListRange: SnapshotStateList<Int>,
    movieFilterRange: List<Int>,
    tvShowsFilterRange: List<Int>,
    addedLastWeekFilterRange: List<Int>,
    availableIn4KFilterRange: List<Int>,
    modifier: Modifier = Modifier,
) {
    var isMovieFilterSelected by remember { mutableStateOf(false) }
    var isTvShowsFilterSelected by remember { mutableStateOf(false) }
    var isAddedLastWeekFilterSelected by remember { mutableStateOf(false) }
    var isAvailableIn4KFilterSelected by remember { mutableStateOf(false) }

    FocusGroup {
        TvLazyRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentPadding = PaddingValues(start = 2.dp)
        ) {
            ChipListItems.values().forEachIndexed { index, chipItem ->
                item {
                    key(chipItem.ordinal) {
                        val isChecked by remember {
                            derivedStateOf {
                                when (chipItem) {
                                    ChipListItems.Movies -> isMovieFilterSelected
                                    ChipListItems.TV_Shows -> isTvShowsFilterSelected
                                    ChipListItems.Added_Last_Week -> isAddedLastWeekFilterSelected
                                    ChipListItems.Available_in_4K -> isAvailableIn4KFilterSelected
                                }
                            }
                        }
                        MovieFilterChip(
                            label = chipItem(),
                            isChecked = isChecked,
                            modifier = if (index == 0) Modifier.initiallyFocused() else Modifier,
                            onCheckedChange = {
                                when (chipItem) {
                                    ChipListItems.Movies -> {
                                        isMovieFilterSelected = !isMovieFilterSelected
                                    }

                                    ChipListItems.TV_Shows -> {
                                        isTvShowsFilterSelected = !isTvShowsFilterSelected
                                    }

                                    ChipListItems.Added_Last_Week -> {
                                        isAddedLastWeekFilterSelected = !isAddedLastWeekFilterSelected
                                    }

                                    ChipListItems.Available_in_4K -> {
                                        isAvailableIn4KFilterSelected = !isAvailableIn4KFilterSelected
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(isMovieFilterSelected) {
        if (isMovieFilterSelected) {
            movieListRange.addAll(movieFilterRange)
        } else {
            movieListRange.removeAll(movieFilterRange)
        }
    }

    LaunchedEffect(isTvShowsFilterSelected) {
        if (isTvShowsFilterSelected) {
            movieListRange.addAll(tvShowsFilterRange)
        } else {
            movieListRange.removeAll(tvShowsFilterRange)
        }
    }

    LaunchedEffect(isAddedLastWeekFilterSelected) {
        if (isAddedLastWeekFilterSelected) {
            movieListRange.addAll(addedLastWeekFilterRange)
        } else {
            movieListRange.removeAll(addedLastWeekFilterRange)
        }
    }

    LaunchedEffect(isAvailableIn4KFilterSelected) {
        if (isAvailableIn4KFilterSelected) {
            movieListRange.addAll(availableIn4KFilterRange)
        } else {
            movieListRange.removeAll(availableIn4KFilterRange)
        }
    }
}
