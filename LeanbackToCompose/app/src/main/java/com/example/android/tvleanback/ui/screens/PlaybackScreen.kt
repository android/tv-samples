package com.example.android.tvleanback.ui.screens

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player as Media3Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.material3.buttons.PlayPauseButton
import androidx.media3.ui.compose.material3.buttons.SeekBackButton
import androidx.media3.ui.compose.material3.buttons.SeekForwardButton
import androidx.preference.PreferenceManager
import com.example.android.tvleanback.R
import com.example.android.tvleanback.data.VideoRepository
import com.example.android.tvleanback.model.Video
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
@Composable
fun PlaybackScreen(
    video: Video,
    onVideoChanged: ((Video) -> Unit)? = null,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentVideo by remember(video) { mutableStateOf(video) }
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }
    var showControls by remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    var autoHideJob by remember { mutableStateOf<Job?>(null) }

    fun scheduleAutoHide() {
        autoHideJob?.cancel()
        autoHideJob = coroutineScope.launch {
            delay(5000)
            showControls = false
        }
    }

    LaunchedEffect(currentVideo) {
        onVideoChanged?.invoke(currentVideo)
        focusRequester.requestFocus()
        scheduleAutoHide()

        val listener = object : Media3Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Media3Player.STATE_ENDED) {
                    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                    val recKey = context.getString(R.string.pref_key_recommendations)
                    val autoplayNext = prefs.getBoolean(recKey, true)
                    if (autoplayNext) {
                        coroutineScope.launch {
                            val allVideos = VideoRepository.getVideos(context)
                            val categoryVideos = allVideos.filter { it.category == currentVideo.category }
                            val currentIndex = categoryVideos.indexOfFirst { it.id == currentVideo.id }
                            if (currentIndex >= 0 && currentIndex + 1 < categoryVideos.size) {
                                currentVideo = categoryVideos[currentIndex + 1]
                            } else {
                                onFinish()
                            }
                        }
                    } else {
                        onFinish()
                    }
                }
            }
        }
        exoPlayer.addListener(listener)

        currentVideo.videoUrl?.let { url ->
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            autoHideJob?.cancel()
            exoPlayer.release()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .focusRequester(focusRequester)
            .focusable()
            .onPreviewKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    when (event.key) {
                        Key.DirectionCenter, Key.Enter, Key.NumPadEnter, Key.MediaPlayPause, Key.Spacebar -> {
                            if (exoPlayer.isPlaying) {
                                exoPlayer.pause()
                            } else {
                                exoPlayer.play()
                            }
                            showControls = true
                            scheduleAutoHide()
                            true
                        }
                        Key.DirectionLeft, Key.MediaRewind -> {
                            val newPos = (exoPlayer.currentPosition - 10_000L).coerceAtLeast(0L)
                            exoPlayer.seekTo(newPos)
                            showControls = true
                            scheduleAutoHide()
                            true
                        }
                        Key.DirectionRight, Key.MediaFastForward -> {
                            val duration = exoPlayer.duration.coerceAtLeast(0L)
                            val newPos = (exoPlayer.currentPosition + 10_000L).coerceAtMost(duration)
                            exoPlayer.seekTo(newPos)
                            showControls = true
                            scheduleAutoHide()
                            true
                        }
                        Key.DirectionUp, Key.DirectionDown -> {
                            showControls = !showControls
                            if (showControls) {
                                scheduleAutoHide()
                            } else {
                                autoHideJob?.cancel()
                            }
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            },
        contentAlignment = Alignment.Center
    ) {
        PlayerSurface(
            player = exoPlayer,
            modifier = Modifier.fillMaxSize()
        )

        if (showControls) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000))
                    .padding(48.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SeekBackButton(player = exoPlayer)
                    PlayPauseButton(player = exoPlayer)
                    SeekForwardButton(player = exoPlayer)
                }
            }
        }
    }
}
