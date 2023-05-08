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

package com.google.jetstream.data.models

import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    @SerializedName("actorList")
    val actorList: List<Actor>?,
    @SerializedName("awards")
    val awards: String?,
    @SerializedName("boxOffice")
    val boxOffice: BoxOffice?,
    @SerializedName("companies")
    val companies: String?,
    @SerializedName("companyList")
    val companyList: List<Company>?,
    @SerializedName("contentRating")
    val contentRating: String?,
    @SerializedName("countries")
    val countries: String?,
    @SerializedName("countryList")
    val countryList: List<Country>?,
    @SerializedName("directors")
    val directors: String?,
    @SerializedName("errorMessage")
    val errorMessage: String?,
    @SerializedName("fullCast")
    val fullCast: Any?,
    @SerializedName("fullTitle")
    val fullTitle: String?,
    @SerializedName("genres")
    val genres: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("rating")
    val rating: String?,
    @SerializedName("ratingVotes")
    val ratingVotes: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("images")
    val images: Any?,
    @SerializedName("keywordList")
    val keywordList: List<String>?,
    @SerializedName("keywords")
    val keywords: String?,
    @SerializedName("languageList")
    val languageList: List<Language>?,
    @SerializedName("languages")
    val languages: String?,
    @SerializedName("metacriticRating")
    val metacriticRating: String?,
    @SerializedName("originalTitle")
    val originalTitle: String?,
    @SerializedName("plot")
    val plot: String?,
    @SerializedName("plotLocal")
    val plotLocal: String?,
    @SerializedName("plotLocalIsRtl")
    val plotLocalIsRtl: Boolean?,
    @SerializedName("posters")
    val posters: Any?,
    @SerializedName("ratings")
    val ratings: Ratings?,
    @SerializedName("releaseDate")
    val releaseDate: String?,
    @SerializedName("runtimeMins")
    val runtimeMins: String?,
    @SerializedName("runtimeStr")
    val runtimeStr: String?,
    @SerializedName("similars")
    val similars: List<Similar>?,
    @SerializedName("stars")
    val stars: String?,
    @SerializedName("tagline")
    val tagline: Any?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("trailer")
    val trailer: Any?,
    @SerializedName("tvEpisodeInfo")
    val tvEpisodeInfo: Any?,
    @SerializedName("tvSeriesInfo")
    val tvSeriesInfo: Any?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("wikipedia")
    val wikipedia: Any?,
    @SerializedName("writerList")
    val writerList: List<Writer>?,
    @SerializedName("writers")
    val writers: String?,
    @SerializedName("year")
    val year: String?
) {
    data class Actor(
        @SerializedName("asCharacter")
        val asCharacter: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("image")
        val image: String?,
        @SerializedName("name")
        val name: String?
    )

    data class BoxOffice(
        @SerializedName("budget")
        val budget: String?,
        @SerializedName("cumulativeWorldwideGross")
        val cumulativeWorldwideGross: String?,
        @SerializedName("grossUSA")
        val grossUSA: String?,
        @SerializedName("openingWeekendUSA")
        val openingWeekendUSA: String?
    )

    data class Company(
        @SerializedName("id")
        val id: String?,
        @SerializedName("name")
        val name: String?
    )

    data class Country(
        @SerializedName("key")
        val key: String?,
        @SerializedName("value")
        val value: String?
    )

    data class Director(
        @SerializedName("id")
        val id: String?,
        @SerializedName("name")
        val name: String?
    )

    data class Genre(
        @SerializedName("key")
        val key: String?,
        @SerializedName("value")
        val value: String?
    )

    data class Language(
        @SerializedName("key")
        val key: String?,
        @SerializedName("value")
        val value: String?
    )

    data class Ratings(
        @SerializedName("errorMessage")
        val errorMessage: String?,
        @SerializedName("filmAffinity")
        val filmAffinity: String?,
        @SerializedName("fullTitle")
        val fullTitle: String?,
        @SerializedName("metacritic")
        val metacritic: String?,
        @SerializedName("rottenTomatoes")
        val rottenTomatoes: String?,
        @SerializedName("theMovieDb")
        val theMovieDb: String?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("type")
        val type: String?,
        @SerializedName("year")
        val year: String?
    )

    data class Similar(
        @SerializedName("id")
        val id: String?,
        @SerializedName("image")
        val image: String?,
        @SerializedName("title")
        val title: String?
    )

    data class Star(
        @SerializedName("id")
        val id: String?,
        @SerializedName("name")
        val name: String?
    )

    data class Writer(
        @SerializedName("id")
        val id: String?,
        @SerializedName("name")
        val name: String?
    )
}
