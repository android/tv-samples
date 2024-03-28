package com.google.jetfit.presentation.screens.training.training_entities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetfit.data.repositories.JetFitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingEntityViewModel @Inject constructor(
    private val repository: JetFitRepository
) : ViewModel() {
    val id: String = "1"

    private val _state: MutableStateFlow<TrainingEntityUiState> by lazy {
        MutableStateFlow(
            TrainingEntityUiState()
        )
    }
    val state = _state.asStateFlow()

    init {
        challengesById()
    }

    private fun challengesById() {
        viewModelScope.launch {
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
        }
    }
}