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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.jetstream.presentation.utils.createInitialFocusRestorerModifiers

@Composable
fun MovieFilterChipRow(
    filterList: FilterList,
    selectedFilterList: FilterList,
    onSelectedFilterListUpdated: (FilterList) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRestorerModifiers = createInitialFocusRestorerModifiers()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .then(focusRestorerModifiers.parentModifier),
    ) {
        filterList.items.forEachIndexed { index, filterCondition ->
            val isChecked = selectedFilterList.items.contains(filterCondition)
            MovieFilterChip(
                label = stringResource(id = filterCondition.labelId),
                isChecked = isChecked,
                onCheckedChange = {
                    val updated = if (it) {
                        selectedFilterList.items + listOf(filterCondition)
                    } else {
                        selectedFilterList.items - setOf(filterCondition)
                    }
                    onSelectedFilterListUpdated(FilterList(updated))
                },
                modifier = if (index == 0) {
                    focusRestorerModifiers.childModifier
                } else {
                    Modifier
                }
            )
        }
    }
}
