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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.jetstream.presentation.theme.JetStreamTheme

/**
 * Handles the visibility and animation of the controls.
 */
@Composable
fun VideoPlayerOverlay(
    modifier: Modifier = Modifier,
    state: VideoPlayerState = rememberVideoPlayerState(),
    focusRequester: FocusRequester = remember { FocusRequester() },
    isPlaying: Boolean,
    content: @Composable () -> Unit 
) {
    LaunchedEffect(state.isDisplayed) {
        if (state.isDisplayed) {
            focusRequester.requestFocus()
        }
    }

    LaunchedEffect(isPlaying) {
        if (!isPlaying) {
            state.showControls(seconds = Int.MAX_VALUE)
        } else {
            state.showControls()
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = state.isDisplayed,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        Column(
            modifier = Modifier
                .focusRequester(focusRequester)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0F),
                            Color.Black
                        )
                    )
                )
                .padding(
                    horizontal = 56.dp,
                    vertical = 32.dp
                )
        ) {
            content()
        }
    }
}

@Preview(device = "id:tv_4k")
@Composable
private fun VideoPlayerOverlayPreview() {
    JetStreamTheme {
        Box(Modifier.fillMaxSize()) {
            VideoPlayerOverlay(
                modifier = Modifier.align(Alignment.BottomCenter),
                isPlaying = true
            ) {
                Box(Modifier.fillMaxWidth().height(100.dp).background(Color.Blue))
            }
        }
    }
}