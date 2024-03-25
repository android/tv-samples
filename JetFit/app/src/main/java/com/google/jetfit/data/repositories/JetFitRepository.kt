package com.google.jetfit.data.repositories

import com.google.jetfit.data.entities.Category
import com.google.jetfit.data.entities.Profile
import com.google.jetfit.data.entities.Session
import com.google.jetfit.data.entities.Song
import com.google.jetfit.data.entities.Training
import com.google.jetfit.data.entities.TrainingE
import kotlinx.coroutines.flow.Flow
import com.google.jetfit.data.entities.Workout

import com.google.jetfit.data.entities.FavList
import kotlinx.coroutines.flow.Flow

interface JetFitRepository {
    fun getWorkouts()
    suspend fun getInstructors():List<String>
    fun getWorkoutById(id: String): Workout
    fun getSongById(id: String): Song
    fun getWorkoutById()
    suspend fun getSessions(): List<Session>
    suspend fun getCategories(): List<Category>
    suspend fun getTrainingsRecommended(): List<Training>
    fun getTrainingById(id: Int): Flow<TrainingE>
    suspend fun getUserProfiles(): List<Profile>

    fun getFavoritesWorkouts(): Flow<FavList>

}