package com.example.android.tvleanback.ui.components

import kotlin.OptIn
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.preference.PreferenceManager
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CompactCard
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.example.android.tvleanback.R
import com.example.android.tvleanback.model.Video
import kotlinx.coroutines.delay

@OptIn(ExperimentalTvMaterial3Api::class, UnstableApi::class)
@Composable
fun MovieCard(
    video: Video,
    onClick: (Video) -> Unit,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 220.dp
) {
    val context = LocalContext.current
    var isFocused by remember { mutableStateOf(false) }
    var showPreview by remember { mutableStateOf(false) }
    var player by remember { mutableStateOf<ExoPlayer?>(null) }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val previewsKey = context.getString(R.string.pref_key_autoplay_previews)
            val enabled = prefs.getBoolean(previewsKey, true)
            if (enabled && !video.videoUrl.isNullOrEmpty()) {
                delay(500) // Debounce delay so scrolling quickly past cards does not trigger video load
                val exoPlayer = ExoPlayer.Builder(context).build().apply {
                    volume = 0f // Mute audio for card preview
                    setMediaItem(MediaItem.fromUri(video.videoUrl))
                    prepare()
                    playWhenReady = true
                }
                player = exoPlayer
                showPreview = true
                delay(6000) // Play preview for 6 seconds then fade back to thumbnail
                showPreview = false
                exoPlayer.release()
                player = null
            }
        } else {
            showPreview = false
            player?.release()
            player = null
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player?.release()
            player = null
        }
    }

    CompactCard(
        onClick = { onClick(video) },
        image = {
            Crossfade(
                targetState = showPreview && player != null,
                label = "CardPreviewCrossfade"
            ) { isPreviewing ->
                if (isPreviewing && player != null) {
                    PlayerSurface(
                        player = player!!,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    )
                } else {
                    AsyncImage(
                        model = video.cardImageUrl,
                        contentDescription = video.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    )
                }
            }
        },
        title = {
            Text(
                text = video.title ?: "",
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 10.dp, start = 8.dp, end = 8.dp)
            )
        },
        subtitle = {
            if (!video.studio.isNullOrEmpty()) {
                Text(
                    text = video.studio,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 6.dp)
                )
            }
        },
        scale = CardDefaults.scale(focusedScale = 1.08f),
        border = CardDefaults.border(
            focusedBorder = androidx.tv.material3.Border(
                border = BorderStroke(2.dp, Color.White)
            )
        ),
        modifier = modifier
            .width(cardWidth)
            .onFocusChanged { isFocused = it.isFocused }
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MovieCardPlaceholder(
    modifier: Modifier = Modifier,
    cardWidth: Dp = 220.dp
) {
    CompactCard(
        onClick = { },
        image = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f))
            )
        },
        title = {
            Text(
                text = " ",
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(top = 10.dp, start = 8.dp, end = 8.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.10f))
            )
        },
        subtitle = {
            Text(
                text = " ",
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 6.dp)
                    .fillMaxWidth(0.6f)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.08f))
            )
        },
        scale = CardDefaults.scale(focusedScale = 1.08f),
        border = CardDefaults.border(
            focusedBorder = androidx.tv.material3.Border(
                border = BorderStroke(2.dp, Color.White)
            )
        ),
        modifier = modifier.width(cardWidth)
    )
}
