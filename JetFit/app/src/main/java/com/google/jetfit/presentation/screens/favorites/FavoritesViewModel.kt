package com.google.jetfit.presentation.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetfit.data.entities.FavList
import com.google.jetfit.data.entities.FavWorkout
import com.google.jetfit.data.repository.workout.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    workoutRepository: WorkoutRepository
) : ViewModel() {

    private val selectedWorkoutItem: MutableStateFlow<FavWorkout?> = MutableStateFlow(null)
    val selectedWorkout = selectedWorkoutItem.asStateFlow()

    val uiState: StateFlow<FavoritesScreenUiState> = combine(
        workoutRepository.getFavoritesWorkouts()
    ) { favoritesWorkouts ->
        FavoritesScreenUiState.Ready(favoritesWorkouts.last())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FavoritesScreenUiState.Loading
    )

    fun onWorkoutSelect(favWorkout: FavWorkout) {
        this.selectedWorkoutItem.update {favWorkout}
    }

    fun onStartWorkout(id: String) {
        this.selectedWorkoutItem.update {null}
    }

    fun onRemoveWorkout(id: String) {
        this.selectedWorkoutItem.update {null}
    }

    fun onDismissRequest(){
        this.selectedWorkoutItem.update {FavWorkout()}
    }

}



sealed interface FavoritesScreenUiState {
    data object Loading: FavoritesScreenUiState
    data object Error: FavoritesScreenUiState
    data class Ready(
        val favoritesWorkouts: FavList,
    ): FavoritesScreenUiState

}