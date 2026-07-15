package com.example.android.tvleanback.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.example.android.tvleanback.R
import com.example.android.tvleanback.data.VideoRepository
import com.example.android.tvleanback.model.Video
import com.example.android.tvleanback.ui.components.LoadingIndicator
import com.example.android.tvleanback.ui.components.MovieCard
import com.example.android.tvleanback.ui.theme.TvLeanbackTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun VerticalGridScreen(
    onVideoClick: (Video) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var videos by remember { mutableStateOf<List<Video>?>(null) }

    val firstCardFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        videos = VideoRepository.getVideos(context)
    }

    LaunchedEffect(videos) {
        if (videos != null) {
            try {
                firstCardFocusRequester.requestFocus()
            } catch (e: Exception) {
                // Focus request might fail if called before layout is settled,
                // but LaunchedEffect(videos) usually runs after composition.
            }
        }
    }

    TvLeanbackTheme {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (videos == null) {
                LoadingIndicator()
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.vertical_grid_title),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Button(onClick = onSearchClick) {
                            Text(text = "SEARCH")
                        }
                    }

                    // Grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(videos!!) { index, video ->
                            MovieCard(
                                video = video,
                                onClick = onVideoClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(
                                        if (index == 0) Modifier.focusRequester(firstCardFocusRequester)
                                        else Modifier
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}
