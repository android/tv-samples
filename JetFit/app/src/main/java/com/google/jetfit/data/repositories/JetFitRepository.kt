package com.google.jetfit.data.repositories

import com.google.jetfit.data.entities.Workout

interface JetFitRepository {
    fun getWorkouts()
    fun getWorkoutById(id: String): Workout

}