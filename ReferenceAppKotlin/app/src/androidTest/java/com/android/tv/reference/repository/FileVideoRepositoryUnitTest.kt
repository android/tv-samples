package com.android.tv.reference.repository

import android.app.Application
import android.content.res.Resources
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.android.tv.reference.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.nio.charset.Charset

@RunWith(AndroidJUnit4::class)
@LargeTest
class FileVideoRepositoryUnitTest {

    private val correctVideoListJson =
        """{"content":[{"name":"Video 1","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 1"},{"name":"Video 2","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 2"},{"name":"Video 3","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 3"}],"metadata":{"last_updated":"2020-04-06T17:15:59"}}"""

    private val malFormedVideoListJson =
        """{"content":[{"name":"Video 1","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 1"},{"name":"Video 2","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 2"},{"name":"Video 3","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 3"}] "metadata":{"last_updated":"2020-04-06T17:15:59"}}"""

    @Mock
    private lateinit var mockApplicationContext: Application

    @Mock
    private lateinit var mockContextResources: Resources

    @Before
    fun setupTests() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(mockApplicationContext.applicationContext).thenReturn(mockApplicationContext)
        Mockito.`when`(mockApplicationContext.resources).thenReturn(mockContextResources)
    }

    @Test
    fun getAllVideos_isCorrect() {
        Mockito.`when`(mockContextResources.openRawResource(R.raw.api)).thenReturn(
            correctVideoListJson.byteInputStream(
                Charset.defaultCharset()
            )
        )

        val repository = FileVideoRepository(mockApplicationContext as Application)
        val videoList = repository.getAllVideos()
        assertNotNull(videoList)
        assertEquals(3, videoList.size)
        for (index in 1..videoList.size) {
            val video = videoList[index - 1]
            assertEquals("Video $index", video.name)
            assertEquals("Category $index", video.category)

            assertEquals(
                "https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4",
                video.videoUri
            )

            assertEquals(
                "https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg",
                video.thumbnailUri
            )

            assertEquals(
                "https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg",
                video.backgroundImageUri
            )
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun getAllVideos_fileMalformed() {
        Mockito.`when`(mockContextResources.openRawResource(R.raw.api)).thenReturn(
            malFormedVideoListJson.byteInputStream(
                Charset.defaultCharset()
            )
        )

        FileVideoRepository(mockApplicationContext as Application)
    }

    @Test(expected = Resources.NotFoundException::class)
    fun init_missingApiFile() {
        Mockito.`when`(mockContextResources.openRawResource(R.raw.api))
            .thenThrow(Resources.NotFoundException())
        FileVideoRepository(mockApplicationContext as Application)
    }
}