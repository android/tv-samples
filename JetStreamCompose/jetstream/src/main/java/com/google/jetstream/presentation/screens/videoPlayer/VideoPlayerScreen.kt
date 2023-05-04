/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.presentation.screens.videoPlayer

import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerControls
import com.google.jetstream.presentation.screens.videoPlayer.components.rememberVideoPlayerState
import com.google.jetstream.presentation.utils.handleDPadKeyEvents
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoPlayerScreen(
    mediaUri: Uri = Uri.parse(StringConstants.Composable.SampleVideoUrl),
    onBackPressed: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var contentCurrentPosition: Long by remember { mutableStateOf(0L) }
    val videoPlayerState = rememberVideoPlayerState(hideSeconds = 4)

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                    context,
                    defaultDataSourceFactory
                )
                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(mediaUri))

                setMediaSource(source)
                prepare()
            }
    }

    BackHandler(onBack = onBackPressed)

    LaunchedEffect(Unit) {
        while (true) {
            delay(300)
            contentCurrentPosition = exoPlayer.currentPosition
        }
    }

    LaunchedEffect(Unit) {
        with(exoPlayer) {
            playWhenReady = true
            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    Box {
        DisposableEffect(
            AndroidView(
                modifier = Modifier
                    .handleDPadKeyEvents(
                        onEnter = {
                            if (!videoPlayerState.isDisplayed) {
                                coroutineScope.launch {
                                    videoPlayerState.showControls()
                                }
                            }
                        }
                    )
                    .focusable(),
                factory = {
                    PlayerView(context).apply {
                        hideController()
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

                        player = exoPlayer
                        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    }
                }
            )
        ) {
            onDispose { exoPlayer.release() }
        }
        VideoPlayerControls(
            modifier = Modifier.align(Alignment.BottomCenter),
            isPlaying = exoPlayer.isPlaying,
            onPlayPauseToggle = { shouldPlay ->
                if (shouldPlay) {
                    exoPlayer.play()
                } else {
                    exoPlayer.pause()
                }
            },
            contentProgressInMillis = contentCurrentPosition,
            contentDurationInMillis = exoPlayer.duration,
            state = videoPlayerState,
            onSeek = { seekProgress ->
                exoPlayer.seekTo(exoPlayer.duration.times(seekProgress).toLong())
            }
        )
    }
}
