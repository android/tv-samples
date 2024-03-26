package com.google.jetfit.data.repositories

import com.google.jetfit.data.entities.FavWorkout
import kotlin.random.Random

class DummyWorkoutData {
    private fun generateRandomImage(): String {
        val images = listOf(
            "https://c4.wallpaperflare.com/wallpaper/714/288/863/muscles-gym-dumbbells-bodybuilder-wallpaper-preview.jpg",
            "https://c4.wallpaperflare.com/wallpaper/853/255/783/dwayne-johnson-dwayne-johnson-wallpaper-preview.jpg",
            "https://c4.wallpaperflare.com/wallpaper/599/689/236/machine-dwayne-johnson-the-rock-workout-wallpaper-preview.jpg",
            "https://c4.wallpaperflare.com/wallpaper/81/470/394/men-weightlifting-gyms-bodybuilder-wallpaper-preview.jpg",
            "https://c4.wallpaperflare.com/wallpaper/574/901/119/pose-muscle-muscle-athlete-simulators-hd-wallpaper-preview.jpg",
            "https://c4.wallpaperflare.com/wallpaper/322/962/354/muscle-rod-pose-training-athlete-hd-wallpaper-preview.jpg",
            "https://c4.wallpaperflare.com/wallpaper/322/962/354/muscle-rod-pose-training-athlete-hd-wallpaper-preview.jpg",
        )
        return images.random()
    }

    private fun generateDuration() = Random.nextInt(20, 60)
    private fun generateIntensity() = Random.nextInt(1, 6)

    private fun generateRandomWorkoutName(): String {
        val workoutNames = listOf(
            "Cardio Blast",
            "Strength Training",
            "HIIT Circuit",
            "Yoga Flow",
            "Pilates Fusion",
            "Core Crusher",
            "Flexibility Focus",
            "Endurance Challenge"
        )
        return workoutNames.random()
    }

    private fun generateRandomDescription(): String {
        val descriptions = listOf(
            "Get ready to sweat and feel the burn with this intense workout!",
            "Improve your strength, endurance, and flexibility with this challenging routine.",
            "Join us for a high-energy workout that will leave you feeling invigorated!",
            "This workout combines cardio and strength exercises for a full-body burn.",
            "Take your fitness to the next level with this dynamic workout.",
            "Join our expert trainers for a fun and effective workout that will help you reach your goals.",
            "Challenge yourself with this heart-pumping workout designed to push your limits.",
            "Transform your body and mind with this empowering workout."
        )
        return descriptions.random()
    }


    val list = listOf(
        FavWorkout(
            id = "01",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "02",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "03",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "04",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "05",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "06",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "07",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "08",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "09",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "10",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "11",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "12",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "13",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "14",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "15",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "16",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "17",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
        FavWorkout(
            id = "18",
            name = generateRandomWorkoutName(),
            image = generateRandomImage(),
            duration = "${generateDuration()} Min",
            intensity = generateIntensity(),
            description = generateRandomDescription()
        ),
    )
}