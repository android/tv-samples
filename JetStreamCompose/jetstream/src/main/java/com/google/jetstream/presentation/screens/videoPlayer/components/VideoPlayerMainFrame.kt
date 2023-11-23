package com.google.jetstream.presentation.screens.videoPlayer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun VideoPlayerMainFrame(
    mediaTitle: @Composable () -> Unit = {},
    mediaActions: @Composable () -> Unit = {},
    seeker: @Composable () -> Unit = {},
    more: (@Composable () -> Unit)? = null
) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Box(Modifier.weight(1f)) { mediaTitle() }
            mediaActions()
        }
        Spacer(Modifier.height(16.dp))
        seeker()
        if (more != null) {
            Spacer(Modifier.height(12.dp))
            Box(Modifier.align(Alignment.CenterHorizontally)) {
                more()
            }
        }
    }
}


@Preview(device = "id:tv_4k")
@Composable
private fun MediaPlayerMainFramePreviewLayout() {
    VideoPlayerMainFrame(
        mediaTitle = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .height(64.dp)
            )
        },
        mediaActions = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .size(196.dp, 40.dp)
            )
        },
        seeker = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .height(16.dp)
            )
        },
        more = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .size(145.dp, 16.dp)
            )
        },
    )
}

@Preview(device = "id:tv_4k")
@Composable
private fun MediaPlayerMainFramePreviewLayoutWithoutMore() {
    VideoPlayerMainFrame(
        mediaTitle = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .height(64.dp)
            )
        },
        mediaActions = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .size(196.dp, 40.dp)
            )
        },
        seeker = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .height(16.dp)
            )
        },
        more = null,
    )
}