package com.google.jetfit.data.repository.routine

import com.google.jetfit.data.entities.Routine

interface RoutineRepository {

    fun getRoutines(): List<Routine>
    fun getRoutineById(id: String): Routine
}