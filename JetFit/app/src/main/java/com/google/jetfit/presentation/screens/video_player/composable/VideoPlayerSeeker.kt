package com.google.jetfit.presentation.screens.video_player.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.google.jetfit.R
import kotlin.time.Duration

@Composable
fun VideoPlayerSeeker(
    focusRequester: FocusRequester,
    state: VideoPlayerState,
    isPlaying: Boolean,
    onPlayPauseToggle: (Boolean) -> Unit,
    onSeek: (Float) -> Unit,
    contentProgress: Duration,
    contentDuration: Duration,
    modifier: Modifier = Modifier,
) {
    val textProgress = contentProgress.toComponents { h, m, s, _ ->
        if (h > 0) {
            "$h:${m}:${s}"
        } else {
            ":${m}:${s}"
        }

    }
    val textDuration = contentDuration.toComponents { h, m, s, _ ->
        if (h > 0) {
            "$h:${m}:${s}"
        } else {
            ":${m}:${s}"
        }
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VideoPlayerControlsIcon(
            modifier = Modifier.focusRequester(focusRequester),
            icon = if (!isPlaying) R.drawable.audio else R.drawable.settings,
            onClick = { onPlayPauseToggle(!isPlaying) },
            state = state,
            isPlaying = isPlaying,
        )
        VideoPlayerControllerIndicator(
            progress = (contentProgress / contentDuration).toFloat(),
            onSeek = onSeek,
            state = state
        )
        VideoPlayerDurationText(textProgress = textProgress, textDuration = textDuration)
    }
}