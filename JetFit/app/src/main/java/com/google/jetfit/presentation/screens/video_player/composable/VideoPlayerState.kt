package com.google.jetfit.presentation.screens.video_player.composable

import androidx.annotation.IntRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce

/**
 * @param hideSeconds How many seconds should the controls be visible before being hidden.
 * */
class VideoPlayerState internal constructor(
    @IntRange(from = 0L)
    private val hideSeconds: Int
) {
    private val flow = MutableSharedFlow<Int>(onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private var _visibility by mutableStateOf(false)
    val visibility get() = _visibility

    fun showControls(seconds: Int = hideSeconds) {
        _visibility = true
        flow.tryEmit(seconds)
    }

    @OptIn(FlowPreview::class)
    suspend fun collect() {
        flow.debounce { it.toLong() * 1000 }
            .collect {
                _visibility = false
            }
    }
}


@Composable
fun rememberVideoPlayerState(@IntRange(from = 0L) hideSeconds: Int = 2) =
    remember { VideoPlayerState(hideSeconds = hideSeconds) }
        .also { LaunchedEffect(it) { it.collect() } }