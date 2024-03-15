package com.google.jetfit.presentation.screens.routine

interface RoutineUiEffect {
    data class NavigateToVideoPlayer(val id: String) : RoutineUiEffect
}