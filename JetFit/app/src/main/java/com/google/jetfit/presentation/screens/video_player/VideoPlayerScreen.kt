package com.google.jetfit.presentation.screens.video_player

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import com.google.jetfit.R
import com.google.jetfit.components.CustomFillButton
import com.google.jetfit.presentation.screens.video_player.composable.VideoPlayerControlsIcon
import com.google.jetfit.presentation.screens.video_player.composable.VideoPlayerFrame
import com.google.jetfit.presentation.screens.video_player.composable.VideoPlayerOverlay
import com.google.jetfit.presentation.screens.video_player.composable.VideoPlayerSeeker
import com.google.jetfit.presentation.screens.video_player.composable.VideoPlayerState
import com.google.jetfit.presentation.screens.video_player.composable.VideoPlayerTitle
import com.google.jetfit.presentation.screens.video_player.composable.rememberVideoPlayerState
import com.google.jetfit.presentation.theme.JetFitTheme
import com.google.jetfit.presentation.utils.dPadEvents
import com.google.jetfit.presentation.utils.rememberExoPlayer
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun VideoPlayerScreen(
    onBackPressed: () -> Unit,
    viewModel: VideoPlayerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    AnimatedVisibility(visible = !state.isLoading && state.error != null) {
        VideoPlayerContent(state = state, onBackPressed = onBackPressed)
    }
}

@Composable
private fun VideoPlayerContent(
    state: VideoPlayerUiState,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val videoPlayerState = rememberVideoPlayerState(hideSeconds = 4000)
    val exoPlayer = rememberExoPlayer(context)
    LaunchedEffect(exoPlayer, state.workoutUiState) {
        exoPlayer.setMediaItem(
            MediaItem.Builder()
                .setUri(state.workoutUiState.videoUrl)
                .setSubtitleConfigurations(
                    if (state.workoutUiState.subtitles == null) {
                        emptyList()
                    } else {
                        listOf(
                            MediaItem.SubtitleConfiguration
                                .Builder(Uri.parse(state.workoutUiState.subtitleUri))
                                .setMimeType("application/vtt")
                                .setLanguage("en")
                                .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
                                .build()
                        )
                    }
                ).build()
        )
        exoPlayer.prepare()
    }

    var contentCurrentPosition by remember { mutableLongStateOf(0L) }
    var isPlaying: Boolean by remember { mutableStateOf(exoPlayer.isPlaying) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(300)
            contentCurrentPosition = exoPlayer.currentPosition
            isPlaying = exoPlayer.isPlaying
        }
    }

    BackHandler(onBack = onBackPressed)

    Box(
        Modifier
            .dPadEvents(
                exoPlayer,
                videoPlayerState,
            )
            .focusable()
    ) {
        AndroidView(
            factory = {
                PlayerView(context).apply { useController = false }
            },
            update = { it.player = exoPlayer },
            onRelease = { exoPlayer.release() }
        )

        val onPlayPauseToggle = { shouldPlay: Boolean ->
            if (shouldPlay) {
                exoPlayer.play()
            } else {
                exoPlayer.pause()
            }
        }

        val focusRequester = remember { FocusRequester() }
        VideoPlayerOverlay(modifier = Modifier.align(Alignment.BottomCenter),
            focusRequester = focusRequester,
            state = videoPlayerState,
            isPlaying = isPlaying,
            centerButton = {
                VideoPlayerControlsIcon(
                    modifier = Modifier.focusRequester(focusRequester),
                    icon = if (!isPlaying) R.drawable.play_icon else R.drawable.pause,
                    onClick = { onPlayPauseToggle(!isPlaying) },
                    state = videoPlayerState,
                    isPlaying = isPlaying,
                )
            },
            subtitles = { /* TODO Implement subtitles */ },
            controls = {
                VideoPlayerControls(
                    isPlaying = isPlaying,
                    contentCurrentPosition = contentCurrentPosition,
                    exoPlayer = exoPlayer,
                    state = videoPlayerState,
                    title = state.workoutUiState.title,
                    instructor = state.workoutUiState.instructor,
                )
            }
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun VideoPlayerControls(
    isPlaying: Boolean,
    contentCurrentPosition: Long,
    exoPlayer: ExoPlayer,
    state: VideoPlayerState,
    title: String,
    instructor: String,
) {
    VideoPlayerFrame(
        videoTitle = {
            VideoPlayerTitle(
                title = title,
                secondaryText = instructor,
            )
        },
        videoActions = {
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                VideoPlayerControlsIcon(
                    icon = R.drawable.subtitles,
                    state = state,
                    isPlaying = isPlaying,
                ) {}
                VideoPlayerControlsIcon(
                    icon = R.drawable.audio,
                    state = state,
                    isPlaying = isPlaying,
                ) {}
                VideoPlayerControlsIcon(
                    icon = R.drawable.settings,
                    state = state,
                    isPlaying = isPlaying,
                ) {}
                CustomFillButton(
                    text = stringResource(R.string.end_workout),
                    textStyle = MaterialTheme.typography.titleMedium
                        .copy(color = MaterialTheme.colorScheme.surface),
                    onClick = {}
                )
            }
        },
        videoSeeker = {
            VideoPlayerSeeker(
                state = state,
                onSeek = { exoPlayer.seekTo(exoPlayer.duration.times(it).toLong()) },
                contentProgress = contentCurrentPosition.milliseconds,
                contentDuration = exoPlayer.duration.milliseconds
            )
        }
    )
}


@Preview(device = Devices.TV_1080p)
@Composable
fun PreviewVideoPlayerScreen() {
    JetFitTheme {
        VideoPlayerContent(
            state = VideoPlayerUiState(
                isLoading = false,
                error = null,
                workoutUiState = WorkoutUiState()
            )
        ) {}
    }
}