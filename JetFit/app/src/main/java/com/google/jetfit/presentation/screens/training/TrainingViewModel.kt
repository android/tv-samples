package com.google.jetfit.presentation.screens.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetfit.data.repository.instructor.InstructorRepository
import com.google.jetfit.data.repository.workout.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel  @Inject constructor(
    private val instructorRepository: InstructorRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TrainingUiState())
    val state = _state.asStateFlow()
    init{
        getInstructors()
        getWorkout()
    }

    private fun getInstructors() {
        viewModelScope.launch {
            val result =instructorRepository.getInstructors()
            _state.update { it.copy(instructorList = result,filterSideMenuUiState= it.filterSideMenuUiState.copy(instructor = result.first()) ) }
        }
    }

    private fun getWorkout() {
        viewModelScope.launch {
            val result = workoutRepository.getWorkouts()
            _state.update {
                it.copy(
                    workouts = result
                )
            }
        }
    }

    fun onFilterClicked() {
        _state.update { it.copy(isFilterExpended = !it.isFilterExpended)}
    }
  fun onSortedClicked() {
        _state.update { it.copy(isSortExpended = !it.isSortExpended)}
    }

    fun onDismissSideMenu() {
        _state.update { it.copy(isFilterExpended = false, isSortExpended = false)}
    }

    fun onSelectedSortedItem(sortItemIndex: Int) {
        _state.update { it.copy(selectedSortItem = sortItemIndex)}
    }

    fun onChangeSelectedTab(index: Int) {
        _state.update { it.copy(selectedTab = index) }
    }

    fun onChangeFocusTab(index: Int) {
        _state.update { it.copy(focusTabIndex = index) }
    }
}