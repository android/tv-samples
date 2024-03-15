package com.google.jetfit.presentation.screens.workout

import com.google.jetfit.data.entities.Workout

data class WorkoutUiState(
    val subtitle: String = "",
    val title: String = "",
    val description: String = "",
    val duration: String = "",
    val intensity: String = "",
    val posterUrl: String = ""
)

fun Workout.toUiState(): WorkoutUiState = WorkoutUiState(
    subtitle = "$instructorName  |  ${workoutType.value}",
    title = name,
    description = description,
    duration = duration.toString(),
    intensity = intensity.value,
    posterUrl = imageUrl
)