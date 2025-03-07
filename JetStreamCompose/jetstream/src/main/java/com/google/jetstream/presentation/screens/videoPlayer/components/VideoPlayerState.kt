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

import androidx.annotation.IntRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.state.NextButtonState
import androidx.media3.ui.compose.state.PlayPauseButtonState
import androidx.media3.ui.compose.state.PreviousButtonState
import androidx.media3.ui.compose.state.RepeatButtonState
import androidx.media3.ui.compose.state.rememberNextButtonState
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.media3.ui.compose.state.rememberPreviousButtonState
import androidx.media3.ui.compose.state.rememberRepeatButtonState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce

@androidx.annotation.OptIn(UnstableApi::class)
class VideoPlayerState(
    @IntRange(from = 0)
    private val hideSeconds: Int,
    private val playPauseButtonState: PlayPauseButtonState,
    private val previousButtonState: PreviousButtonState,
    private val nextButtonState: NextButtonState,
    private val repeatButtonState: RepeatButtonState,
) {
    var isControlsVisible by mutableStateOf(true)
        private set

    val isPlaying
        get() = !playPauseButtonState.showPlay

    val hasNextMovie
        get() = nextButtonState.isEnabled

    val hasPreviousMovie
        get() = previousButtonState.isEnabled

    val repeatMode
        get() = repeatButtonState.repeatModeState

    fun togglePlayPause() {
        playPauseButtonState.onClick()
    }

    fun nextMovie() {
        nextButtonState.onClick()
    }

    fun previousMovie() {
        previousButtonState.onClick()
    }

    fun toggleRepeat() {
        repeatButtonState.onClick()
    }

    fun showControls() {
        if (isPlaying) {
            updateControlVisibility()
        } else {
            updateControlVisibility(seconds = Int.MAX_VALUE)
        }
    }

    private fun updateControlVisibility(seconds: Int = hideSeconds) {
        isControlsVisible = true
        channel.trySend(seconds)
    }

    private val channel = Channel<Int>(CONFLATED)

    @OptIn(FlowPreview::class)
    suspend fun observe() {
        channel.consumeAsFlow()
            .debounce { it.toLong() * 1000 }
            .collect { isControlsVisible = false }
    }
}

/**
 * Create and remember a [VideoPlayerState] instance. Useful when trying to control the state of
 * the [VideoPlayerOverlay]-related composable.
 * @return A remembered instance of [VideoPlayerState].
 * @param hideSeconds How many seconds should the controls be visible before being hidden.
 * */
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun rememberVideoPlayerState(
    exoPlayer: ExoPlayer,
    @IntRange(from = 0) hideSeconds: Int = 2
): VideoPlayerState {
    val playPauseButtonState = rememberPlayPauseButtonState(exoPlayer)
    val nextButtonState = rememberNextButtonState(exoPlayer)
    val previousButtonState = rememberPreviousButtonState(exoPlayer)
    val repeatButtonState = rememberRepeatButtonState(exoPlayer)
    return remember(playPauseButtonState) {
        VideoPlayerState(
            hideSeconds = hideSeconds,
            playPauseButtonState = playPauseButtonState,
            nextButtonState = nextButtonState,
            previousButtonState = previousButtonState,
            repeatButtonState = repeatButtonState
        )
    }
        .also { LaunchedEffect(it) { it.observe() } }
}
