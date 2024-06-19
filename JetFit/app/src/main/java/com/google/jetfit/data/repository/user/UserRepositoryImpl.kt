package com.google.jetfit.data.repository.user

import com.google.jetfit.data.entities.Profile
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(

):UserRepository {
    override suspend fun getUserProfiles(): List<Profile> {
        return listOf(
            Profile(
                id = "1",
                name = "Liam",
                avatar = "https://github.com/TheChance101/tv-samples/assets/59895284/4fd7ab6d-02d1-4f0c-b118-ffff98e71f3a"
            ),
            Profile(
                id = "2",
                name = "Olivia",
                avatar = "https://github.com/TheChance101/tv-samples/assets/59895284/3cfb4d9c-55d6-4568-961c-fb1dae786814"
            ),
            Profile(
                id = "3",
                name = "Noah",
                avatar = "https://github.com/TheChance101/tv-samples/assets/59895284/a0f931d1-e29f-41e9-8e03-b70ad8e479df"
            ),
        )
    }

}