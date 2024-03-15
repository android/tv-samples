package com.google.jetfit.presentation.screens.video_player.composable

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import com.google.jetfit.presentation.theme.JetFitTheme
import com.google.jetfit.presentation.utils.handleDPadKeyEvents

@OptIn(ExperimentalComposeUiApi::class, ExperimentalTvMaterial3Api::class)
@Composable
fun RowScope.VideoPlayerControllerIndicator(
    progress: Float,
    onSeek: (seekProgress: Float) -> Unit,
    state: VideoPlayerState,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isSelected by remember { mutableStateOf(false) }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val color by rememberUpdatedState(
        newValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurface
    )
    val animatedIndicatorHeight by animateDpAsState(
        targetValue = 4.dp.times((if (isFocused) 2.5f else 1f)), label = ""
    )
    var seekProgress by remember { mutableFloatStateOf(0f) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isSelected) {
        if (isSelected) {
            state.showControls(seconds = Int.MAX_VALUE)
        } else {
            state.showControls()
        }
    }

    Canvas(
        modifier = modifier
            .weight(1f)
            .height(animatedIndicatorHeight)
            .padding(horizontal = 4.dp)
            .handleDPadKeyEvents(
                onEnter = {
                    if (isSelected) {
                        onSeek(seekProgress)
                        focusManager.moveFocus(FocusDirection.Exit)
                    } else {
                        seekProgress = progress
                    }
                    isSelected = !isSelected
                },
                onLeft = {
                    if (isSelected) {
                        seekProgress = (seekProgress - 0.1f).coerceAtLeast(0f)
                    } else {
                        focusManager.moveFocus(FocusDirection.Left)
                    }
                },
                onRight = {
                    if (isSelected) {
                        seekProgress = (seekProgress + 0.1f).coerceAtMost(1f)
                    } else {
                        focusManager.moveFocus(FocusDirection.Right)
                    }
                }
            )
            .focusable(interactionSource = interactionSource),
        onDraw = {
            val yOffset = size.height.div(2)
            drawLine(
                color = color.copy(alpha = 0.24f),
                start = Offset(x = 0f, y = yOffset),
                end = Offset(x = size.width, y = yOffset),
                strokeWidth = size.height,
                cap = StrokeCap.Round
            )
            drawLine(
                color = color,
                start = Offset(x = 0f, y = yOffset),
                end = Offset(
                    x = size.width.times(if (isSelected) seekProgress else progress),
                    y = yOffset
                ),
                strokeWidth = size.height,
                cap = StrokeCap.Round
            )
        }
    )
}


@Preview(device = Devices.TV_1080p)
@Composable
fun PreviewVideoPlayerControllerIndicator() {
    JetFitTheme {
        Row {
            VideoPlayerControllerIndicator(
                progress = 0.1f,
                onSeek = {},
                state = rememberVideoPlayerState()
            )
        }
    }
}