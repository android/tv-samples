package com.google.jetfit.presentation.screens.player.video.composable

import androidx.annotation.IntRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce

/**
 * @param hideSeconds How many seconds should the controls be visible before being hidden.
 * */
class VideoPlayerState internal constructor(
    @IntRange(from = 0L)
    private val hideSeconds: Int
) {
    private val channel = Channel<Int>(Channel.CONFLATED)

    private var _controlsVisibility by mutableStateOf(false)
    val controlsVisibility: Boolean get() = _controlsVisibility

    fun showControls(seconds: Int = hideSeconds) {
        _controlsVisibility = true
        channel.trySend(seconds)
    }

    @OptIn(FlowPreview::class)
    suspend fun observe() {
        channel.consumeAsFlow()
            .debounce { it.toLong() * 1000 }
            .collect {
                _controlsVisibility = false
            }
    }
}


@Composable
fun rememberVideoPlayerState(@IntRange(from = 0L) hideSeconds: Int = 4): VideoPlayerState =
    remember { VideoPlayerState(hideSeconds = hideSeconds) }
        .also {
            LaunchedEffect(it) {
                it.observe()
            }
        }