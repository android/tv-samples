package com.google.jetfit.data.repository.instructor

import com.google.jetfit.data.entities.Subscription
import com.google.jetfit.data.repository.sessions.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InstructorRepositoryImpl @Inject constructor(
    private val sessionRepository: SessionRepository
):InstructorRepository{
    override suspend fun getInstructors(): List<String> {
        return sessionRepository.getSessions().map { it.instructor }
    }

    override suspend fun getInstructorImageById(instructorId: String): String {
        return sessionRepository.getSessions().find { it.id == instructorId }?.imageUrl ?: ""
    }

    override fun getSubscriptionOptionsByInstructorId(instructorId: String): Flow<List<Subscription>> {
        return flow {
            emit(
                listOf(
                    Subscription(
                        periodTime = "1",
                        price = "$7.99",
                    ),
                    Subscription(
                        periodTime = "3",
                        price = "$19.99",
                    ),
                    Subscription(
                        periodTime = "12",
                        price = "$79.99",
                    ),
                )
            )
        }
    }
}