package com.google.jetfit.presentation.screens.trainingentities.routine

import androidx.lifecycle.ViewModel
import com.google.jetfit.data.repositories.JetFitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    repository: JetFitRepository
) : ViewModel() {
    val id: String = "1"

    private val _state: MutableStateFlow<RoutineUiState> by lazy {
        MutableStateFlow(
            RoutineUiState()
        )
    }
    val state = _state.asStateFlow()

    init {
        try {
            repository.getRoutineById(id).also { routine ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        subtitle = "${routine.instructorName}  |  ${routine.workoutType.value}",
                        title = routine.name,
                        description = routine.description,
                        duration = "${routine.duration} min",
                        intensity = routine.intensity.value,
                        imageUrl = routine.imageUrl
                    )
                }
            }
        } catch (e: Throwable) {
            _state.update { it.copy(isLoading = false, error = e.message.toString()) }
        }
    }
}