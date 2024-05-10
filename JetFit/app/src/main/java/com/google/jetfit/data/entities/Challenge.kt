package com.google.jetfit.data.entities

import java.util.Date

data class Challenge(
    val id: String,
    val name: String,
    val description: String,
    val instructorName: String,
    val workoutType: WorkoutType,
    val imageUrl: String,
    val minutesPerDay: Int,
    val numberOfDays: Int,
    val weaklyPlans: List<Pair<String, List<Workout>>>,
    val intensity: Intensity,
    val releasedDate: Date,
    val language: Language,
    val subtitleLanguage: SubtitleLanguage
)