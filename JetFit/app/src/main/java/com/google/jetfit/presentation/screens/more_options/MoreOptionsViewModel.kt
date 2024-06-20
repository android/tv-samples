package com.google.jetfit.presentation.screens.more_options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetfit.data.repository.training.TrainingRepository
import com.google.jetfit.data.util.Result
import com.google.jetfit.data.util.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MoreOptionsViewModel @Inject constructor(
    trainingRepository: TrainingRepository
) :
    ViewModel() {

    val uiState: StateFlow<MoreOptionsUiState> =
        trainingRepository.getTrainingById(1)
            .asResult()
            .map {
                when (it) {
                    is Result.Loading -> MoreOptionsUiState.Loading
                    is Result.Error -> MoreOptionsUiState.Error
                    is Result.Success -> {
                        MoreOptionsUiState.Ready(
                            trainingDetails = it.data
                        )
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MoreOptionsUiState.Loading,
            )

}