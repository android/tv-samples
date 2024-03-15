package com.google.jetfit.presentation.screens.series

interface SeriesUiEffect {
    data class NavigateToVideoPlayer(val id: String) : SeriesUiEffect
}
