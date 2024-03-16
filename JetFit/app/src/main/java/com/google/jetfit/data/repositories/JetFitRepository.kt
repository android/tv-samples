package com.google.jetfit.data.repositories

import com.google.jetfit.data.entities.Category
import com.google.jetfit.data.entities.Session
import com.google.jetfit.data.entities.Training

interface JetFitRepository {
    fun getWorkouts()
    fun getWorkoutById()
    suspend fun getSessions(): List<Session>
    suspend fun getCategories(): List<Category>
    suspend fun getTrainingsRecommended(): List<Training>

}