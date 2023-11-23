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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerControllerIndicator
import com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerControllerText
import com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerControlsIcon
import com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerMainFrame
import com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerMediaTitle
import com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerOverlay
import com.google.jetstream.presentation.screens.videoPlayer.components.rememberVideoPlayerState
import com.google.jetstream.presentation.utils.handleDPadKeyEvents
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object VideoPlayerScreen {
    const val MovieIdBundleKey = "movieId"
}

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoPlayerScreen(
    mediaUri: Uri = Uri.parse(StringConstants.Composable.SampleVideoUrl),
    onBackPressed: () -> Unit,
    videoPlayerScreenViewModel: VideoPlayerScreenViewModel = hiltViewModel()
) {
    val uiState by videoPlayerScreenViewModel.uiState.collectAsStateWithLifecycle()

    // TODO: Handle Loading & Error states
    when (val s = uiState) {

        is VideoPlayerScreenUiState.Loading -> {}
        is VideoPlayerScreenUiState.Error -> {}
        is VideoPlayerScreenUiState.Done -> {
            val movieDetails = s.movieDetails
            // TODO: Move more logic into the view model
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

                val focusRequester = remember { FocusRequester() }
                VideoPlayerOverlay(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    focusRequester = focusRequester,
                    state = videoPlayerState,
                    isPlaying = exoPlayer.isPlaying
                ) {
                    val onPlayPauseToggle = { shouldPlay: Boolean ->
                        if (shouldPlay) {
                            exoPlayer.play()
                        } else {
                            exoPlayer.pause()
                        }
                    }
                    val contentProgressInMillis = contentCurrentPosition
                    val contentDurationInMillis = exoPlayer.duration
                    val onSeek = { seekProgress: Float ->
                        exoPlayer.seekTo(exoPlayer.duration.times(seekProgress).toLong())
                    }
                    val isPlaying = exoPlayer.isPlaying
                    val state = videoPlayerState

                    val contentProgress by remember(
                        contentProgressInMillis,
                        contentDurationInMillis
                    ) {
                        derivedStateOf {
                            contentProgressInMillis.toFloat() / contentDurationInMillis
                        }
                    }

                    val contentProgressString by remember(contentProgressInMillis) {
                        derivedStateOf {
                            val contentProgressMinutes = (contentProgressInMillis / 1000) / 60
                            val contentProgressSeconds = (contentProgressInMillis / 1000) % 60
                            val contentProgressMinutesStr =
                                if (contentProgressMinutes < 10) {
                                    contentProgressMinutes.padStartWith0()
                                } else {
                                    contentProgressMinutes.toString()
                                }
                            val contentProgressSecondsStr =
                                if (contentProgressSeconds < 10) {
                                    contentProgressSeconds.padStartWith0()
                                } else {
                                    contentProgressSeconds.toString()
                                }
                            "$contentProgressMinutesStr:$contentProgressSecondsStr"
                        }
                    }

                    val contentDurationString by remember(contentDurationInMillis) {
                        derivedStateOf {
                            val contentDurationMinutes =
                                (contentDurationInMillis / 1000 / 60).coerceAtLeast(minimumValue = 0)
                            val contentDurationSeconds =
                                (contentDurationInMillis / 1000 % 60).coerceAtLeast(minimumValue = 0)
                            val contentDurationMinutesStr =
                                if (contentDurationMinutes < 10) {
                                    contentDurationMinutes.padStartWith0()
                                } else {
                                    contentDurationMinutes.toString()
                                }
                            val contentDurationSecondsStr =
                                if (contentDurationSeconds < 10) {
                                    contentDurationSeconds.padStartWith0()
                                } else {
                                    contentDurationSeconds.toString()
                                }
                            "$contentDurationMinutesStr:$contentDurationSecondsStr"
                        }
                    }

                    VideoPlayerMainFrame(
                        mediaTitle = {
                            VideoPlayerMediaTitle(
                                title = movieDetails.name,
                                secondaryText = movieDetails.releaseDate,
                                tertiaryText = movieDetails.director,
                                isLive = false,
                                isAd = false
                            )
                        },
                        mediaActions = {
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                VideoPlayerControlsIcon(
                                    icon = Icons.Default.AutoAwesomeMotion,
                                    state = state,
                                    isPlaying = isPlaying,
                                    contentDescription = StringConstants
                                        .Composable
                                        .VideoPlayerControlPlaylistButton
                                )
                                VideoPlayerControlsIcon(
                                    modifier = Modifier.padding(start = 12.dp),
                                    icon = Icons.Default.ClosedCaption,
                                    state = state,
                                    isPlaying = isPlaying,
                                    contentDescription = StringConstants
                                        .Composable
                                        .VideoPlayerControlClosedCaptionsButton
                                )
                                VideoPlayerControlsIcon(
                                    modifier = Modifier.padding(start = 12.dp),
                                    icon = Icons.Default.Settings,
                                    state = state,
                                    isPlaying = isPlaying,
                                    contentDescription = StringConstants
                                        .Composable
                                        .VideoPlayerControlSettingsButton
                                )
                            }
                        },
                        seeker = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                VideoPlayerControlsIcon(
                                    modifier = Modifier.focusRequester(focusRequester),
                                    icon = if (!isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause,
                                    onClick = { onPlayPauseToggle(!isPlaying) },
                                    state = state,
                                    isPlaying = isPlaying,
                                    contentDescription = StringConstants
                                        .Composable
                                        .VideoPlayerControlPlayPauseButton
                                )
                                VideoPlayerControllerText(text = contentProgressString)
                                VideoPlayerControllerIndicator(
                                    progress = contentProgress,
                                    onSeek = onSeek,
                                    state = state
                                )
                                VideoPlayerControllerText(text = contentDurationString)
                            }
                        },
                        more = null
                    )
                }
            }
        }
    }
}

private fun Long.padStartWith0() = this.toString().padStart(2, '0')
