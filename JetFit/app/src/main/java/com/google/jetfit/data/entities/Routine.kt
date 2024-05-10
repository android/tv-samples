package com.google.jetfit.data.entities

import java.util.Date

data class Routine (
    val id: String,
    val name: String,
    val description: String,
    val instructorName: String,
    val workoutType: WorkoutType,
    val imageUrl: String,
    val duration: Long,
    val videoUrl: String,
    val intensity: Intensity,
    val releasedDate: Date,
    val language: Language,
    val subtitleLanguage: SubtitleLanguage
)