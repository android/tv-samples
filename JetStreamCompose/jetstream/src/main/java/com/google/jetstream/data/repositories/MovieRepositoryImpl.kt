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
import com.google.jetstream.data.entities.MovieCast
import com.google.jetstream.data.entities.MovieCategory
import com.google.jetstream.data.entities.MovieCategoryDetails
import com.google.jetstream.data.entities.MovieCategoryList
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.data.entities.MovieList
import com.google.jetstream.data.entities.MovieReviewsAndRatings
import com.google.jetstream.data.models.MovieCastResponse
import com.google.jetstream.data.models.MovieCategoriesResponse
import com.google.jetstream.data.models.MoviesResponse
import com.google.jetstream.data.util.AssetsReader
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.data.util.StringConstants.Movie.Reviewer.DefaultCount
import com.google.jetstream.data.util.StringConstants.Movie.Reviewer.DefaultRating
import com.google.jetstream.data.util.StringConstants.Movie.Reviewer.FreshTomatoes
import com.google.jetstream.data.util.StringConstants.Movie.Reviewer.ReviewerName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(assetsReader: AssetsReader) : MovieRepository {

    private val mostPopularMovies = assetsReader
        .readJsonFile<MoviesResponse>(fileName = StringConstants.Assets.MostPopularMovies)
        ?: emptyList()

    private val inTheatersMovies = assetsReader
        .readJsonFile<MoviesResponse>(fileName = StringConstants.Assets.InTheaters)
        ?: emptyList()

    private val mostPopularTVShows = assetsReader
        .readJsonFile<MoviesResponse>(fileName = StringConstants.Assets.MostPopularTVShows)
        ?: emptyList()

    private val movieCastContainer = assetsReader
        .readJsonFile<MovieCastResponse>(fileName = StringConstants.Assets.MovieCast)
        ?: emptyList()

    private val top250Movies: MovieList
    private val top250MoviesWithWideThumbnail: MovieList
    private val categoryList: MovieCategoryList

    init {
        val movieList = assetsReader
            .readJsonFile<MoviesResponse>(fileName = StringConstants.Assets.Top250Movies)
            ?: emptyList()

        top250Movies = MovieList(
            value = movieList.map {
                Movie(
                    id = it.id,
                    posterUri = it.image_2_3,
                    name = it.title,
                    description = it.fullTitle
                )
            }
        )
        top250MoviesWithWideThumbnail = MovieList(
            value = movieList.map {
                Movie(
                    id = it.id,
                    posterUri = it.image_16_9,
                    name = it.title,
                    description = it.fullTitle
                )
            }
        )
        val movieCategory = assetsReader
            .readJsonFile<MovieCategoriesResponse>(fileName = StringConstants.Assets.MovieCategories)
            ?: emptyList()
        categoryList = MovieCategoryList(
            value = movieCategory.map {
                MovieCategory(
                    id = it.id,
                    name = it.name
                )
            }
        )

    }

    override fun getFeaturedMovies(): Flow<MovieList> = flow {
        val list = top250MoviesWithWideThumbnail
            .filterIndexed { index, _ -> listOf(1, 3, 5, 7, 9).contains(index) }
        emit(MovieList(value = list))
    }

    override fun getTrendingMovies(): Flow<MovieList> = flow {
        val list = mostPopularMovies.subList(fromIndex = 0, toIndex = 10).map {
            Movie(
                id = it.id,
                description = it.fullTitle,
                name = it.title,
                posterUri = it.image_2_3
            )
        }
        emit(MovieList(value = list))
    }

    override fun getTop10Movies(): Flow<MovieList> = flow {
        val list = top250MoviesWithWideThumbnail.subList(fromIndex = 20, toIndex = 30)
        emit(MovieList(value = list))
    }

    override fun getNowPlayingMovies(): Flow<MovieList> = flow {
        val list = inTheatersMovies
            .map {
                Movie(
                    id = it.id,
                    posterUri = it.image_2_3,
                    name = it.title,
                    description = it.fullTitle
                )
            }
            .subList(fromIndex = 0, toIndex = 10)
        emit(MovieList(value = list))
    }

    override fun getMovieCategories(): Flow<MovieCategoryList> = flow {
        emit(categoryList)
    }

    override suspend fun getMovieCategoryDetails(categoryId: String): MovieCategoryDetails {
        val category = categoryList.find { it.id == categoryId } ?: categoryList.first()
        return MovieCategoryDetails(
            id = category.id,
            name = category.name,
            movies = top250Movies
                .shuffled()
                .subList(fromIndex = 0, toIndex = 20)
        )
    }

    override suspend fun getMovieDetails(movieId: String): MovieDetails {
        val movie = top250Movies.find { it.id == movieId } ?: top250Movies.first()
        return MovieDetails(
            id = movie.id,
            posterUri = movie.posterUri,
            name = movie.name,
            description = movie.description,
            pgRating = "PG-13",
            releaseDate = "2021 (US)",
            categories = listOf("Action", "Adventure", "Fantasy", "Comedy"),
            duration = "1h 59m",
            director = "Larry Page",
            screenplay = "Sundai Pichai",
            music = "Sergey Brin",
            castAndCrew = movieCastContainer.map {
                MovieCast(
                    id = it.id,
                    characterName = it.characterName,
                    realName = it.realName,
                    avatarUrl = it.avatarUrl
                )
            },
            status = "Released",
            originalLanguage = "English",
            budget = "$15M",
            revenue = "$40M",
            similarMovies = (1..2).map { top250Movies.random() },
            reviewsAndRatings = listOf(
                MovieReviewsAndRatings(
                    reviewerName = FreshTomatoes,
                    reviewerIconUri = StringConstants.Movie.Reviewer.FreshTomatoesImageUrl,
                    reviewCount = "22",
                    reviewRating = "89%"
                ),
                MovieReviewsAndRatings(
                    reviewerName = ReviewerName,
                    reviewerIconUri = StringConstants.Movie.Reviewer.ImageUrl,
                    reviewCount = DefaultCount,
                    reviewRating = DefaultRating
                ),
            )
        )
    }

    override suspend fun searchMovies(query: String): MovieList {
        val list = top250Movies
            .filter { it.name.contains(other = query, ignoreCase = true) }
        return MovieList(value = list)
    }

    override fun getMoviesWithLongThumbnail(): Flow<MovieList> = flow {
        emit(top250MoviesWithWideThumbnail)
    }

    override fun getMovies(): Flow<MovieList> = flow {
        emit(top250Movies)
    }

    override fun getPopularFilmsThisWeek(): Flow<MovieList> = flow {
        val list = mostPopularMovies.subList(fromIndex = 11, toIndex = 20).map {
            Movie(
                id = it.id,
                posterUri = it.image_2_3,
                name = it.title,
                description = it.fullTitle
            )
        }
        emit(MovieList(value = list))
    }

    override fun getTVShows(): Flow<MovieList> = flow {
        val list = mostPopularTVShows
            .subList(fromIndex = 0, toIndex = 5)
            .map {
                Movie(
                    id = it.id,
                    posterUri = it.image_16_9,
                    name = it.title,
                    description = it.fullTitle
                )
            }
        emit(MovieList(value = list))
    }

    override fun getBingeWatchDramas(): Flow<MovieList> = flow {
        val list = mostPopularTVShows
            .subList(fromIndex = 6, toIndex = 15)
            .map {
                Movie(
                    id = it.id,
                    posterUri = it.image_2_3,
                    name = it.title,
                    description = it.fullTitle
                )
            }
        emit(MovieList(value = list))
    }

    override fun getFavouriteMovies(): Flow<MovieList> = flow {
        val list = top250Movies
            .subList(fromIndex = 0, toIndex = 28)
        emit(MovieList(value = list))
    }

}
