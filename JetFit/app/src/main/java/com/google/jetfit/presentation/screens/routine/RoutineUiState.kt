package com.google.jetfit.presentation.screens.routine

import com.google.jetfit.data.entities.Routine

data class RoutineUiState(
    val subtitle: String = "",
    val title: String = "",
    val description: String = "",
    val duration: String = "",
    val intensity: String = "",
    val posterUrl: String = ""
)

fun Routine.toUiState(): RoutineUiState = RoutineUiState(
    subtitle = "$instructorName  |  ${workoutType.value}",
    title = name,
    description = description,
    duration = "$duration min",
    intensity = intensity.value,
    posterUrl = imageUrl
)