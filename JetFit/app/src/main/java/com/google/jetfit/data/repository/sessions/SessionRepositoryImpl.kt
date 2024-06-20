package com.google.jetfit.data.repository.sessions

import com.google.jetfit.data.entities.Category
import com.google.jetfit.data.entities.Session
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
):SessionRepository
{
    override suspend fun getSessions(): List<Session> {
        return listOf(
            Session(
                id = "1",
                instructor = "Danielle Orlando",
                title = "Strengthen & lengthen pilates",
                description = "This pilates workout is perfect for good balance between your overall strength and flexibility. Use your own body weight to strengthen and sculpt our muscles",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/8e91aafe-52be-41fd-8869-977ca0410ab5"
            ),
            Session(
                id = "2",
                instructor = "John Smith",
                title = "Yoga Flow",
                description = "Join us for a relaxing yoga flow session to unwind and improve flexibility. Suitable for all levels.",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/41277637-6cc9-4bcd-9a1f-b4b12d501dc8"
            ),
            Session(
                id = "3",
                instructor = "Emily Johnson",
                title = "HIIT Workout",
                description = "Get ready to sweat with our high-intensity interval training (HIIT) session. Burn calories, build strength, and improve endurance!",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/b1773e6c-4a50-4e45-a8f2-5ff176e8167a",
            ),
            Session(
                id = "4",
                instructor = "Michael Thompson",
                title = "Boxing Bootcamp",
                description = "Punch and kick your way to fitness with our boxing bootcamp! Improve your coordination, agility, and strength while having fun.",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/7927e3f0-bb09-4309-a11c-0748cd39b535"
            ),
        )

    }

    override suspend fun getCategories(): List<Category> {
        return listOf(
            Category(
                id = "1",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/59895284/0369bc67-d8c5-4957-a6ef-4027f06a1f96",
                description = "There are many benefits to yoga and Pilates, including increased...",
                title = "Yoga & Pilates",
            ),
            Category(
                id = "2",
                imageUrl ="https://github.com/TheChance101/tv-samples/assets/59895284/22b516d3-2925-495a-8e6c-25550f02b679",
                title = "Strength training",
                description = "Strength training makes you stronger, helps you control your..."
            ),
            Category(
                id = "3",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/e83a8d1e-5d78-4ad2-8a42-341251d77e79",
                title = "Aerobic & Cardio",
                description = "Get your blood pumping and build up your endurance with some..."
            ),
        )
    }
}