package com.google.jetfit.presentation.screens.series

import com.google.jetfit.data.entities.Series

data class SeriesUiState(
    val subtitle: String = "",
    val title: String = "",
    val description: String = "",
    val duration: String = "",
    val intensity: String = "",
    val numberOfWeeks: String = "",
    val numberOfClasses: String = "",
    val minutesPerDay: String = "",
    val posterUrl: String = ""
)

fun Series.toUiState(): SeriesUiState = SeriesUiState(
    subtitle = "$instructorName  |  ${workoutType.value}",
    title = name,
    description = description,
    intensity = intensity.value,
    numberOfClasses = numberOfClasses.toString(),
    numberOfWeeks = numberOfWeeks.toString(),
    minutesPerDay = minutesPerDay.toString(),
    posterUrl = imageUrl
)