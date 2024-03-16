package com.google.jetfit.presentation.screens.player.audio.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.google.jetfit.presentation.screens.player.composable.PlayerControllerIndicator

@Composable
fun AudioPlayerControllerIndicator(
    progress: Float,
    onSeek: (seekProgress: Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isSelected by remember { mutableStateOf(false) }
    PlayerControllerIndicator(
        progress = progress,
        onSeek = onSeek,
        isSelected = isSelected,
        onSelected = { isSelected = !isSelected },
        modifier = modifier
    )
}


@Preview(device = Devices.TV_1080p)
@Composable
fun PreviewAudioPlayerControllerIndicator() {
    AudioPlayerControllerIndicator(progress = 1f, {})
}