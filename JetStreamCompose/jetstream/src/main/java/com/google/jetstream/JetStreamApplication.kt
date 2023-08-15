package com.google.jetstream

import android.app.Application
import com.google.jetstream.data.repositories.MovieRepository
import com.google.jetstream.data.repositories.MovieRepositoryImpl
import com.google.jetstream.data.util.AssetsReader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp
class JetStreamApplication : Application()

@InstallIn(SingletonComponent::class)
@Module
object MovieRepositoryModule {

    @Provides
    fun provideMovieRepository(assetsReader: AssetsReader): MovieRepository {
        return MovieRepositoryImpl(assetsReader)
    }

}
