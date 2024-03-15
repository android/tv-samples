package com.google.jetfit.presentation.screens.challenge

import com.google.jetfit.data.entities.Challenge

data class ChallengeUiState(
    val subtitle: String = "",
    val title: String = "",
    val description: String = "",
    val intensity: String = "",
    val posterUrl: String = "",
    val numberOfDays: String = "",
    val minutesPerDay: String = "",
    val tabs: List<String> = listOf(),
    val weaklyPlans: List<Map<String, List<WorkoutItemUiState>>> = listOf(),
    val shouldShowDetails: Boolean = true,
    val selectedTabIndex: Int = 0,
    val isFavorite: Boolean = false
)

fun Challenge.toUiState(): ChallengeUiState = ChallengeUiState(
    subtitle = "$instructorName  |  ${workoutType.value}",
    title = name,
    description = description,
    intensity = intensity.value,
    posterUrl = imageUrl,
    numberOfDays = numberOfDays.toString(),
    minutesPerDay = minutesPerDay.toString(),
    tabs = weaklyPlans.map { it.first },
    weaklyPlans = weaklyPlans.map {
        mapOf(Pair(it.first, it.second.map { workout -> workout.toItemUiState() }))
    }
)

