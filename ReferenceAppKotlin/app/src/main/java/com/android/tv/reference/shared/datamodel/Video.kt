/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.tv.reference.shared.datamodel

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * Represents a video that can be played in the app
 */
@Parcelize
@JsonClass(generateAdapter = true)
class Video(
    val id: String,
    val name: String,
    val description: String,
    val uri: String,
    val videoUri: String,
    val thumbnailUri: String,
    @Json(name = "backgroundUri") val backgroundImageUri: String,
    val category: String,
    val videoType: VideoType,
    // The duration is specified in the ISO 8601 format as 'PT00H00M'. For more information on the
    // format, refer - https://en.wikipedia.org/wiki/ISO_8601.
    val duration: String = "PT00H00M",
    // The series, season and episode information is picked from the JSON feed that stores the
    // catalog. For consistency and proper formatting of the JSON, the fields for series, season and
    // episode data have been defined as empty strings for content types that are not TV Episodes.
    val seriesUri: String = "",
    val seasonUri: String = "",
    val episodeNumber: String = "",
    val seasonNumber: String = ""
) : Parcelable {

    override fun toString(): String {
        return "Video(name='$name')"
    }
}

enum class VideoType {
    @Json(name = "clip")
    CLIP,

    @Json(name = "episode")
    EPISODE,

    @Json(name = "movie")
    MOVIE
}
