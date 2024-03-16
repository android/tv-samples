package com.google.jetfit.presentation.screens.player.video.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.jetfit.presentation.theme.JetFitTheme

@Composable
fun VideoPlayerFrame(
    videoSeeker: @Composable () -> Unit,
    videoTitle: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    videoActions: @Composable () -> Unit = {},
) {
    Column(modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(Modifier.weight(1f)) { videoTitle() }
            videoActions()
        }
        videoSeeker()
    }
}


@Preview(device = Devices.TV_1080p)
@Composable
fun PreviewVideoPlayerFrame() {
    JetFitTheme {
        VideoPlayerFrame(
            videoTitle = {
                Box(
                    Modifier
                        .border(2.dp, Color.Red)
                        .background(Color.LightGray)
                        .fillMaxWidth()
                        .height(64.dp)
                )
            },
            videoActions = {
                Box(
                    Modifier
                        .border(2.dp, Color.Red)
                        .background(Color.LightGray)
                        .size(196.dp, 40.dp)
                )
            },
            videoSeeker = {
                Box(
                    Modifier
                        .border(2.dp, Color.Red)
                        .background(Color.LightGray)
                        .fillMaxWidth()
                        .height(16.dp)
                )
            }
        )
    }
}