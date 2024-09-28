package com.google.jetfit.presentation.screens.player.audio.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.google.jetfit.presentation.theme.JetFitTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AudioPlayerDurationText(
    textDuration: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)
) {
    Text(
        modifier = modifier,
        text = textDuration,
        color = color.copy(alpha = 0.60f),
        style = MaterialTheme.typography.labelMedium
    )
}


@Preview(device = Devices.TV_1080p)
@Composable
fun PreviewAudioPlayerControllerText() {
    JetFitTheme {
        AudioPlayerDurationText(textDuration = "3:06")
    }
}