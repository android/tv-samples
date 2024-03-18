package com.google.jetfit.presentation.screens.trainingentities.routine

data class RoutineUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val subtitle: String = "",
    val title: String = "",
    val description: String = "",
    val duration: String = "",
    val intensity: String = "",
    val imageUrl: String = ""
)