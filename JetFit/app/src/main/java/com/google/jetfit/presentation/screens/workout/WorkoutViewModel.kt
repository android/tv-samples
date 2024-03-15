package com.google.jetfit.presentation.screens.workout

import com.google.jetfit.data.entities.Workout
import com.google.jetfit.data.repositories.JetFitRepository
import com.google.jetfit.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val repository: JetFitRepository,
) : BaseViewModel<WorkoutUiState, WorkoutUiEffect>(WorkoutUiState()), WorkoutInteractionListener {
    val id: String = "1"

    init {
        tryToExecute({ repository.getWorkoutById(id) }, ::onGettingWorkoutSuccess, ::onError)
    }

    private fun onGettingWorkoutSuccess(workout: Workout) {
        updateState(workout.toUiState())
    }

    private fun onError(throwable: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onStartWorkoutClicked() {
        sendNewEffect(WorkoutUiEffect.NavigateToVideoPlayer(id = id))
    }
}