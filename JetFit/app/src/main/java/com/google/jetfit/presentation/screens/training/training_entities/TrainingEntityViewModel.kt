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
    private val contentType: TrainingEntityUiState.ContentType =
        TrainingEntityUiState.ContentType.ROUTINE

    private val _state: MutableStateFlow<TrainingEntityUiState> by lazy {
        MutableStateFlow(
            TrainingEntityUiState()
        )
    }
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(contentType = contentType) }
        when (state.value.contentType) {
            TrainingEntityUiState.ContentType.CHALLENGES -> getChallengeById()
            TrainingEntityUiState.ContentType.SERIES -> getSeriesById()
            TrainingEntityUiState.ContentType.WORK_OUT -> getWorkoutById()
            TrainingEntityUiState.ContentType.ROUTINE -> getRoutineById()
        }

    }

    private fun getRoutineById() {
        viewModelScope.launch {
            repository.getRoutineById(id).also { routine ->
                _state.update {
                    it.copy(
                        subtitle = "${routine.instructorName}  |  ${routine.workoutType.value}",
                        title = routine.name,
                        description = routine.description,
                        itemsInfo = listOf(
                            TrainingEntityUiState.TrainingInfoItem(
                                "${routine.duration} min",
                                "Duration"
                            ),
                            TrainingEntityUiState.TrainingInfoItem(
                                routine.intensity.value,
                                "Intensity"
                            )
                        ),
                        imageUrl = routine.imageUrl
                    )
                }
            }
        }
    }

    private fun getWorkoutById() {
        viewModelScope.launch {
            repository.getWorkoutById(id).also { workout ->
                _state.update {
                    it.copy(
                        subtitle = "${workout.instructorName}  |  ${workout.workoutType.value}",
                        title = workout.name,
                        description = workout.description,
                        itemsInfo = listOf(
                            TrainingEntityUiState.TrainingInfoItem(
                                "${workout.duration} min",
                                "Duration"
                            ),
                            TrainingEntityUiState.TrainingInfoItem(
                                workout.intensity.value,
                                "Intensity"
                            )
                        ),
                        imageUrl = workout.imageUrl
                    )
                }
            }
        }
    }

    private fun getSeriesById() {
        viewModelScope.launch {
            repository.getSeriesById(id).also { series ->
                _state.update {
                    it.copy(
                        subtitle = "${series.instructorName}  |  ${series.intensity.value}",
                        title = series.name,
                        description = series.description,
                        itemsInfo = listOf(
                            TrainingEntityUiState.TrainingInfoItem(
                                series.numberOfWeeks.toString(),
                                "Week"
                            ),
                            TrainingEntityUiState.TrainingInfoItem(
                                series.numberOfClasses.toString(),
                                "Classes"
                            ),
                            TrainingEntityUiState.TrainingInfoItem(
                                series.intensity.value,
                                "Intensity"
                            ),
                            TrainingEntityUiState.TrainingInfoItem(
                                series.minutesPerDay.toString(),
                                "Minutes per day"
                            )
                        ),
                        imageUrl = series.imageUrl
                    )
                }
            }
        }
    }

    private fun getChallengeById() {
        viewModelScope.launch {
            repository.getChallengeById(id).also { challenge ->
                _state.update {
                    it.copy(
                        subtitle = "${challenge.instructorName}  |  ${challenge.workoutType.value}",
                        title = challenge.name,
                        description = challenge.description,
                        imageUrl = challenge.imageUrl,
                        tabs = challenge.weaklyPlans.map { weaklyPlan -> weaklyPlan.first },
                        itemsInfo = listOf(
                            TrainingEntityUiState.TrainingInfoItem(
                                challenge.numberOfDays.toString(),
                                "Days"
                            ),
                            TrainingEntityUiState.TrainingInfoItem(
                                challenge.intensity.value,
                                "Intensity"
                            ),
                            TrainingEntityUiState.TrainingInfoItem(
                                challenge.minutesPerDay.toString(),
                                "Minutes per day"
                            )
                        ),
                        weaklyPlans = challenge.weaklyPlans.map { weaklyPlan ->
                            mapOf(
                                Pair(
                                    weaklyPlan.first,
                                    weaklyPlan.second.map { workout ->
                                        TrainingEntityUiState.ChallengeWorkoutItemUiState(
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