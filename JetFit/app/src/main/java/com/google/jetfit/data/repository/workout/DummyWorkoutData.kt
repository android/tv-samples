package com.google.jetfit.data.repository.workout

import com.google.jetfit.data.entities.FavWorkout
import kotlin.random.Random

class DummyWorkoutData {
    private fun generateRandomImage(): String {
        val images = listOf(
            "https://github.com/TheChance101/tv-samples/assets/45900975/b1773e6c-4a50-4e45-a8f2-5ff176e8167a",
            "https://github.com/TheChance101/tv-samples/assets/45900975/7927e3f0-bb09-4309-a11c-0748cd39b535",
            "https://github.com/TheChance101/tv-samples/assets/45900975/194152ea-d535-4504-bcae-17007d47e20a",
            "https://github.com/TheChance101/tv-samples/assets/45900975/f086b517-9c6a-44fa-9924-a6a5c9bde386",
            "https://github.com/TheChance101/tv-samples/assets/45900975/8e91aafe-52be-41fd-8869-977ca0410ab5",
            "https://github.com/TheChance101/tv-samples/assets/45900975/41277637-6cc9-4bcd-9a1f-b4b12d501dc8",
            "https://github.com/TheChance101/tv-samples/assets/45900975/e83a8d1e-5d78-4ad2-8a42-341251d77e79",
            "https://github.com/TheChance101/tv-samples/assets/45900975/7236d183-0be8-4b4a-b6c9-e628e67c9bc3",
            "https://github.com/TheChance101/tv-samples/assets/45900975/2f0c3c1c-d40d-46c8-995d-3a524b521a8d",
            "https://github.com/TheChance101/tv-samples/assets/45900975/dd02be7b-b97b-42eb-b152-11a8579d3821",
            "https://github.com/TheChance101/tv-samples/assets/45900975/b1d53df4-2c05-4cb9-813f-0f42bcafa0d1",
            "https://github.com/TheChance101/tv-samples/assets/45900975/90c1f376-adca-4915-9fdf-4c871008a75b",
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