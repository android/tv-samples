/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream

import android.app.Application
import com.google.jetstream.data.repositories.MovieCastDataSource
import com.google.jetstream.data.repositories.MovieCategoryDataSource
import com.google.jetstream.data.repositories.MovieDataSource
import com.google.jetstream.data.repositories.MovieRepository
import com.google.jetstream.data.repositories.MovieRepositoryImpl
import com.google.jetstream.data.repositories.TvDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class JetStreamApplication : Application()

@InstallIn(SingletonComponent::class)
@Module
object MovieRepositoryModule {

    @Singleton
    @Provides
    fun provideMovieRepository(
        movieDataSource: MovieDataSource,
        tvDataSource: TvDataSource,
        movieCastDataSource: MovieCastDataSource,
        movieCategoryDataSource: MovieCategoryDataSource,
    ): MovieRepository {
        return MovieRepositoryImpl(
            movieDataSource,
            tvDataSource,
            movieCastDataSource,
            movieCategoryDataSource
        )
    }

}
