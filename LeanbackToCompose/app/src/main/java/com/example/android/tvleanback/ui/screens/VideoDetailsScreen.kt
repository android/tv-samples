package com.example.android.tvleanback.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.OutlinedButtonDefaults
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.example.android.tvleanback.data.VideoRepository
import com.example.android.tvleanback.model.Video
import com.example.android.tvleanback.ui.components.MovieCard
import com.example.android.tvleanback.ui.components.MovieCardPlaceholder
import com.example.android.tvleanback.ui.components.SectionHeader
import com.example.android.tvleanback.ui.components.StudioBadge
import com.example.android.tvleanback.ui.theme.TvLeanbackTheme

@OptIn(ExperimentalTvMaterial3Api::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun VideoDetailsScreen(
    video: Video,
    onWatchTrailerClick: (Video) -> Unit,
    onRelatedVideoClick: (Video) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var allVideos by remember { mutableStateOf<List<Video>?>(null) }
    val focusRequester = remember { FocusRequester() }
    var showDescriptionOverlay by remember { mutableStateOf(false) }
    var hasVisualOverflow by remember(video.description) { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        allVideos = VideoRepository.getVideos(context)
        focusRequester.requestFocus()
    }

    val relatedVideos = remember(allVideos, video) {
        allVideos?.filter { it.category == video.category && it.id != video.id } ?: emptyList()
    }

    TvLeanbackTheme {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Background Image with Gradient Scrim
            AsyncImage(
                model = video.bgImageUrl ?: video.cardImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.6f),
                                MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 36.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Details Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp)
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Poster Image
                    AsyncImage(
                        model = video.cardImageUrl,
                        contentDescription = video.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(220.dp)
                            .height(330.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )
                    Spacer(modifier = Modifier.width(40.dp))

                    // Info & Actions
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(330.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            if (!video.studio.isNullOrEmpty()) {
                                StudioBadge(studio = video.studio)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            Text(
                                text = video.title ?: "",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 2,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = video.description ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 3,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                onTextLayout = { textLayoutResult ->
                                    if (textLayoutResult.hasVisualOverflow) {
                                        hasVisualOverflow = true
                                    }
                                }
                            )
                            if (hasVisualOverflow) {
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = { showDescriptionOverlay = true },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    colors = OutlinedButtonDefaults.colors(
                                        containerColor = Color.Transparent,
                                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        focusedContainerColor = Color.Transparent,
                                        focusedContentColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                    border = OutlinedButtonDefaults.border(
                                        border = androidx.tv.material3.Border(
                                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Transparent)
                                        ),
                                        focusedBorder = androidx.tv.material3.Border(
                                            border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                                        )
                                    ),
                                    scale = OutlinedButtonDefaults.scale(focusedScale = 1.0f)
                                ) {
                                    Text(
                                        text = "READ MORE",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }

                        // Action Buttons Row (anchored to bottom of 330.dp container, zero jump when READ MORE appears!)
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Button(
                                onClick = { onWatchTrailerClick(video) },
                                modifier = Modifier.focusRequester(focusRequester)
                            ) {
                                Text(text = "WATCH TRAILER")
                            }
                            Button(onClick = {
                                Toast.makeText(context, "Rent $3.99", Toast.LENGTH_SHORT).show()
                            }) {
                                Text(text = "RENT $3.99")
                            }
                            Button(onClick = {
                                Toast.makeText(context, "Buy $9.99", Toast.LENGTH_SHORT).show()
                            }) {
                                Text(text = "BUY $9.99")
                            }
                        }
                    }
                }

                // Related Videos Row or Loading Placeholder
                if (allVideos == null) {
                    // Skeleton Loading Placeholder (exact sub-pixel layout match to prevent any UI expansion or jump!)
                    Column {
                        SectionHeader(
                            title = " ",
                            modifier = Modifier
                                .padding(horizontal = 48.dp)
                                .padding(top = 16.dp, bottom = 0.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 48.dp, end = 48.dp, top = 4.dp, bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            repeat(4) {
                                MovieCardPlaceholder()
                            }
                        }
                    }
                } else if (relatedVideos.isNotEmpty()) {
                    val firstItemFocusRequester = remember { FocusRequester() }
                    Column {
                        SectionHeader(
                            title = "Related Videos",
                            modifier = Modifier
                                .padding(horizontal = 48.dp)
                                .padding(top = 16.dp, bottom = 0.dp)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(start = 48.dp, end = 48.dp, top = 4.dp, bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.focusRestorer { firstItemFocusRequester }
                        ) {
                            itemsIndexed(relatedVideos) { index, relatedVideo ->
                                MovieCard(
                                    video = relatedVideo,
                                    onClick = onRelatedVideoClick,
                                    modifier = if (index == 0) Modifier.focusRequester(firstItemFocusRequester) else Modifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDescriptionOverlay) {
        BackHandler {
            showDescriptionOverlay = false
        }
        val overlayFocusRequester = remember { FocusRequester() }
        val scrollState = rememberScrollState()

        LaunchedEffect(Unit) {
            overlayFocusRequester.requestFocus()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.90f))
                .clickable { showDescriptionOverlay = false }
                .padding(horizontal = 48.dp, vertical = 36.dp)
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithContent {
                    drawContent()
                    val fadeHeight = 64.dp.toPx()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Black, Color.Transparent),
                            startY = size.height - fadeHeight,
                            endY = size.height
                        ),
                        topLeft = Offset(0f, size.height - fadeHeight),
                        size = Size(size.width, fadeHeight),
                        blendMode = BlendMode.DstIn
                    )
                },
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(overlayFocusRequester)
                    .focusable()
                    .onKeyEvent { event ->
                        if (event.type == KeyEventType.KeyDown) {
                            when (event.key) {
                                Key.DirectionDown -> {
                                    scrollState.dispatchRawDelta(250f)
                                    true
                                }
                                Key.DirectionUp -> {
                                    scrollState.dispatchRawDelta(-250f)
                                    true
                                }
                                else -> false
                            }
                        } else {
                            false
                        }
                    }
                    .verticalScroll(scrollState)
                    .clickable { showDescriptionOverlay = false }
            ) {
                Text(
                    text = video.title ?: "",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = video.description ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}
