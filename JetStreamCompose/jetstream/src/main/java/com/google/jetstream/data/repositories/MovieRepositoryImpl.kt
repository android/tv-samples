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
import com.google.jetstream.data.models.MovieCastResponseItem
import com.google.jetstream.data.models.MovieCategoriesResponseItem
import com.google.jetstream.data.models.MoviesResponseItem
import com.google.jetstream.data.util.AssetsReader
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.data.util.StringConstants.Movie.Reviewer.DefaultCount
import com.google.jetstream.data.util.StringConstants.Movie.Reviewer.DefaultRating
import com.google.jetstream.data.util.StringConstants.Movie.Reviewer.FreshTomatoes
import com.google.jetstream.data.util.StringConstants.Movie.Reviewer.ReviewerName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val assetsReader: AssetsReader
) : MovieRepository {

    private val mostPopularMovies = flow {
        val list = withContext(Dispatchers.IO) {
            assetsReader.getJsonDataFromAsset(StringConstants.Assets.MostPopularMovies).map {
                Json.decodeFromString<List<MoviesResponseItem>>(it)
            }.getOrDefault(emptyList())
        }
        emit(list)
    }

    private val inTheatersMovies = flow {
        val list = withContext(Dispatchers.IO) {
            assetsReader.getJsonDataFromAsset(StringConstants.Assets.InTheaters).map {
                Json.decodeFromString<List<MoviesResponseItem>>(it)
            }.getOrDefault(emptyList())
        }
        emit(list)
    }

    private val mostPopularTVShows = flow {
        val list = withContext(Dispatchers.IO) {
            assetsReader.getJsonDataFromAsset(StringConstants.Assets.MostPopularTVShows).map {
                Json.decodeFromString<List<MoviesResponseItem>>(it)
            }.getOrDefault(emptyList())
        }
        emit(list)
    }


    private suspend fun readMovieCastList(): List<MovieCast> {
        return withContext(Dispatchers.IO) {
            assetsReader.getJsonDataFromAsset(StringConstants.Assets.MovieCast).map {
                Json.decodeFromString<List<MovieCastResponseItem>>(it)
            }.getOrDefault(emptyList())
        }.map {
            MovieCast(
                id = it.id,
                characterName = it.characterName,
                realName = it.realName,
                avatarUrl = it.avatarUrl
            )
        }
    }

    private val top250MoviesFlow = flow {
        val list = readTop250MovieList()
        emit(list)
    }

    private val mutexMovieListCache = Mutex()
    private lateinit var movieListCache: List<MoviesResponseItem>

    private suspend fun readTop250MovieList(): List<MoviesResponseItem> {
        return mutexMovieListCache.withLock {
            if (!::movieListCache.isInitialized) {
                movieListCache = withContext(Dispatchers.IO) {
                    assetsReader.getJsonDataFromAsset(StringConstants.Assets.Top250Movies).map {
                        Json.decodeFromString<List<MoviesResponseItem>>(it)
                    }.getOrDefault(emptyList())
                }
            }
            movieListCache
        }
    }

    private val top250Movies = top250MoviesFlow.map { movieList ->
        movieList.map {
            Movie.fromMoviesResponseItem(it)
        }
    }

    private val top250MoviesWithWideThumbnail = top250MoviesFlow.map { movieList ->
        movieList.map { Movie.fromMovieResponseItemWithLongerThumbnail(it) }
    }

    private val categoryList = flow {
        emit(readCategoryList())
    }

    private val mutexCategoryListCache = Mutex()
    private lateinit var movieCategoriesResponseItemCache: List<MovieCategoriesResponseItem>

    private suspend fun readCategoryList(): MovieCategoryList {
        return mutexCategoryListCache.withLock {
            if (!::movieCategoriesResponseItemCache.isInitialized) {
                movieCategoriesResponseItemCache = withContext(Dispatchers.IO) {
                    assetsReader.getJsonDataFromAsset(StringConstants.Assets.MovieCategories).map {
                        Json.decodeFromString<List<MovieCategoriesResponseItem>>(it)
                    }.getOrDefault(emptyList())
                }
            }
            MovieCategoryList(
                movieCategoriesResponseItemCache.map {
                    MovieCategory(it.id, it.name)
                }
            )
        }
    }

    override fun getFeaturedMovies(): Flow<MovieList> = top250MoviesWithWideThumbnail.map {
        val list = it.filterIndexed { index, _ -> listOf(1, 3, 5, 7, 9).contains(index) }
        MovieList(list)
    }

    override fun getTrendingMovies(): Flow<MovieList> = mostPopularMovies.map { movieList ->
        val list = movieList.subList(fromIndex = 0, toIndex = 10).map {
            Movie.fromMoviesResponseItem(it)
        }
        MovieList(value = list)
    }

    override fun getTop10Movies(): Flow<MovieList> = top250MoviesWithWideThumbnail.map {
        val list = it.subList(fromIndex = 20, toIndex = 30)
        MovieList(list)
    }

    override fun getNowPlayingMovies(): Flow<MovieList> = inTheatersMovies.map { movieList ->
        val list = movieList
            .subList(fromIndex = 0, toIndex = 10)
            .map {
                Movie.fromMoviesResponseItem(it)
            }
        MovieList(value = list)
    }

    override fun getMovieCategories() = categoryList

    override suspend fun getMovieCategoryDetails(categoryId: String): MovieCategoryDetails {
        val categoryList = readCategoryList()
        val movieList = readTop250MovieList()

        val category = categoryList.find { categoryId == it.id } ?: categoryList.first()
        val list = movieList.shuffled().subList(0, 20).map {
            Movie.fromMoviesResponseItem(it)
        }
        return MovieCategoryDetails(
            id = category.id,
            name = category.name,
            movies = MovieList(list)
        )
    }

    override suspend fun getMovieDetails(movieId: String): MovieDetails {
        val movieList = readTop250MovieList()
        val found = movieList.find { it.id == movieId } ?: movieList.first()
        val movie = Movie.fromMoviesResponseItem(found)
        val similarMovieList = (1..2).map { Movie.fromMoviesResponseItem(movieList.random()) }

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
            castAndCrew = readMovieCastList(),
            status = "Released",
            originalLanguage = "English",
            budget = "$15M",
            revenue = "$40M",
            similarMovies = similarMovieList,
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
        val responses = readTop250MovieList()
        val filtered = mutexMovieListCache.withLock {
            responses.filter {
                it.title.contains(other = query, ignoreCase = true)
            }
        }
        return MovieList(value = filtered.map { Movie.fromMoviesResponseItem(it) })
    }

    override fun getMoviesWithLongThumbnail() = top250MoviesWithWideThumbnail.map {
        MovieList(it)
    }

    override fun getMovies(): Flow<MovieList> = top250Movies.map { MovieList(it) }

    override fun getPopularFilmsThisWeek(): Flow<MovieList> =
        mostPopularMovies.map { movieList ->
            val list = movieList.subList(fromIndex = 11, toIndex = 20)
                .map { Movie.fromMoviesResponseItem(it) }
            MovieList(value = list)
        }

    override fun getTVShows(): Flow<MovieList> = mostPopularTVShows.map { movieList ->
        val list = movieList.subList(fromIndex = 0, toIndex = 5)
            .map { Movie.fromMovieResponseItemWithLongerThumbnail(it) }
        MovieList(list)
    }

    override fun getBingeWatchDramas(): Flow<MovieList> = mostPopularTVShows.map { movieList ->
        val list = movieList.subList(fromIndex = 6, toIndex = 15)
            .map { Movie.fromMoviesResponseItem(it) }
        MovieList(value = list)
    }

    override fun getFavouriteMovies(): Flow<MovieList> = top250Movies.map {
        val list = it.subList(fromIndex = 0, toIndex = 28)
        MovieList(value = list)
    }

}
