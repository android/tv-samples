package com.google.jetfit.presentation.screens.player.video.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.jetfit.presentation.theme.JetFitTheme
import com.google.jetfit.presentation.utils.padStartWith0
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

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
            "$h:${m.padStartWith0()}:${s.padStartWith0()}"
        } else {
            "${m.padStartWith0()}:${s.padStartWith0()}"
        }

    }
    val textDuration = contentDuration.toComponents { h, m, s, _ ->
        if (h > 0) {
            "$h:${m.padStartWith0()}:${s.padStartWith0()}"
        } else {
            "${m.padStartWith0()}:${s.padStartWith0()}"
        }
    }
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        VideoPlayerControllerIndicator(
            progress = (contentProgress / contentDuration).toFloat(), onSeek = onSeek, state = state
        )
        VideoPlayerDurationText(
            modifier = Modifier.padding(
                horizontal = 12.dp
            ), textProgress = textProgress, textDuration = textDuration
        )
    }
}


@Preview
@Composable
fun PreviewVideoPlayerSeeker() {
    JetFitTheme {
        VideoPlayerSeeker(
            state = rememberVideoPlayerState(),
            onSeek = {},
            contentProgress = (5L).milliseconds,
            contentDuration = (50L).milliseconds
        )
    }
}