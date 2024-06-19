package com.google.jetfit.data.repository.challenges

import com.google.jetfit.data.entities.Challenge

interface ChallengesRepository {
    fun getChallenges(): List<Challenge>
    fun getChallengeById(id: String): Challenge
}