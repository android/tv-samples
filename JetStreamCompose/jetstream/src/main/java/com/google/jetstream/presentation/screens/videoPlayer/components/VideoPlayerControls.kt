/*
 * Copyright 2025 Google LLC
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

package com.google.jetstream.presentation.screens.videoPlayer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.data.util.StringConstants
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun VideoPlayerControls(
    movieDetails: MovieDetails,
    contentCurrentPosition: Long,
    contentDuration: Long,
    isPlaying: Boolean,
    hasNextMovie: Boolean,
    hasPreviousMovie: Boolean,
    repeatMode: Int,
    focusRequester: FocusRequester,
    onPlayPauseToggle: () -> Unit = {},
    onSeek: (Float) -> Unit = {},
    onShowControls: () -> Unit = {},
    onPreviousMovie: () -> Unit = {},
    onNextMovie: () -> Unit = {},
    onRepeat: () -> Unit = {}
) {
    VideoPlayerMainFrame(
        mediaTitle = {
            VideoPlayerMediaTitle(
                title = movieDetails.name,
                secondaryText = movieDetails.releaseDate,
                tertiaryText = movieDetails.director,
                type = VideoPlayerMediaTitleType.DEFAULT
            )
        },
        mediaActions = {
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                VideoPlayerControlsIcon(
                    icon = Icons.Default.SkipPrevious,
                    isPlaying = isPlaying,
                    contentDescription =
                    StringConstants.Composable.VideoPlayerControlSkipPreviousButton,
                    onShowControls = onShowControls,
                    onClick = onPreviousMovie
                )
                VideoPlayerControlsIcon(
                    icon = Icons.Default.SkipNext,
                    isPlaying = isPlaying,
                    contentDescription =
                    StringConstants.Composable.VideoPlayerControlSkipNextButton,
                    onShowControls = onShowControls,
                    onClick = onNextMovie
                )
                RepeatIcon(
                    isPlaying = isPlaying,
                    repeatMode = repeatMode,
                    onShowControls = onShowControls,
                    onClick = onRepeat,
                )
                VideoPlayerControlsIcon(
                    icon = Icons.Default.SkipPrevious,
                    isPlaying = isPlaying,
                    contentDescription =
                    StringConstants.Composable.VideoPlayerControlClosedCaptionsButton,
                    onShowControls = onShowControls
                )
                VideoPlayerControlsIcon(
                    icon = Icons.Default.AutoAwesomeMotion,
                    isPlaying = isPlaying,
                    contentDescription =
                    StringConstants.Composable.VideoPlayerControlPlaylistButton,
                    onShowControls = onShowControls
                )
                VideoPlayerControlsIcon(
                    icon = Icons.Default.ClosedCaption,
                    isPlaying = isPlaying,
                    contentDescription =
                    StringConstants.Composable.VideoPlayerControlClosedCaptionsButton,
                    onShowControls = onShowControls
                )
                VideoPlayerControlsIcon(
                    icon = Icons.Default.Settings,
                    isPlaying = isPlaying,
                    contentDescription =
                    StringConstants.Composable.VideoPlayerControlSettingsButton,
                    onShowControls = onShowControls
                )
            }
        },
        seeker = {
            VideoPlayerSeeker(
                focusRequester = focusRequester,
                isPlaying = isPlaying,
                onPlayPauseToggle = onPlayPauseToggle,
                onSeek = onSeek,
                onShowControls = onShowControls,
                contentProgress = contentCurrentPosition.milliseconds,
                contentDuration = contentDuration.milliseconds,
            )
        },
        more = null
    )
}
