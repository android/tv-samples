package com.google.jetfit.presentation.screens.training.challenge

data class ChallengeUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val subtitle: String = "",
    val title: String = "",
    val description: String = "",
    val intensity: String = "",
    val imageUrl: String = "",
    val numberOfDays: String = "",
    val minutesPerDay: String = "",
    val tabs: List<String> = listOf(),
    val weaklyPlans: List<Map<String, List<ChallengeWorkoutItemUiState>>> = listOf(),
    val shouldShowDetails: Boolean = true,
    val isFavorite: Boolean = false,
    val pages: List<Page> = listOf(Page.Details, Page.Tabs)
)

data class ChallengeWorkoutItemUiState(
    val id: String,
    val imageUrl: String,
    val title: String,
    val subtitle: String
)