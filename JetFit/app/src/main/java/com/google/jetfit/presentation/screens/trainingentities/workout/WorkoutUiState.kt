package com.google.jetfit.presentation.screens.trainingentities.workout

data class WorkoutUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val subtitle: String = "",
    val title: String = "",
    val description: String = "",
    val duration: String = "",
    val intensity: String = "",
    val imageUrl: String = ""
)