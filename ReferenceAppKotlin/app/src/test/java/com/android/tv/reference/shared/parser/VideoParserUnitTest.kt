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
package com.android.tv.reference.shared.parser

import com.android.tv.reference.repository.VideoParser
import com.android.tv.reference.shared.datamodel.VideoType
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.JsonDataException
import org.junit.Assert.assertThrows
import org.junit.Test

class VideoParserUnitTest {

    @Test
    fun loadVideosFromJson_isCorrect() {
        val videoList = VideoParser.loadVideosFromJson(VALID_JSON)
        assertThat(videoList).hasSize(3)
        videoList.forEachIndexed { index, video ->
            val type = CONTENT_TYPES[index]
            assertThat(video.id).isEqualTo("${type}_id")
            assertThat(video.name).isEqualTo("$type name")
            assertThat(video.description).isEqualTo("Description for $type")

            val uriBase = "https://atv-reference-app.firebaseapp.com/content/$type"
            assertThat(video.uri).isEqualTo(uriBase)
            assertThat(video.videoUri).isEqualTo("$uriBase/video.mp4")
            assertThat(video.thumbnailUri).isEqualTo("$uriBase/thumbnail.jpg")
            assertThat(video.backgroundImageUri).isEqualTo("$uriBase/background.jpg")

            assertThat(video.category).isEqualTo("${type}s")
            val expectedEnum = when (type) {
                "clip" -> VideoType.CLIP
                "episode" -> VideoType.EPISODE
                "movie" -> VideoType.MOVIE
                else -> throw RuntimeException("Invalid type: $type")
            }
            assertThat(video.videoType).isEqualTo(expectedEnum)
        }
    }

    @Test
    fun loadVideosFromJson_inputMalformed() {
        assertThrows(JsonDataException::class.java) {
            VideoParser.loadVideosFromJson(MALFORMED_JSON)
        }
    }

    @Test
    fun findVideoFromJson_videoFound() {
        val video = VideoParser.findVideoFromJson(VALID_JSON, "movie_id")
        assertThat(video).isNotNull()
    }

    @Test
    fun findVideoFromJson_videoNotFound() {
        val video = VideoParser.findVideoFromJson(VALID_JSON, "Video 999")
        assertThat(video).isNull()
    }

    @Test
    fun findVideoFromJson_inputMalformed() {
        assertThrows(JsonDataException::class.java) {
            VideoParser.findVideoFromJson(MALFORMED_JSON, "Video 1")
        }
    }

    companion object {
        private val CONTENT_TYPES = arrayOf("clip", "episode", "movie")
        private const val VALID_JSON =
            """{
"content": [
    {
        "id": "clip_id",
        "name": "clip name",
        "description": "Description for clip",
        "uri": "https://atv-reference-app.firebaseapp.com/content/clip",
        "videoUri": "https://atv-reference-app.firebaseapp.com/content/clip/video.mp4",
        "thumbnailUri": "https://atv-reference-app.firebaseapp.com/content/clip/thumbnail.jpg",
        "backgroundUri": "https://atv-reference-app.firebaseapp.com/content/clip/background.jpg",
        "category": "clips",
        "videoType": "clip",
        "seriesUri": "https://atv-reference-app.firebaseapp.com/content/clip/series",
        "seasonUri": "",
        "seasonNumber": "",
        "episodeNumber": "",
        "duration": "PT00H00M"
    },
    {
        "id": "episode_id",
        "name": "episode name",
        "description": "Description for episode",
        "uri": "https://atv-reference-app.firebaseapp.com/content/episode",
        "videoUri": "https://atv-reference-app.firebaseapp.com/content/episode/video.mp4",
        "thumbnailUri": "https://atv-reference-app.firebaseapp.com/content/episode/thumbnail.jpg",
        "backgroundUri": "https://atv-reference-app.firebaseapp.com/content/episode/background.jpg",
        "category": "episodes",
        "videoType": "episode",
        "seriesUri": "https://atv-reference-app.firebaseapp.com/content/episode/series",
        "seasonUri": "https://atv-reference-app.firebaseapp.com/content/episode/season",
        "seasonNumber": "1",
        "episodeNumber": "1",
        "duration": "PT00H00M"
    },
    {
        "id": "movie_id",
        "name": "movie name",
        "description": "Description for movie",
        "uri": "https://atv-reference-app.firebaseapp.com/content/movie",
        "videoUri": "https://atv-reference-app.firebaseapp.com/content/movie/video.mp4",
        "thumbnailUri": "https://atv-reference-app.firebaseapp.com/content/movie/thumbnail.jpg",
        "backgroundUri": "https://atv-reference-app.firebaseapp.com/content/movie/background.jpg",
        "category": "movies",
        "videoType": "movie",
        "seriesUri": "https://atv-reference-app.firebaseapp.com/content/movie/series",
        "seasonUri": "",
        "seasonNumber": "",
        "episodeNumber": "",
        "duration": "PT00H00M"
    }
]
}"""
        private val MALFORMED_JSON = VALID_JSON.substring(10)
    }
}
