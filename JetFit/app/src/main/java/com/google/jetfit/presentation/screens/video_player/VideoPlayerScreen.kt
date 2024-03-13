package com.google.jetfit.presentation.screens.video_player

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun VideoPlayerScreen(viewModel: VideoPlayerViewModel = hiltViewModel()) {
    VideoPlayerContent()
}

@Composable
private fun VideoPlayerContent() {

}