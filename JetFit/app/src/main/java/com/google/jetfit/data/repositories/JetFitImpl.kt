package com.google.jetfit.data.repositories

import com.google.jetfit.data.entities.Workout
import javax.inject.Inject

class JetFitImpl @Inject constructor() : JetFitRepository {
    override fun getWorkouts() {
        TODO("Not yet implemented")
    }

    override fun getWorkoutById(id: String): Workout {
        return Workout(
            id = "123456sdasdsa",
            title = "Battle ropes HIIT",
            instructor = "Hugo Wright",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            subtitles = null,
            subtitleUri = null
        )
    }
}