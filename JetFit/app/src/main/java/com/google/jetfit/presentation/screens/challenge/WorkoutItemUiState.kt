package com.google.jetfit.presentation.screens.challenge

import com.google.jetfit.data.entities.Workout

data class WorkoutItemUiState(
    val id: String,
    val imageUrl: String,
    val title: String,
    val subtitle: String
)

fun Workout.toItemUiState(): WorkoutItemUiState = WorkoutItemUiState(
    id = id,
    imageUrl = imageUrl,
    title = name,
    subtitle = "$duration min  |  ${intensity.level}",
)