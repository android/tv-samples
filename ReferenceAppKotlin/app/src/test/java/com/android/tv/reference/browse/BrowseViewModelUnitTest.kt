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
package com.android.tv.reference.browse

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.android.tv.reference.repository.VideoRepository
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.datamodel.VideoType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@LargeTest
@RunWith(AndroidJUnit4::class)
class BrowseViewModelUnitTest {

    private lateinit var testVideos: List<Video>

    @Mock
    private lateinit var mockVideoRepository: VideoRepository

    @Before
    fun setupTests() {
        MockitoAnnotations.initMocks(this)
        testVideos = createTestVideos()
        Mockito.`when`(mockVideoRepository.getAllVideos()).thenReturn(testVideos)
    }

    @Test
    fun getVideoGroupList_isCorrect() {
        val viewModel = BrowseViewModel(ApplicationProvider.getApplicationContext())
        val videoGroups = viewModel.getVideoGroupList(mockVideoRepository)
        assertNotNull(videoGroups)
        assertEquals(2, videoGroups.size)

        // Categories are different
        assertNotEquals(videoGroups[0].category, videoGroups[1].category)

        // Each Video in a VideoGroup should belong to the same category
        videoGroups.forEach { videoGroup ->
            val testCategory = videoGroup.category
            videoGroup.videoList.forEach { assertEquals(testCategory, it.category) }
        }
    }

    private fun createTestVideos(): List<Video> {
        return listOf(
            Video(
                "category_a_video_0",
                "name",
                "description",
                "https://example.com/valid",
                "https://example.com/valid",
                "https://example.com/valid",
                "https://example.com/valid",
                "category_a",
                VideoType.CLIP
            ),
            Video(
                "category_b_video_0",
                "name",
                "description",
                "https://example.com/valid",
                "https://example.com/valid",
                "https://example.com/valid",
                "https://example.com/valid",
                "category_b",
                VideoType.CLIP
            ),
            Video(
                "category_a_video_1",
                "name",
                "description",
                "https://example.com/valid",
                "https://example.com/valid",
                "https://example.com/valid",
                "https://example.com/valid",
                "category_a",
                VideoType.CLIP
            )
        )
    }
}
