package com.google.jetfit.presentation.screens.training.training_entities

import androidx.lifecycle.ViewModel
import com.google.jetfit.data.repositories.JetFitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TrainigEntityViewModel @Inject constructor(
    repository: JetFitRepository
) : ViewModel(), TrainingEntityInteractions {
    val id: String = "1"

    private val _state: MutableStateFlow<TrainingEntityUiState> by lazy {
        MutableStateFlow(
            TrainingEntityUiState()
        )
    }
    val state = _state.asStateFlow()

    init {
        try {
            repository.getChallengeById(id).also { challenge ->
                _state.update {
                    it.copy(
                        subtitle = "${challenge.instructorName}  |  ${challenge.workoutType.value}",
                        title = challenge.name,
                        description = challenge.description,
                        imageUrl = challenge.imageUrl,
                        tabs = challenge.weaklyPlans.map { weaklyPlan -> weaklyPlan.first },
                        weaklyPlans = challenge.weaklyPlans.map { weaklyPlan ->
                            mapOf(
                                Pair(
                                    weaklyPlan.first,
                                    weaklyPlan.second.map { workout ->
                                        ChallengeWorkoutItemUiState(
                                            id = workout.id,
                                            imageUrl = workout.imageUrl,
                                            title = workout.name,
                                            time = workout.duration,
                                            typeText = workout.intensity.level
                                        )
                                    })
                            )
                        }
                    )
                }
            }
        } catch (_: Throwable) {

        }
    }

    override fun onClickShowChallengeTabs() {
        _state.update { value ->
            value.copy(
                isChallengeTabsVisible = !value.isChallengeTabsVisible
            )
        }
    }
}