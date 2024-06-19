package com.google.jetfit.presentation.screens.player.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetfit.data.repository.workout.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    private val _state = MutableStateFlow(VideoPlayerUiState())
    val state get() = _state.asStateFlow()

    init {
        getWorkoutById()
    }

    private fun getWorkoutById() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val workout = workoutRepository.getWorkoutById("1")
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        workoutUiState = WorkoutUiState(
                            title = workout.name,
                            instructor = workout.instructorName,
                            videoUrl = workout.videoUrl,
                            id = workout.id,
                            subtitles = null,
                            subtitleUri = workout.subtitleUri,
                        )
                    )
                }
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(isLoading = false, error = e.message.toString())
            }
        }
    }
}