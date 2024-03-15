package com.google.jetfit.presentation.screens.workout

interface WorkoutUiEffect {
    data class NavigateToVideoPlayer(val id: String) : WorkoutUiEffect
}