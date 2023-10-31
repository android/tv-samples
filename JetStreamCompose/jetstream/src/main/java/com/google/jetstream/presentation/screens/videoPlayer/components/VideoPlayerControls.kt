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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.util.StringConstants

@Composable
fun VideoPlayerControls(
    modifier: Modifier = Modifier,
    state: VideoPlayerState = rememberVideoPlayerState(),
    isPlaying: Boolean,
    onPlayPauseToggle: (Boolean) -> Unit,
    onSeek: (seekProgress: Float) -> Unit,
    contentProgressInMillis: Long,
    contentDurationInMillis: Long
) {
    val focusRequester = remember { FocusRequester() }

    val contentProgress by remember(contentProgressInMillis, contentDurationInMillis) {
        derivedStateOf {
            contentProgressInMillis.toFloat() / contentDurationInMillis
        }
    }

    val contentProgressString by remember(contentProgressInMillis) {
        derivedStateOf {
            val contentProgressMinutes = (contentProgressInMillis / 1000) / 60
            val contentProgressSeconds = (contentProgressInMillis / 1000) % 60
            val contentProgressMinutesStr =
                if (contentProgressMinutes < 10) {
                    contentProgressMinutes.padStartWith0()
                } else {
                    contentProgressMinutes.toString()
                }
            val contentProgressSecondsStr =
                if (contentProgressSeconds < 10) {
                    contentProgressSeconds.padStartWith0()
                } else {
                    contentProgressSeconds.toString()
                }
            "$contentProgressMinutesStr:$contentProgressSecondsStr"
        }
    }

    val contentDurationString by remember(contentDurationInMillis) {
        derivedStateOf {
            val contentDurationMinutes =
                (contentDurationInMillis / 1000 / 60).coerceAtLeast(minimumValue = 0)
            val contentDurationSeconds =
                (contentDurationInMillis / 1000 % 60).coerceAtLeast(minimumValue = 0)
            val contentDurationMinutesStr =
                if (contentDurationMinutes < 10) {
                    contentDurationMinutes.padStartWith0()
                } else {
                    contentDurationMinutes.toString()
                }
            val contentDurationSecondsStr =
                if (contentDurationSeconds < 10) {
                    contentDurationSeconds.padStartWith0()
                } else {
                    contentDurationSeconds.toString()
                }
            "$contentDurationMinutesStr:$contentDurationSecondsStr"
        }
    }

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
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        )
                    )
                )
                .padding(
                    horizontal = 56.dp,
                    vertical = 32.dp
                )
        ) {
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VideoPlayerControlsIcon(
                    icon = Icons.Default.AutoAwesomeMotion,
                    state = state,
                    isPlaying = isPlaying,
                    contentDescription = StringConstants
                        .Composable
                        .VideoPlayerControlPlaylistButton
                )
                VideoPlayerControlsIcon(
                    modifier = Modifier.padding(start = 12.dp),
                    icon = Icons.Default.ClosedCaption,
                    state = state,
                    isPlaying = isPlaying,
                    contentDescription = StringConstants
                        .Composable
                        .VideoPlayerControlClosedCaptionsButton
                )
                VideoPlayerControlsIcon(
                    modifier = Modifier.padding(start = 12.dp),
                    icon = Icons.Default.Settings,
                    state = state,
                    isPlaying = isPlaying,
                    contentDescription = StringConstants
                        .Composable
                        .VideoPlayerControlSettingsButton
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                VideoPlayerControlsIcon(
                    modifier = Modifier.focusRequester(focusRequester),
                    icon = if (!isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause,
                    onClick = { onPlayPauseToggle(!isPlaying) },
                    state = state,
                    isPlaying = isPlaying,
                    contentDescription = StringConstants
                        .Composable
                        .VideoPlayerControlPlayPauseButton
                )
                VideoPlayerControllerText(text = contentProgressString)
                VideoPlayerControllerIndicator(
                    progress = contentProgress,
                    onSeek = onSeek,
                    state = state
                )
                VideoPlayerControllerText(text = contentDurationString)
            }
        }
    }
}

private fun Long.padStartWith0() = this.toString().padStart(2, '0')
