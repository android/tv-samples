package com.google.jetfit.presentation.screens.favorites

interface FavoritesInteraction {

    fun onStartWorkout(id: String)
    fun onRemoveWorkout(id: String)
}