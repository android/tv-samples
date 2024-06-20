package com.google.jetfit.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    val id: String,
    val name: String,
)
