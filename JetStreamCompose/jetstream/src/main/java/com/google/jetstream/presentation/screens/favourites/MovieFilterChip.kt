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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.FilterChip
import androidx.tv.material3.FilterChipDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ProvideTextStyle
import androidx.tv.material3.Text
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.theme.JetStreamCardShape

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MovieFilterChip(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFocused by remember { mutableStateOf(false) }
    FilterChip(
        modifier = modifier
            .padding(end = 16.dp)
            .onFocusChanged {
                isFocused = it.isFocused || it.hasFocus
            }
            // TODO: Adding a temporary fix for b/279022324
            .then(
                if (isFocused)
                    Modifier.border(
                        ChipFocusedBorder.border.width,
                        ChipFocusedBorder.border.brush,
                        ChipFocusedBorder.shape
                    )
                else
                    Modifier
            ),
        onClick = { onCheckedChange(!isChecked) },
        selected = isChecked,
        leadingIcon = {
            AnimatedVisibility(visible = isChecked) {
                Icon(
                    Icons.Default.Check,
                    contentDescription =
                    StringConstants.Composable.ContentDescription.FilterSelected,
                    modifier = Modifier.size(16.dp)
                )
            }
        },
        shape = FilterChipDefaults.shape(shape = JetStreamCardShape),
        border = FilterChipDefaults.border(
            border = Border(
                border = BorderStroke(
                    width = 1.dp, color = MaterialTheme.colorScheme.border.copy(alpha = 0.5f)
                ),
                shape = JetStreamCardShape
            ),
            focusedBorder = ChipFocusedBorder,
        ),
        scale = FilterChipDefaults.scale(focusedScale = 1f),
        colors = FilterChipDefaults.colors(
            focusedContainerColor = ChipColor,
            selectedContainerColor = ChipColor,
            focusedSelectedContainerColor = ChipColor,
            focusedContentColor = ChipContentColor,
            focusedSelectedContentColor = ChipContentColor
        )
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.labelMedium) {
            Text(text = label)
        }
    }
}

private val ChipColor @Composable get() = Color.White.copy(alpha = 0.1f)

@OptIn(ExperimentalTvMaterial3Api::class)
private val ChipContentColor @Composable get() = MaterialTheme.colorScheme.inverseSurface

@OptIn(ExperimentalTvMaterial3Api::class)
private val ChipFocusedBorder
    @Composable get() = Border(
        border = BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.onSurface,
        ),
        shape = JetStreamCardShape
    )
