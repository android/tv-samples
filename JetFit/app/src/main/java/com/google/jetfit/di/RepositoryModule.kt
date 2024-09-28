package com.google.jetfit.di

import com.google.jetfit.data.repository.Series.SeriesRepository
import com.google.jetfit.data.repository.Series.SeriesRepositoryImpl
import com.google.jetfit.data.repository.challenges.ChallengesRepository
import com.google.jetfit.data.repository.challenges.ChallengesRepositoryImpl
import com.google.jetfit.data.repository.instructor.InstructorRepository
import com.google.jetfit.data.repository.instructor.InstructorRepositoryImpl
import com.google.jetfit.data.repository.routine.RoutineRepository
import com.google.jetfit.data.repository.routine.RoutineRepositoryImpl
import com.google.jetfit.data.repository.sessions.SessionRepository
import com.google.jetfit.data.repository.sessions.SessionRepositoryImpl
import com.google.jetfit.data.repository.training.TrainingRepository
import com.google.jetfit.data.repository.training.TrainingRepositoryImpl
import com.google.jetfit.data.repository.user.UserRepository
import com.google.jetfit.data.repository.user.UserRepositoryImpl
import com.google.jetfit.data.repository.workout.WorkoutRepository
import com.google.jetfit.data.repository.workout.WorkoutRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Singleton
    @Binds
    abstract fun bindSessionRepository(
        sessionRepository: SessionRepositoryImpl
    ): SessionRepository

    @Singleton
    @Binds
    abstract fun bindSeriesRepository(
        seriesRepositoryImpl: SeriesRepositoryImpl
    ): SeriesRepository

    @Singleton
    @Binds
    abstract fun bindWorkoutRepository(
        workoutRepositoryImpl: WorkoutRepositoryImpl
    ): WorkoutRepository

    @Singleton
    @Binds
    abstract fun bindInstructorRepository(
        instructorRepositoryImpl: InstructorRepositoryImpl
    ): InstructorRepository


    @Binds
    @Singleton
    abstract fun bindChallengeRepository(
       challengesRepositoryImpl: ChallengesRepositoryImpl
    ): ChallengesRepository

    @Binds
    @Singleton
    abstract fun bindRoutineRepository(
        routineRepositoryImpl: RoutineRepositoryImpl
    ): RoutineRepository

    @Binds
    @Singleton
    abstract fun bindTrainingRepository(
        trainingRepositoryImpl: TrainingRepositoryImpl
    ): TrainingRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}