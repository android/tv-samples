package com.google.jetfit.presentation.screens.player.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetfit.data.repositories.JetFitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val jetFitRepository: JetFitRepository
) : ViewModel() {
    private val _state = MutableStateFlow(VideoPlayerUiState())
    val state get() = _state.asStateFlow()

    init {
        getWorkoutById()
    }

    private fun getWorkoutById() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val workout = jetFitRepository.getWorkoutById("123456sdasdsa")
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        workoutUiState = WorkoutUiState(
                            title = workout.title,
                            instructor = workout.instructor,
                            videoUrl = workout.videoUrl,
                            id = workout.id,
                            subtitles = workout.subtitles,
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