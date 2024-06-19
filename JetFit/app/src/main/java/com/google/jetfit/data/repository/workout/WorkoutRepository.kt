package com.google.jetfit.data.repository.workout

import com.google.jetfit.data.entities.FavList
import com.google.jetfit.data.entities.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getWorkouts(): List<Workout>

    fun getWorkoutById(id: String): Workout

    fun getFavoritesWorkouts(): Flow<FavList>
}