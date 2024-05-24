package com.google.jetfit.presentation.screens.training

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
class TrainingViewModel  @Inject constructor(
    private val jetFitRepository: JetFitRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TrainingUiState())
    val state = _state.asStateFlow()
    init{
        getInstructors()
    }

    private fun getInstructors() {
        viewModelScope.launch {
            val result =jetFitRepository.getInstructors()
            _state.update { it.copy(instructorList = result,filterSideMenuUiState= it.filterSideMenuUiState.copy(instructor = result.first()) ) }
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
}