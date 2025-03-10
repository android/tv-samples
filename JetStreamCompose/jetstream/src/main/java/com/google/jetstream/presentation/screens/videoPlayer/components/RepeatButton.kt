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

import androidx.annotation.OptIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.RepeatButtonState
import androidx.media3.ui.compose.state.rememberRepeatButtonState
import androidx.tv.material3.LocalContentColor
import com.google.jetstream.data.util.StringConstants

@OptIn(UnstableApi::class)
@Composable
fun RepeatButton(
    player: Player,
    modifier: Modifier = Modifier,
    state: RepeatButtonState = rememberRepeatButtonState(player),
    contentDescription: String? = StringConstants.Composable.VideoPlayerControlRepeatButton,
    onShowControls: () -> Unit,
) {
    val repeatMode = state.repeatModeState
    val isRepeating = repeatMode != Player.REPEAT_MODE_OFF
    val color = LocalContentColor.current

    VideoPlayerControlsIcon(
        icon = when (repeatMode) {
            Player.REPEAT_MODE_ONE -> Icons.Default.RepeatOne
            else -> Icons.Default.Repeat
        },
        isPlaying = player.isPlaying,
        contentDescription = contentDescription,
        onShowControls = onShowControls,
        onClick = state::onClick,
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
