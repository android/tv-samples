package com.google.jetfit.presentation.screens.challenge

import com.google.jetfit.data.entities.Challenge
import com.google.jetfit.data.repositories.JetFitRepository
import com.google.jetfit.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    repository: JetFitRepository
) : BaseViewModel<ChallengeUiState, ChallengeUiEffect>(ChallengeUiState()),
    ChallengeInteractionListener {
    val id: String = "1"

    init {
        tryToExecute({ repository.getChallengeById(id) }, ::onGettingChallengeSuccess, ::onError)
    }

    private fun onGettingChallengeSuccess(challenge: Challenge) {
        updateState(challenge.toUiState())
    }

    private fun onError(throwable: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onDownPressed() {
        updateState(state.value.copy(shouldShowDetails = false))
    }

    override fun onUpPressed() {
        updateState(state.value.copy(shouldShowDetails = true))
    }

    override fun onTabChanged(newIndex: Int) {
        updateState(state.value.copy(selectedTabIndex = newIndex))
    }

    override fun onWorkoutClicked(id: String) {
        TODO("Not yet implemented")
    }

    override fun onStartSessionClicked() {
        TODO("Not yet implemented")
    }

    override fun onAddToFavoritesClicked() {
        TODO("Not yet implemented")
    }
}