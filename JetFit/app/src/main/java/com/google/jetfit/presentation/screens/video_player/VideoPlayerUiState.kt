package com.google.jetfit.presentation.screens.video_player

import javax.annotation.concurrent.Immutable

@Immutable
data class VideoPlayerUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val workoutUiState: WorkoutUiState = WorkoutUiState()
)

@Immutable
data class WorkoutUiState(
    val videoUrl: String = "",
    val title: String = "",
    val instructor: String = "",
)