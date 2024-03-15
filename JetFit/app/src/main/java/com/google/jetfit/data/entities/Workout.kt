package com.google.jetfit.data.entities

data class Workout(
    val id: String,
    val title: String,
    val videoUrl: String,
    val instructor: String,
    val subtitles: String?,
    val subtitleUri: String?,
)