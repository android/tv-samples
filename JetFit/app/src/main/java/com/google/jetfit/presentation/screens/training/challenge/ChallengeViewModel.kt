package com.google.jetfit.presentation.screens.training.challenge

import androidx.lifecycle.ViewModel
import com.google.jetfit.data.repositories.JetFitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    repository: JetFitRepository
) : ViewModel() {
    val id: String = "1"

    private val _state: MutableStateFlow<ChallengeUiState> by lazy {
        MutableStateFlow(
            ChallengeUiState()
        )
    }
    val state = _state.asStateFlow()

    init {
        try {
            repository.getChallengeById(id).also { challenge ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        subtitle = "${challenge.instructorName}  |  ${challenge.workoutType.value}",
                        title = challenge.name,
                        description = challenge.description,
                        intensity = challenge.intensity.value,
                        imageUrl = challenge.imageUrl,
                        numberOfDays = challenge.numberOfDays.toString(),
                        minutesPerDay = challenge.minutesPerDay.toString(),
                        tabs = challenge.weaklyPlans.map { weaklyPlan -> weaklyPlan.first },
                        weaklyPlans = challenge.weaklyPlans.map { weaklyPlan ->
                            mapOf(
                                Pair(
                                    weaklyPlan.first,
                                    weaklyPlan.second.map { workout -> workout.toItemUiState() })
                            )
                        }
                    )
                }
            }
        } catch (e: Throwable) {
            _state.update {
                it.copy(isLoading = false, error = e.message.toString())
            }
        }
    }
}