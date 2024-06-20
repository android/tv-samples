package com.google.jetfit.data.repository.training

import com.google.jetfit.data.entities.Training
import com.google.jetfit.data.entities.TrainingDetails
import kotlinx.coroutines.flow.Flow

interface TrainingRepository {

    suspend fun getTrainingsRecommended(): List<Training>
    fun getTrainingById(id: Int): Flow<TrainingDetails>
}