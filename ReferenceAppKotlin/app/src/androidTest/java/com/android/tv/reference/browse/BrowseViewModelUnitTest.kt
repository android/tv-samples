package com.android.tv.reference.browse

import android.app.Application
import android.content.res.Resources
import android.test.mock.MockContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.android.tv.reference.MainActivity
import com.android.tv.reference.R
import com.android.tv.reference.repository.FileVideoRepository
import com.android.tv.reference.repository.VideoRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.nio.charset.Charset

@RunWith(AndroidJUnit4::class)
@LargeTest
class BrowseViewModelUnitTest {

    private val videosByCategoryJson =
        """{"content":[{"name":"Video 1","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 1"},{"name":"Video 2","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 1"},{"name":"Video 1","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 2"},{"name":"Video 2","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 2"},{"name":"Video 1","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 3"},{"name":"Video 2","videoUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4","thumbnailUri":"https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg","backgroundUri":"https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg","category":"Category 3"}],"metadata":{"last_updated":"2020-04-06T17:15:59"}}"""

    @Mock
    private lateinit var mockApplicationContext: Application

    @Mock
    private lateinit var mockContextResources: Resources

    @Mock
    private lateinit var mockVideoRepository: VideoRepository

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setupTests() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(mockApplicationContext.applicationContext).thenReturn(mockApplicationContext)
        Mockito.`when`(mockApplicationContext.resources).thenReturn(mockContextResources)
        Mockito.`when`(mockVideoRepository.getAllVideos()).thenReturn(emptyList())
    }

    @Test
    fun getVideoGroupList_isCorrect() {
        activityRule.activity.runOnUiThread {
            Mockito.`when`(mockContextResources.openRawResource(R.raw.api)).thenReturn(
                videosByCategoryJson.byteInputStream(
                    Charset.defaultCharset()
                )
            )

            val viewModel = BrowseViewModel(mockApplicationContext)
            val videoGroups = viewModel.getVideoGroupList()
            assertNotNull(videoGroups)
            assertEquals(3, videoGroups.size)
            for (group in 1..videoGroups.size) {
                assertEquals("Category $group", videoGroups[group - 1].category)
                assertEquals(2, videoGroups[group - 1].videoList.size)
            }
        }
    }
}