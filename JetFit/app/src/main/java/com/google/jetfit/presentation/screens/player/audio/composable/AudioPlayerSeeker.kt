package com.google.jetfit.presentation.screens.player.audio.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.jetfit.presentation.utils.padStartWith0
import kotlin.time.Duration

@Composable
fun AudioPlayerSeeker(
    onSeek: (Float) -> Unit,
    contentProgress: Duration,
    contentDuration: Duration,
    modifier: Modifier = Modifier,
) {
    val textProgress = contentProgress.toComponents { h, m, s, _ ->
        if (h > 0) {
            "$h:${m}:${s.padStartWith0()}"
        } else {
            "${m}:${s.padStartWith0()}"
        }

    }
    val textDuration = contentDuration.toComponents { h, m, s, _ ->
        if (h > 0) {
            "$h:${m}:${s.padStartWith0()}"
        } else {
            "${m}:${s.padStartWith0()}"
        }
    }

    Column(modifier = modifier) {
        AudioPlayerControllerIndicator(
            progress = (contentProgress / contentDuration).toFloat(),
            onSeek = onSeek
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AudioPlayerDurationText(textDuration = textProgress)
            AudioPlayerDurationText(textDuration = textDuration)
        }
    }
}