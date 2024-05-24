package com.google.jetfit.data.repositories

import com.google.jetfit.data.entities.Challenge
import com.google.jetfit.data.entities.Routine
import com.google.jetfit.data.entities.Series
import com.google.jetfit.data.entities.Category
import com.google.jetfit.data.entities.Profile
import com.google.jetfit.data.entities.Session
import com.google.jetfit.data.entities.Song
import com.google.jetfit.data.entities.Subscription
import com.google.jetfit.data.entities.Training
import com.google.jetfit.data.entities.TrainingDetails
import kotlinx.coroutines.flow.Flow
import com.google.jetfit.data.entities.Workout
import com.google.jetfit.data.entities.FavList

interface JetFitRepository {
    fun getWorkouts(): List<Workout>
    suspend fun getInstructors():List<String>
    fun getWorkoutById(id: String): Workout
    fun getSeries(): List<Series>
    fun getSeriesById(id: String): Series
    fun getRoutines(): List<Routine>
    fun getRoutineById(id: String): Routine
    fun getChallenges(): List<Challenge>
    fun getChallengeById(id: String): Challenge
    fun getSongById(id: String): Song
    suspend fun getSessions(): List<Session>
    suspend fun getCategories(): List<Category>
    suspend fun getTrainingsRecommended(): List<Training>
    fun getTrainingById(id: Int): Flow<TrainingDetails>
    suspend fun getUserProfiles(): List<Profile>

    fun getFavoritesWorkouts(): Flow<FavList>

    fun getSubscriptionOptionsByInstructorId(instructorId: String): Flow<List<Subscription>>
    suspend fun getInstructorImageById(instructorId: String): String

}