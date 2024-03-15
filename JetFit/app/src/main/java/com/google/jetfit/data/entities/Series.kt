package com.google.jetfit.data.entities

import java.util.Date

data class Series(
    val id: String,
    val name: String,
    val description: String,
    val instructorName: String,
    val workoutType: WorkoutType,
    val imageUrl: String,
    val numberOfWeeks: Long,
    val numberOfClasses: Int,
    val minutesPerDay: Int,
    val videoUrl: String,
    val intensity: Intensity,
    val releasedDate: Date,
    val language: Language,
    val subtitleLanguage: SubtitleLanguage
)
