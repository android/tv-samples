package com.google.jetfit.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetfit.data.repository.sessions.SessionRepository
import com.google.jetfit.data.repository.training.TrainingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val trainingRepository: TrainingRepository
) : ViewModel() {
    private val _state: MutableStateFlow<HomeUiState> by lazy { MutableStateFlow(HomeUiState()) }
    val state = _state.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    sessions = sessionRepository.getSessions(),
                    categories = sessionRepository.getCategories(),
                    recommended = trainingRepository.getTrainingsRecommended()
                )
            }
        }
    }

}