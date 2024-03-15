package com.google.jetfit.presentation.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun rememberExoPlayer(context: Context) = remember {
    ExoPlayer.Builder(context).setSeekForwardIncrementMs(10).setSeekBackIncrementMs(10)
        .setMediaSourceFactory(
            ProgressiveMediaSource.Factory(DefaultDataSource.Factory(context))
        ).setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING).build().apply {
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
        }
}