package com.google.jetfit.presentation.screens.training.series

import androidx.lifecycle.ViewModel
import com.google.jetfit.data.repositories.JetFitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SeriesViewModel @Inject constructor(
    repository: JetFitRepository
) : ViewModel() {
    val id: String = "1"

    private val _state: MutableStateFlow<SeriesUiState> by lazy {
        MutableStateFlow(
            SeriesUiState()
        )
    }
    val state = _state.asStateFlow()

    init {
        try {
            repository.getSeriesById(id).also { series ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        subtitle = "${series.instructorName}  |  ${series.intensity.value}",
                        title = series.name,
                        description = series.description,
                        intensity = series.intensity.value,
                        numberOfClasses = series.numberOfClasses.toString(),
                        numberOfWeeks = series.numberOfWeeks.toString(),
                        minutesPerDay = series.minutesPerDay.toString(),
                        imageUrl = series.imageUrl
                    )
                }
            }
        } catch (e: Throwable) {
            _state.update { it.copy(isLoading = false, error = e.message.toString()) }
        }
    }
}