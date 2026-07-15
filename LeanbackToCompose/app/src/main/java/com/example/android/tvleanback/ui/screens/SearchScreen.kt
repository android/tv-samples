package com.example.android.tvleanback.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.example.android.tvleanback.data.VideoRepository
import com.example.android.tvleanback.model.Video
import com.example.android.tvleanback.ui.components.LoadingIndicator
import com.example.android.tvleanback.ui.components.MovieCard
import com.example.android.tvleanback.ui.components.TvTextField
import com.example.android.tvleanback.ui.theme.TvLeanbackTheme

@OptIn(ExperimentalTvMaterial3Api::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    onVideoClick: (Video) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var allVideos by remember { mutableStateOf<List<Video>?>(null) }
    var query by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val textFieldFocusRequester = remember { FocusRequester() }
    val gridFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        allVideos = VideoRepository.getVideos(context)
    }

    val filteredVideos = remember(allVideos, query) {
        allVideos?.filter { video ->
            if (query.isBlank()) true
            else {
                (video.title?.contains(query, ignoreCase = true) == true) ||
                (video.description?.contains(query, ignoreCase = true) == true) ||
                (video.category?.contains(query, ignoreCase = true) == true)
            }
        } ?: emptyList()
    }

    TvLeanbackTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(32.dp)
        ) {
            // Search Header Box
            TvTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = "Search for videos by title, category, or description...",
                focusRequester = textFieldFocusRequester,
                imeAction = ImeAction.Search,
                onImeAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Results Grid
            if (allVideos == null) {
                LoadingIndicator()
            } else if (filteredVideos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No matching videos found for \"$query\"",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Text(
                    text = "Results (${filteredVideos.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRestorer { gridFocusRequester }
                ) {
                    itemsIndexed(filteredVideos) { index, video ->
                        MovieCard(
                            video = video,
                            onClick = onVideoClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(
                                    if (index == 0) Modifier.focusRequester(gridFocusRequester)
                                    else Modifier
                                )
                        )
                    }
                }
            }
        }
    }
}
