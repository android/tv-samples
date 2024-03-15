package com.google.jetfit.presentation.screens.series

import com.google.jetfit.data.entities.Series
import com.google.jetfit.data.repositories.JetFitRepository
import com.google.jetfit.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SeriesViewModel @Inject constructor(
    repository: JetFitRepository
) : BaseViewModel<SeriesUiState, SeriesUiEffect>(SeriesUiState()), SeriesInteractionListener {
    val id: String = "1"

    init {
        tryToExecute({repository.getSeriesById(id)}, ::onGettingSeriesSuccess, ::onError)
    }

    private fun onGettingSeriesSuccess(series: Series) {
        updateState(series.toUiState())
    }

    private fun onError(throwable: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onStartProgramClicked() {
        sendNewEffect(SeriesUiEffect.NavigateToVideoPlayer(id))
    }

    override fun onRecommendScheduleClicked() {
        TODO("Not yet implemented")
    }
}