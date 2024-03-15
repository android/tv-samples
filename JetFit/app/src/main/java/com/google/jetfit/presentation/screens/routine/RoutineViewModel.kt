package com.google.jetfit.presentation.screens.routine

import com.google.jetfit.data.entities.Routine
import com.google.jetfit.data.repositories.JetFitRepository
import com.google.jetfit.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    repository: JetFitRepository
): BaseViewModel<RoutineUiState, RoutineUiEffect>(RoutineUiState()), RoutineInteractionListener {
    val id: String = "1"

    init {
        tryToExecute({ repository.getRoutineById(id) }, ::onGettingRoutineSuccess, ::onError)
    }

    private fun onGettingRoutineSuccess(routine: Routine) {
        updateState(routine.toUiState())
    }

    private fun onError(throwable: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onStartRoutineClicked() {
        sendNewEffect(RoutineUiEffect.NavigateToVideoPlayer(id))
    }

    override fun onSetupDailyReminderClicked() {
        TODO("Not yet implemented")
    }

    override fun onFavoriteClicked() {
        TODO("Not yet implemented")
    }
}