package com.android.tv.reference.shared.parser

import com.android.tv.reference.parser.VideoParser
import com.android.tv.reference.shared.datamodel.VideoType
import com.squareup.moshi.JsonDataException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class VideoParserUnitTest {

    @Test
    fun loadVideosFromJson_isCorrect() {
        val videoList = VideoParser.loadVideosFromJson(VALID_JSON)
        assertNotNull(videoList)
        assertEquals(3, videoList.size)
        videoList.forEachIndexed { index, video ->
            val type = CONTENT_TYPES[index]
            assertEquals("${type}_id", video.id)
            assertEquals("$type name", video.name)
            assertEquals("Description for $type", video.description)

            val uriBase = "https://atv-reference-app.firebaseapp.com/content/$type"
            assertEquals(uriBase, video.uri)
            assertEquals("$uriBase/video.mp4", video.videoUri)
            assertEquals("$uriBase/thumbnail.jpg", video.thumbnailUri)
            assertEquals("$uriBase/background.jpg", video.backgroundImageUri)

            assertEquals("${type}s", video.category)
            val expectedEnum = when (type) {
                "clip" -> VideoType.CLIP
                "episode" -> VideoType.EPISODE
                "movie" -> VideoType.MOVIE
                else -> throw RuntimeException("Invalid type: $type")
            }
            assertEquals(expectedEnum, video.videoType)
        }
    }

    @Test(expected = JsonDataException::class)
    fun loadVideosFromJson_inputMalformed() {
        val videoList = VideoParser.loadVideosFromJson(MALFORMED_JSON)
    }

    @Test
    fun findVideoFromJson_videoFound() {
        val video = VideoParser.findVideoFromJson(VALID_JSON, "movie_id")
        assertNotNull(video)
    }

    @Test
    fun findVideoFromJson_videoNotFound() {
        val video = VideoParser.findVideoFromJson(VALID_JSON, "Video 999")
        assertNull(video)
    }

    @Test(expected = JsonDataException::class)
    fun findVideoFromJson_inputMalformed() {
        val video = VideoParser.findVideoFromJson(MALFORMED_JSON, "Video 1")
    }

    companion object {
        private val CONTENT_TYPES = arrayOf("clip", "episode", "movie")
        private const val VALID_JSON = """{
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
        "videoType": "clip"
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
        "videoType": "episode"
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
        "videoType": "movie"
    }
]
}"""
        private val MALFORMED_JSON = VALID_JSON.substring(10)
    }
}
