package com.google.jetfit.presentation.screens.video_player.composable

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.google.jetfit.components.CustomFillIconButton

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
internal fun VideoPlayerControlsIcon(
    modifier: Modifier = Modifier,
    state: VideoPlayerState,
    isPlaying: Boolean,
    icon: Int,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isPlaying && isFocused) {
        if (isPlaying && isFocused) {
            state.showControls()
        }
    }

    CustomFillIconButton(
        modifier = modifier.size(40.dp),
        onClick = onClick,
        icon = icon,
        buttonColor = Color.Transparent
    )
}