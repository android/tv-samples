package com.google.jetfit.data.repository.user

import com.google.jetfit.data.entities.Profile

interface UserRepository {
    suspend fun getUserProfiles(): List<Profile>

}