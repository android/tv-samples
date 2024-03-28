package com.google.jetfit.presentation.screens.training.training_entities

import com.google.jetfit.R
import com.google.jetfit.presentation.screens.training.training_entities.TrainingEntityUiState.ContentType.*

data class TrainingEntityUiState(
    val contentType: ContentType = ROUTINE,
    val subtitle: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val itemsInfo: List<TrainingInfoItem> = listOf(),
    val tabs: List<String> = listOf(),
    val weaklyPlans: List<Map<String, List<ChallengeWorkoutItemUiState>>> = listOf(),
    val isFavorite: Boolean = false,
    val challengePages: List<TrainingEntityPages> = listOf(
        TrainingEntityPages.EntityDetails,
        TrainingEntityPages.EntityTabs
    )
) {
    data class ChallengeWorkoutItemUiState(
        val id: String,
        val imageUrl: String,
        val title: String,
        val time: String,
        val typeText: String,
    )
    data class TrainingInfoItem(
        val info: String = "",
        val label: String = ""
    )

    enum class ContentType {
        CHALLENGES,
        SERIES,
        WORK_OUT,
        ROUTINE
    }

}


fun TrainingEntityUiState.ContentType.getStartButtonID() = when (this) {
    CHALLENGES -> R.string.start_session
    SERIES -> R.string.start_program
    WORK_OUT -> R.string.start_workout
    ROUTINE -> R.string.start_routine
}

fun TrainingEntityUiState.ContentType.getSecondaryButtonID() = when (this) {
    CHALLENGES -> R.string.add_to_favorites
    SERIES -> R.string.recommend_schedule
    else -> R.string.set_up_daily_reminder
}

fun TrainingEntityUiState.ContentType.getSecondaryButtonIcon() = when (this) {
    CHALLENGES -> R.drawable.fav_icon
    SERIES -> R.drawable.message_icon
    else -> R.drawable.bell_icon
}

fun TrainingEntityUiState.ContentType.isSecondaryButtonVisible() = when (this) {
    WORK_OUT -> false
    else -> true
}