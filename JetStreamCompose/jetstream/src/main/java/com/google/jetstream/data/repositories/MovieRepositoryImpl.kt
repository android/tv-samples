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
import com.google.jetstream.data.entities.ThumbnailType
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val assetsReader: AssetsReader
) : MovieRepository {

    private val movieCastDataSource = CachedDataSource {
        readMovieCastData(assetsReader, StringConstants.Assets.MovieCast).map {
            MovieCast.from(it)
        }
    }

    private val movieCategoryDataSource = CachedDataSource {
        readMovieCategoryData(assetsReader, StringConstants.Assets.MovieCategories).map {
            MovieCategory.from(it)
        }
    }

    private var movieDataSource: MovieDataSource
    private var movieWithLongThumbnailDataSource: MovieDataSource
    private var featuredMovieDataSource: MovieDataSource
    private var trendingMovieDataSource: MovieDataSource
    private var top10MovieDataSource: MovieDataSource
    private var nowPlayingMovieDataSource: MovieDataSource
    private var popularFilmThisWeekDataSource: MovieDataSource
    private var tvShowDataSource: MovieDataSource
    private var bingeWatchDramaDatasource: MovieDataSource
    private var favoriteMovieDataSource: MovieDataSource

    init {
        val top250MovieDataReader = CachedDataSource {
            readMovieData(assetsReader, StringConstants.Assets.Top250Movies)
        }
        val mostPopularMovieDataSource = CachedDataSource {
            readMovieData(assetsReader, StringConstants.Assets.MostPopularMovies).map {
                Movie.from(it)
            }
        }

        val mostPopularTvShowsDataSource = CachedDataSource {
            readMovieData(assetsReader, StringConstants.Assets.MostPopularTVShows)
        }

        movieDataSource = CachedDataSource {
            top250MovieDataReader.read().map {
                Movie.from(it)
            }
        }

        movieWithLongThumbnailDataSource = CachedDataSource {
            top250MovieDataReader.read().map {
                Movie.from(it, ThumbnailType.Long)
            }
        }

        featuredMovieDataSource = MovieDataSource {
            movieWithLongThumbnailDataSource.read().filterIndexed { index, _ ->
                listOf(1, 3, 5, 7, 9).contains(index)
            }
        }

        trendingMovieDataSource = MovieDataSource {
            mostPopularMovieDataSource.read().subList(0, 10)
        }

        top10MovieDataSource = MovieDataSource {
            movieWithLongThumbnailDataSource.read().subList(20, 30)
        }

        nowPlayingMovieDataSource = MovieDataSource {
            readMovieData(assetsReader, StringConstants.Assets.InTheaters).subList(0, 10).map {
                Movie.from(it)
            }
        }

        popularFilmThisWeekDataSource = MovieDataSource {
            mostPopularMovieDataSource.read().subList(11, 20)
        }

        tvShowDataSource = MovieDataSource {
            mostPopularTvShowsDataSource.read().subList(0, 5).map {
                Movie.from(it, ThumbnailType.Long)
            }
        }

        bingeWatchDramaDatasource = MovieDataSource {
            mostPopularTvShowsDataSource.read().subList(6, 15).map {
                Movie.from(it)
            }
        }

        favoriteMovieDataSource = MovieDataSource {
            movieDataSource.read().subList(0, 28)
        }

    }

    override fun getFeaturedMovies() = flow {
        val list = MovieList(featuredMovieDataSource.read())
        emit(list)
    }

    override fun getTrendingMovies(): Flow<MovieList> = flow {
        val list = MovieList(trendingMovieDataSource.read())
        emit(list)
    }

    override fun getTop10Movies(): Flow<MovieList> = flow {
        val list = MovieList(top10MovieDataSource.read())
        emit(list)
    }

    override fun getNowPlayingMovies(): Flow<MovieList> = flow {
        val list = MovieList(nowPlayingMovieDataSource.read())
        emit(list)
    }

    override fun getMovieCategories() = flow {
        val list = MovieCategoryList(movieCategoryDataSource.read())
        emit(list)
    }

    override suspend fun getMovieCategoryDetails(categoryId: String): MovieCategoryDetails {
        val categoryList = movieCategoryDataSource.read()
        val category = categoryList.find { categoryId == it.id } ?: categoryList.first()

        val movieList = movieDataSource.read().shuffled().subList(0, 20)

        return MovieCategoryDetails(
            id = category.id,
            name = category.name,
            movies = MovieList(movieList)
        )
    }

    override suspend fun getMovieDetails(movieId: String): MovieDetails {
        val movieList = movieDataSource.read()
        val movie = movieList.find { it.id == movieId } ?: movieList.first()
        val similarMovieList = movieList.shuffled().subList(0, 2)
        val castList = movieCastDataSource.read()

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
            castAndCrew = castList,
            status = "Released",
            originalLanguage = "English",
            budget = "$15M",
            revenue = "$40M",
            similarMovies = MovieList(similarMovieList),
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
        val filtered = movieDataSource.read().filter {
            it.name.contains(other = query, ignoreCase = true)
        }
        return MovieList(filtered)
    }

    override fun getMoviesWithLongThumbnail() = flow {
        val list = movieWithLongThumbnailDataSource.read()
        emit(MovieList(list))
    }

    override fun getMovies(): Flow<MovieList> = flow {
        val list = movieDataSource.read()
        emit(MovieList(list))
    }

    override fun getPopularFilmsThisWeek(): Flow<MovieList> = flow {
        val list = popularFilmThisWeekDataSource.read()
        emit(MovieList(list))
    }

    override fun getTVShows(): Flow<MovieList> = flow {
        val list = tvShowDataSource.read()
        emit(MovieList(list))
    }

    override fun getBingeWatchDramas(): Flow<MovieList> = flow {
        val list = bingeWatchDramaDatasource.read()
        emit(MovieList(list))
    }

    override fun getFavouriteMovies(): Flow<MovieList> = flow {
        val list = favoriteMovieDataSource.read()
        emit(MovieList(list))
    }

}

private suspend fun readMovieData(
    assetsReader: AssetsReader,
    resourceId: String
): List<MoviesResponseItem> = withContext(Dispatchers.IO) {
    assetsReader.getJsonDataFromAsset(resourceId).map {
        Json.decodeFromString<List<MoviesResponseItem>>(it)
    }.getOrDefault(emptyList())
}

private suspend fun readMovieCastData(
    assetsReader: AssetsReader,
    resourceId: String
): List<MovieCastResponseItem> = withContext(Dispatchers.IO) {
    assetsReader.getJsonDataFromAsset(resourceId).map {
        Json.decodeFromString<List<MovieCastResponseItem>>(it)
    }.getOrDefault(emptyList())
}

private suspend fun readMovieCategoryData(
    assetsReader: AssetsReader,
    resourceId: String
): List<MovieCategoriesResponseItem> = withContext(Dispatchers.IO) {
    assetsReader.getJsonDataFromAsset(resourceId).map {
        Json.decodeFromString<List<MovieCategoriesResponseItem>>(it)
    }.getOrDefault(emptyList())
}

private class CachedDataSource<T>(private val reader: suspend () -> List<T>) {
    private val mutex = Mutex()
    private lateinit var cache: List<T>

    suspend fun read(): List<T> {
        mutex.withLock {
            if (!::cache.isInitialized) {
                cache = reader()
            }
        }
        return cache
    }
}

private typealias MovieDataSource = CachedDataSource<Movie>