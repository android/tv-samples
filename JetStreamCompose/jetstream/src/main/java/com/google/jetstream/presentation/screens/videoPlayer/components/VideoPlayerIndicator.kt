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

package com.google.jetstream.presentation.screens.videoPlayer.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import com.google.jetstream.presentation.utils.handleDPadKeyEvents

@OptIn(ExperimentalComposeUiApi::class, ExperimentalTvMaterial3Api::class)
@Composable
fun RowScope.VideoPlayerControllerIndicator(
    progress: Float,
    state: VideoPlayerState,
    onSeek: (seekProgress: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var hasSought by remember { mutableStateOf(false) }
    val color = MaterialTheme.colorScheme.onSurface
    var seekProgress by remember { mutableFloatStateOf(0f) }
    var seekBarWidth by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(hasSought) {
        if (hasSought) {
            state.showControls(seconds = Int.MAX_VALUE)
        } else {
            state.showControls()
        }
    }

    val handleSeekEventModifier = modifier.handleDPadKeyEvents(
        onEnter = {
            onSeek(seekProgress / seekBarWidth)
            hasSought = false
        },
        onLeft = {
            seekProgress = (seekProgress - 10f).coerceAtLeast(0f)
            hasSought = true
        },
        onRight = {
            seekProgress = (seekProgress + 10f).coerceAtMost(seekBarWidth)
            hasSought = true
        }
    )

    Canvas(
        modifier = handleSeekEventModifier
            .weight(1f)
            .height(4.5.dp)
            .padding(horizontal = 4.dp)
            .focusable(),
        onDraw = {
            if (!hasSought) {
                seekProgress = size.width.times(progress)
                seekBarWidth = size.width
            }
            val yOffset = size.height.div(2)
            drawLine(
                color = color.copy(alpha = 0.24f),
                start = Offset(x = 0f, y = yOffset),
                end = Offset(x = size.width, y = yOffset),
                strokeWidth = size.height,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.Red,
                start = Offset(x = 0f, y = yOffset),
                end = Offset(
                    x = size.width.times(progress),
                    y = yOffset
                ),
                strokeWidth = size.height,
                cap = StrokeCap.Round
            )
            drawCircle(
                color = Color.White,
                radius = size.height.times(1.5f),
                center = Offset(x = seekProgress, y = yOffset)
            )
        }
    )
}
