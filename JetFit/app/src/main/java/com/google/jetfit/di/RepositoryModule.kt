package com.google.jetfit.di

import com.google.jetfit.data.repositories.JetFitImpl
import com.google.jetfit.data.repositories.JetFitRepository
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
    abstract fun bindJetFitRepository(
        jetFitImpl: JetFitImpl
    ): JetFitRepository
}