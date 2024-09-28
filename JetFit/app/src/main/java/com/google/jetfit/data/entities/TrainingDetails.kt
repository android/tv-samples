package com.google.jetfit.data.entities

data class TrainingDetails(
    val id: String,
    val instructor: String,
    val type: String,
    val title: String,
    val time: String,
    val imageUrl: String,
    val description: String
)