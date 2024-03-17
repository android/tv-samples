package com.google.jetfit.presentation.screens.player.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.google.jetfit.presentation.theme.JetFitTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PlayerTitle(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    descriptionModifier: Modifier = Modifier,
    titleStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    descriptionTextStyle: TextStyle = MaterialTheme.typography.labelLarge,
) {
    Column(modifier) {
        Text(
            title,
            style = titleStyle,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            modifier = descriptionModifier,
            text = description,
            style = descriptionTextStyle,
            color = MaterialTheme.colorScheme.onSurface.copy(0.60f),
            textAlign = TextAlign.Center
        )
    }
}


@Preview(device = Devices.TV_1080p)
@Composable
fun PreviewPlayerTitle() {
    JetFitTheme {
        PlayerTitle(title = "Battle ropes HIIT", description = "Hugo Wright")
    }
}