package com.google.jetfit.presentation.screens.training.workout

import androidx.lifecycle.ViewModel
import com.google.jetfit.data.repositories.JetFitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    repository: JetFitRepository,
) : ViewModel() {
    val id: String = "1"

    private val _state: MutableStateFlow<WorkoutUiState> by lazy {
        MutableStateFlow(
            WorkoutUiState()
        )
    }
    val state = _state.asStateFlow()

    init {
        try {
            repository.getWorkoutById(id).also { workout ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        subtitle = "${workout.instructorName}  |  ${workout.workoutType.value}",
                        title = workout.name,
                        description = workout.description,
                        duration = workout.duration.toString(),
                        intensity = workout.intensity.value,
                        imageUrl = workout.imageUrl
                    )
                }
            }
        } catch (e: Throwable) {
            _state.update { it.copy(isLoading = false, error = e.message.toString()) }
        }
    }
}