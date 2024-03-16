package com.google.jetfit.data.repositories

import com.google.jetfit.data.entities.Song
import com.google.jetfit.data.entities.Workout

import com.google.jetfit.data.entities.Category
import com.google.jetfit.data.entities.Session
import com.google.jetfit.data.entities.Training

interface JetFitRepository {
    fun getWorkouts()
    fun getWorkoutById(id: String): Workout
    fun getSongById(id: String): Song
    fun getWorkoutById()
    suspend fun getSessions(): List<Session>
    suspend fun getCategories(): List<Category>
    suspend fun getTrainingsRecommended(): List<Training>

}