package com.google.jetfit.presentation.screens.player.video

import javax.annotation.concurrent.Immutable

@Immutable
data class VideoPlayerUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val workoutUiState: WorkoutUiState = WorkoutUiState()
)

@Immutable
data class WorkoutUiState(
    val id: String = "",
    val videoUrl: String = "",
    val title: String = "",
    val instructor: String = "",
    val subtitles: String? = null,
    val subtitleUri: String? = null,
)