/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.data.repositories

import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.entities.MovieCategory
import com.google.jetstream.data.entities.MovieCategoryDetails
import com.google.jetstream.data.entities.MovieDetails

interface MovieRepository {
    fun getFeaturedMovies(): List<Movie>
    fun getTrendingMovies(): List<Movie>
    fun getTop10Movies(): List<Movie>
    fun getNowPlayingMovies(): List<Movie>
    fun getMovieCategories(): List<MovieCategory>
    fun getMovieCategoryDetails(categoryId: String): MovieCategoryDetails
    fun getMovieDetails(movieId: String): MovieDetails
    fun searchMovies(query: String): List<Movie>
    fun getMovies_16_9(): List<Movie>
    fun getMovies_2_3(): List<Movie>
    fun getPopularFilmsThisWeek(): List<Movie>
    fun getTVShows(): List<Movie>
    fun getBingeWatchDramas(): List<Movie>
    fun getFavouriteMovies(): List<Movie>
}
