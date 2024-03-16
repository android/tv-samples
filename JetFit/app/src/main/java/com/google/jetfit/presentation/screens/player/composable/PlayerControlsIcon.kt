package com.google.jetfit.presentation.screens.player.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.ButtonBorder
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.IconButtonDefaults
import androidx.tv.material3.MaterialTheme
import com.google.jetfit.R
import com.google.jetfit.components.CustomFillIconButton
import com.google.jetfit.presentation.theme.JetFitTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PlayerControlsIcon(
    icon: Int,
    border: ButtonBorder,
    buttonColor: Color,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    onClick: () -> Unit
) {
    CustomFillIconButton(
        modifier = modifier.size(size),
        onClick = onClick,
        icon = icon,
        buttonColor = buttonColor,
        border = border
    )
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(device = Devices.TV_1080p)
@Composable
fun PreviewPlayerControlsIcon() {
    JetFitTheme {
        PlayerControlsIcon(
            modifier = Modifier.size(40.dp),
            icon = R.drawable.play_icon,
            border = IconButtonDefaults.border(
                border = Border(
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.border),
                    shape = CircleShape
                )
            ),
            buttonColor = Color.Transparent
        ) {}
    }
}