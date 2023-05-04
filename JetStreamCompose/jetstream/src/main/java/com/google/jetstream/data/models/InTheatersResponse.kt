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

class InTheatersResponse : ArrayList<InTheatersResponse.InTheatersResponseItem>() {
    data class InTheatersResponseItem(
        @SerializedName("contentRating")
        val contentRating: String,
        @SerializedName("directors")
        val directors: String,
        @SerializedName("fullTitle")
        val fullTitle: String,
        @SerializedName("genres")
        val genres: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("rating")
        val rating: String,
        @SerializedName("ratingCount")
        val ratingCount: String,
        @SerializedName("image_16_9")
        val image_16_9: String,
        @SerializedName("image_2_3")
        val image_2_3: String,
        @SerializedName("metacriticRating")
        val metacriticRating: String,
        @SerializedName("plot")
        val plot: String,
        @SerializedName("releaseState")
        val releaseState: String,
        @SerializedName("runtimeMins")
        val runtimeMins: String,
        @SerializedName("runtimeStr")
        val runtimeStr: String,
        @SerializedName("stars")
        val stars: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("year")
        val year: String
    ) {
        data class Director(
            @SerializedName("id")
            val id: String,
            @SerializedName("name")
            val name: String
        )

        data class Genre(
            @SerializedName("key")
            val key: String,
            @SerializedName("value")
            val value: String
        )

        data class Star(
            @SerializedName("id")
            val id: String,
            @SerializedName("name")
            val name: String
        )
    }
}
