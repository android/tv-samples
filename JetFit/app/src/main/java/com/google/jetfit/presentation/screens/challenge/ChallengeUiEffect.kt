package com.google.jetfit.presentation.screens.challenge

import com.google.jetfit.presentation.screens.routine.RoutineUiEffect

interface ChallengeUiEffect {
    data class NavigateToVideoPlayer(val id: String) : RoutineUiEffect
}