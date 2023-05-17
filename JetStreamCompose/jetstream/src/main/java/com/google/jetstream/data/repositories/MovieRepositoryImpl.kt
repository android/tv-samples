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
import com.google.jetstream.data.entities.MovieDetails
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

class MovieRepositoryImpl(val assetsReader: AssetsReader) : MovieRepository {
    private val _top250Movies = assetsReader
        .readJsonFile<MoviesResponse>(fileName = StringConstants.Assets.Top250Movies)
        ?: emptyList()

    private val _mostPopularMovies = assetsReader
        .readJsonFile<MoviesResponse>(fileName = StringConstants.Assets.MostPopularMovies)
        ?: emptyList()

    private val _inTheatersMovies = assetsReader
        .readJsonFile<MoviesResponse>(fileName = StringConstants.Assets.InTheaters)
        ?: emptyList()

    private val _mostPopularTVShows = assetsReader
        .readJsonFile<MoviesResponse>(fileName = StringConstants.Assets.MostPopularTVShows)
        ?: emptyList()

    private val _movieCategories = assetsReader
        .readJsonFile<MovieCategoriesResponse>(fileName = StringConstants.Assets.MovieCategories)
        ?: emptyList()

    private val _movieCastContainer = assetsReader
        .readJsonFile<MovieCastResponse>(fileName = StringConstants.Assets.MovieCast)
        ?: emptyList()

    override fun getFeaturedMovies(): List<Movie> {
        return _top250Movies
            .filterIndexed { index, _ -> listOf(1, 3, 5, 7, 9).contains(index) }
            .map {
                Movie(
                    id = it.id,
                    posterUri = it.image_16_9,
                    name = it.title,
                    description = it.fullTitle
                )
            }
    }

    override fun getTrendingMovies(): List<Movie> {
        return _mostPopularMovies.subList(fromIndex = 0, toIndex = 10).map {
            Movie(
                id = it.id,
                description = it.fullTitle,
                name = it.title,
                posterUri = it.image_2_3
            )
        }
    }

    override fun getTop10Movies(): List<Movie> {
        return _top250Movies.subList(fromIndex = 20, toIndex = 30).map {
            Movie(
                id = it.id,
                posterUri = it.image_16_9,
                name = it.title,
                description = it.fullTitle
            )
        }
    }

    override fun getNowPlayingMovies(): List<Movie> {
        return _inTheatersMovies
            .map {
                Movie(
                    id = it.id,
                    posterUri = it.image_2_3,
                    name = it.title,
                    description = it.fullTitle
                )
            }
            .subList(fromIndex = 0, toIndex = 10)
    }

    override fun getMovieCategories(): List<MovieCategory> {
        return _movieCategories.map {
            MovieCategory(
                id = it.id,
                name = it.name
            )
        }
    }

    override fun getMovieCategoryDetails(categoryId: String): MovieCategoryDetails {
        val movieCategories = getMovieCategories()
        val movieCategory = movieCategories.find { it.id == categoryId } ?: movieCategories.first()
        return MovieCategoryDetails(
            id = movieCategory.id,
            name = movieCategory.name,
            movies = _top250Movies
                .shuffled()
                .subList(fromIndex = 0, toIndex = 20)
                .map {
                    Movie(
                        id = it.id,
                        posterUri = it.image_2_3,
                        name = it.title,
                        description = it.fullTitle
                    )
                }
        )
    }

    override fun getMovieDetails(movieId: String): MovieDetails {
        val movies = getMovies_2_3()
        val movie = movies.find { it.id === movieId } ?: movies.first()

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
            castAndCrew = _movieCastContainer.map {
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
            similarMovies = (1..2).map { movies.random() },
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

    override fun searchMovies(query: String): List<Movie> {
        return _top250Movies
            .filter { it.title.contains(other = query, ignoreCase = true) }
            .map {
                Movie(
                    id = it.id,
                    posterUri = it.image_2_3,
                    name = it.title,
                    description = it.fullTitle
                )
            }
    }

    override fun getMovies_16_9(): List<Movie> {
        return _top250Movies
            .filterIndexed { index, _ -> listOf(2, 4, 6, 8, 10).contains(index) }
            .map {
                Movie(
                    id = it.id,
                    posterUri = it.image_16_9,
                    name = it.title,
                    description = it.fullTitle
                )
            }
    }

    override fun getMovies_2_3(): List<Movie> {
        return _top250Movies
            .filterIndexed { index, _ -> listOf(2, 4, 6, 8, 10).contains(index) }
            .map {
                Movie(
                    id = it.id,
                    posterUri = it.image_2_3,
                    name = it.title,
                    description = it.fullTitle
                )
            }
    }

    override fun getPopularFilmsThisWeek(): List<Movie> {
        return _mostPopularMovies.subList(fromIndex = 11, toIndex = 20).map {
            Movie(
                id = it.id,
                posterUri = it.image_2_3,
                name = it.title,
                description = it.fullTitle
            )
        }
    }

    override fun getTVShows(): List<Movie> {
        return _mostPopularTVShows
            .subList(fromIndex = 0, toIndex = 5)
            .map {
                Movie(
                    id = it.id,
                    posterUri = it.image_16_9,
                    name = it.title,
                    description = it.fullTitle
                )
            }
    }

    override fun getBingeWatchDramas(): List<Movie> {
        return _mostPopularTVShows
            .subList(fromIndex = 6, toIndex = 15)
            .map {
                Movie(
                    id = it.id,
                    posterUri = it.image_2_3,
                    name = it.title,
                    description = it.fullTitle
                )
            }
    }

    override fun getFavouriteMovies(): List<Movie> {
        return _top250Movies
            .subList(fromIndex = 0, toIndex = 28)
            .map {
                Movie(
                    id = it.id,
                    posterUri = it.image_2_3,
                    name = it.title,
                    description = it.fullTitle
                )
            }
    }
}
