package com.google.jetstream.presentation.screens.videoPlayer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

object VideoPlayerPulse {
    enum class Type { FORWARD, BACK, NONE }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun VideoPlayerPulse(
    state: VideoPlayerPulseState = remember { VideoPlayerPulseState() }
) {
    val icon = when(state.type) {
        VideoPlayerPulse.Type.FORWARD ->  Icons.Default.Forward10
        VideoPlayerPulse.Type.BACK -> Icons.Default.Replay10
        VideoPlayerPulse.Type.NONE -> null
    }
    if(icon != null) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                .size(88.dp)
                .wrapContentSize()
                .size(48.dp)
        )
    }
}

class VideoPlayerPulseState {
    private var _type = mutableStateOf(VideoPlayerPulse.Type.NONE)
    val type: VideoPlayerPulse.Type get() = _type.value

    private var currentTimer: Job? = null

    suspend fun setType(type: VideoPlayerPulse.Type) = coroutineScope {
        // Cancel previous delay
        currentTimer?.cancel()
        currentTimer = launch {
            _type.value = type
            delay(2.seconds)
            _type.value = VideoPlayerPulse.Type.NONE
        }
    }
}