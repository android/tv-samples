package com.google.jetfit.presentation.screens.more_options

import com.google.jetfit.data.entities.TrainingDetails

sealed interface MoreOptionsUiState {
    data object Loading : MoreOptionsUiState
    data object Error : MoreOptionsUiState
    data class Ready(
        val trainingDetails: TrainingDetails
    ) : MoreOptionsUiState {
        fun formatTimeAndTypeTraining(): String {
            return "${trainingDetails.time} | ${trainingDetails.type} ••••"
        }
    }
}
