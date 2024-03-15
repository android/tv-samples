package com.google.jetfit.presentation.screens.video_player.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlin.time.Duration

@Composable
fun VideoPlayerSeeker(
    state: VideoPlayerState,
    onSeek: (Float) -> Unit,
    contentProgress: Duration,
    contentDuration: Duration,
    modifier: Modifier = Modifier,
) {
    val textProgress = contentProgress.toComponents { h, m, s, _ ->
        if (h > 0) {
            "$h:${m}:${s}"
        } else {
            ":${m}:${s}"
        }

    }
    val textDuration = contentDuration.toComponents { h, m, s, _ ->
        if (h > 0) {
            "$h:${m}:${s}"
        } else {
            ":${m}:${s}"
        }
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VideoPlayerControllerIndicator(
            progress = (contentProgress / contentDuration).toFloat(),
            onSeek = onSeek,
            state = state
        )
        VideoPlayerDurationText(textProgress = textProgress, textDuration = textDuration)
    }
}