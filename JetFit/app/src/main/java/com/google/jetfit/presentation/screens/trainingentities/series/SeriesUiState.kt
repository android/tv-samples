package com.google.jetfit.presentation.screens.trainingentities.series

data class SeriesUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val subtitle: String = "",
    val title: String = "",
    val description: String = "",
    val duration: String = "",
    val intensity: String = "",
    val numberOfWeeks: String = "",
    val numberOfClasses: String = "",
    val minutesPerDay: String = "",
    val imageUrl: String = ""
)