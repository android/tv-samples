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
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.PreviousButtonState
import androidx.media3.ui.compose.state.rememberPreviousButtonState
import com.google.jetstream.data.util.StringConstants

@OptIn(UnstableApi::class)
@Composable
fun PreviousButton(
    player: Player,
    modifier: Modifier = Modifier,
    state: PreviousButtonState = rememberPreviousButtonState(player),
    onShowControls: () -> Unit = {},
) {
    VideoPlayerControlsIcon(
        icon = Icons.Default.SkipPrevious,
        isPlaying = player.isPlaying,
        contentDescription =
        StringConstants.Composable.VideoPlayerControlSkipPreviousButton,
        onShowControls = onShowControls,
        onClick = state::onClick,
        modifier = modifier,
    )
}
