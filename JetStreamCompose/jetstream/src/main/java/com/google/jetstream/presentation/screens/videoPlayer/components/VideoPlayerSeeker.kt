package com.google.jetstream.presentation.screens.videoPlayer.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.google.jetstream.data.util.StringConstants

@Composable
fun VideoPlayerSeeker(
    focusRequester: FocusRequester,
    state: VideoPlayerState,
    isPlaying: Boolean,
    onPlayPauseToggle: (Boolean) -> Unit,
    onSeek: (Float) -> Unit,
    contentProgressInMillis: Long,
    contentDurationInMillis: Long
) {
    val contentProgress by remember(
        contentProgressInMillis,
        contentDurationInMillis
    ) {
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

private fun Long.padStartWith0() = this.toString().padStart(2, '0')
