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

import com.google.jetstream.data.entities.ThumbnailType
import com.google.jetstream.data.entities.toMovie
import com.google.jetstream.data.util.AssetsReader
import com.google.jetstream.data.util.StringConstants
import javax.inject.Inject

class MovieDataSource @Inject constructor(
    assetsReader: AssetsReader
) {

    private val top250MovieDataReader = CachedDataReader {
        readMovieData(assetsReader, StringConstants.Assets.Top250Movies)
    }

    private val mostPopularMovieDataReader = MovieDataReader {
        readMovieData(assetsReader, StringConstants.Assets.MostPopularMovies).map {
            it.toMovie()
        }
    }

    private val movieDataReader = MovieDataReader {
        top250MovieDataReader.read().map {
            it.toMovie()
        }
    }

    private var movieWithLongThumbnailDataReader: MovieDataReader = CachedDataReader {
        top250MovieDataReader.read().map {
            it.toMovie(ThumbnailType.Long)
        }
    }

    private val nowPlayingMovieDataReader: MovieDataReader = MovieDataReader {
        readMovieData(assetsReader, StringConstants.Assets.InTheaters).subList(0, 10).map {
            it.toMovie()
        }
    }

    suspend fun getMovieList(thumbnailType: ThumbnailType = ThumbnailType.Standard) =
        when (thumbnailType) {
            ThumbnailType.Standard -> movieDataReader.read()
            ThumbnailType.Long -> movieWithLongThumbnailDataReader.read()
        }

    suspend fun getFeaturedMovieList() =
        movieWithLongThumbnailDataReader.read().filterIndexed { index, _ ->
            listOf(1, 3, 5, 7, 9).contains(index)
        }

    suspend fun getTrendingMovieList() =
        mostPopularMovieDataReader.read().subList(0, 10)

    suspend fun getTop10MovieList() =
        movieWithLongThumbnailDataReader.read().subList(20, 30)

    suspend fun getNowPlayingMovieList() =
        nowPlayingMovieDataReader.read()

    suspend fun getPopularFilmThisWeek() =
        mostPopularMovieDataReader.read().subList(11, 20)

    suspend fun getFavoriteMovieList() =
        movieDataReader.read().subList(0, 28)
}
