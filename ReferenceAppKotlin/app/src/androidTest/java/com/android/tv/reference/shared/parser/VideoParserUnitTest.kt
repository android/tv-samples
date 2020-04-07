package com.android.tv.reference.shared.parser

import com.android.tv.reference.parser.VideoParser
import org.junit.Assert.*
import org.junit.Test
import java.lang.IllegalArgumentException

class VideoParserUnitTest {

    private val correctVideoListJson =
        """{"content":[{"name":"Video 1","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 1"},{"name":"Video 2","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 2"},{"name":"Video 3","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 3"}],"metadata":{"last_updated":"2020-04-06T17:15:59"}}"""

    private val malFormedVideoListJson =
        """{"content":[{"name":"Video 1","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 1"},{"name":"Video 2","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 2"},{"name":"Video 3","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 3"}] "metadata":{"last_updated":"2020-04-06T17:15:59"}}"""

    @Test
    fun loadVideosFromJson_isCorrect() {
        val videoList =
            VideoParser.loadVideosFromJson(
                correctVideoListJson
            )
        assertNotNull(videoList)
        assertEquals(3, videoList.size)
        for(index in 1..videoList.size) {
            val video = videoList[index-1]
            assertEquals("Video $index", video.name)
            assertEquals("Category $index", video.category)

            assertEquals("https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4",
            video.videoUri)

            assertEquals("https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg",
            video.thumbnailUri)

            assertEquals("https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg",
            video.backgroundImageUri)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun loadVideosFromJson_inputMalformed() {
        val videoList =
            VideoParser.loadVideosFromJson(
                malFormedVideoListJson
            )
    }

    @Test
    fun findVideoFromJson_videoFound() {
        val video = VideoParser.findVideoFromJson(correctVideoListJson, "Video 1")
        assertNotNull(video)
    }

    @Test
    fun findVideoFromJson_videoNotFound() {
        val video = VideoParser.findVideoFromJson(correctVideoListJson, "Video 999")
        assertNull(video)
    }

    @Test(expected = IllegalArgumentException::class)
    fun findVideoFromJson_inputMalformed() {
        val video = VideoParser.findVideoFromJson(malFormedVideoListJson, "Video 1")
    }

}