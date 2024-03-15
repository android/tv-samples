package com.google.jetfit.presentation.screens.challenge

interface ChallengeInteractionListener {
    fun onDownPressed()
    fun onUpPressed()
    fun onTabChanged(newIndex: Int)
    fun onWorkoutClicked(id: String)
    fun onStartSessionClicked()
    fun onAddToFavoritesClicked()
}