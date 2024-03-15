package com.google.jetfit.presentation.screens.video_player

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.presentation.screens.video_player.composable.VideoPlayerControlsIcon
import com.google.jetfit.presentation.screens.video_player.composable.VideoPlayerFrame
import com.google.jetfit.presentation.screens.video_player.composable.VideoPlayerOverlay
import com.google.jetfit.presentation.screens.video_player.composable.VideoPlayerSeeker
import com.google.jetfit.presentation.screens.video_player.composable.VideoPlayerState
import com.google.jetfit.presentation.screens.video_player.composable.VideoPlayerTitle
import com.google.jetfit.presentation.screens.video_player.composable.rememberVideoPlayerState
import com.google.jetfit.presentation.utils.handleDPadKeyEvents
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun VideoPlayerScreen(viewModel: VideoPlayerViewModel = hiltViewModel()) {
    VideoPlayerContent {

    }
}

@Composable
private fun VideoPlayerContent(onBackPressed: () -> Unit) {
    val context = LocalContext.current
    val videoPlayerState = rememberVideoPlayerState(hideSeconds = 4)
    val exoPlayer = rememberExoPlayer(context)
    LaunchedEffect(exoPlayer, "") {
        exoPlayer.setMediaItem(
            MediaItem.Builder()
                .setUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
//                .setSubtitleConfigurations(
//                    if (movieDetails.subtitleUri == null) {
//                        emptyList()
//                    } else {
//                        listOf(
//                            MediaItem.SubtitleConfiguration.Builder(Uri.parse(movieDetails.subtitleUri))
//                                .setMimeType("application/vtt")
//                                .setLanguage("en")
//                                .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
//                                .build()
//                        )
//                    }
                // )
                .build()
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

    Box(Modifier.focusable()) {
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
        VideoPlayerOverlay(
            modifier = Modifier.align(Alignment.BottomCenter),
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
                    isPlaying,
                    contentCurrentPosition,
                    exoPlayer,
                    videoPlayerState,
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
) {
    VideoPlayerFrame(
        videoTitle = {
            VideoPlayerTitle(
                title = "Battle ropes HIIT",
                secondaryText = "Hugo Wright",
            )
        },
        videoActions = {
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VideoPlayerControlsIcon(
                    icon = R.drawable.subtitles,
                    state = state,
                    isPlaying = isPlaying,
                ) {}
                VideoPlayerControlsIcon(
                    modifier = Modifier.padding(start = 12.dp),
                    icon = R.drawable.audio,
                    state = state,
                    isPlaying = isPlaying,
                ) {}
                VideoPlayerControlsIcon(
                    modifier = Modifier.padding(start = 12.dp),
                    icon = R.drawable.settings,
                    state = state,
                    isPlaying = isPlaying,
                ) {}
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.colors(
                        containerColor = Color.White
                    ),
                    shape = ButtonDefaults.shape(RoundedCornerShape(44.dp)),
                    modifier = Modifier
                        .height(44.dp)
                        .width(126.2.dp)
                ) {
                    Text(text = "End workout")
                }
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


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun rememberExoPlayer(context: Context) = remember {
    ExoPlayer.Builder(context)
        .setSeekForwardIncrementMs(10)
        .setSeekBackIncrementMs(10)
        .setMediaSourceFactory(
            ProgressiveMediaSource.Factory(DefaultDataSource.Factory(context))
        )
        .setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        .build()
        .apply {
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
        }
}


private fun Modifier.dPadEvents(
    exoPlayer: ExoPlayer,
    videoPlayerState: VideoPlayerState,
): Modifier = this.handleDPadKeyEvents(
    onLeft = {
//        exoPlayer.seekBack()
//        pulseState.setType(BACK)
    },
    onRight = {
//        exoPlayer.seekForward()
//        pulseState.setType(FORWARD)
    },
    onUp = { videoPlayerState.showControls() },
    onDown = { videoPlayerState.showControls() },
    onEnter = {
        exoPlayer.pause()
        videoPlayerState.showControls()
    }
)