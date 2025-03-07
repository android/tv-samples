/*
 * Copyright 2025 Google LLC
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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.tv.material3.LocalContentColor
import com.google.jetstream.data.util.StringConstants

@Composable
fun RepeatIcon(
    isPlaying: Boolean,
    onShowControls: () -> Unit,
    modifier: Modifier = Modifier,
    repeatMode: Int = Player.REPEAT_MODE_OFF,
    contentDescription: String? = StringConstants.Composable.VideoPlayerControlRepeatButton,
    onClick: () -> Unit = {}
) {
    val isRepeating = repeatMode != Player.REPEAT_MODE_OFF
    val color = LocalContentColor.current

    VideoPlayerControlsIcon(
        icon = when (repeatMode) {
            Player.REPEAT_MODE_ONE -> Icons.Default.RepeatOne
            else -> Icons.Default.Repeat
        },
        isPlaying = isPlaying,
        contentDescription = contentDescription,
        onShowControls = onShowControls,
        onClick = onClick,
        modifier = modifier.drawBehind {
            if (isRepeating) {
                val radius = 2.dp.toPx()
                drawCircle(
                    color = color,
                    radius = radius,
                    center = Offset((size.width - radius) / 2, size.height - radius * 3)
                )
            }
        }
    )
}
