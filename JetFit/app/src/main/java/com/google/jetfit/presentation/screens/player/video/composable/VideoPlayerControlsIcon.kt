package com.google.jetfit.presentation.screens.player.video.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.IconButtonDefaults
import androidx.tv.material3.MaterialTheme
import com.google.jetfit.R
import com.google.jetfit.presentation.screens.player.composable.PlayerControlsIcon
import com.google.jetfit.presentation.theme.JetFitTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
internal fun VideoPlayerControlsIcon(
    state: VideoPlayerState,
    isPlaying: Boolean,
    icon: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isPlaying && isFocused) {
        if (isPlaying && isFocused) {
            state.showControls()
        }
    }

    PlayerControlsIcon(
        modifier = modifier.size(40.dp),
        icon = icon,
        border = IconButtonDefaults.border(
            border = Border(
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.border),
                shape = CircleShape
            )
        ),
        buttonColor = Color.Transparent,
        onClick = onClick
    )
}


@Preview(device = Devices.TV_1080p)
@Composable
fun PreviewVideoPlayerControlsIcon() {
    JetFitTheme {
        val state = rememberVideoPlayerState()
        VideoPlayerControlsIcon(state = state, isPlaying = true, icon = R.drawable.subtitles) {
        }
    }
}