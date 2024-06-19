package com.google.jetfit.data.repository.sessions

import com.google.jetfit.data.entities.Category
import com.google.jetfit.data.entities.Session

interface SessionRepository {

    suspend fun getSessions(): List<Session>
    suspend fun getCategories(): List<Category>
}