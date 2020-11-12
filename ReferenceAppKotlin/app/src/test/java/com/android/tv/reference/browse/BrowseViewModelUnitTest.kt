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
import com.google.common.truth.Truth.assertThat
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
        assertThat(videoGroups).hasSize(2)

        // Categories are different
        assertThat(videoGroups.map { it.category })
            .containsExactly("category_a", "category_b").inOrder()

        // Each Video in a VideoGroup should belong to the same category
        videoGroups.forEach { videoGroup ->
            val testCategory = videoGroup.category
            val videoCategories = videoGroup.videoList.map { it.category }.distinct()
            assertThat(videoCategories).containsExactly(testCategory)
        }
    }

    private fun createTestVideos(): List<Video> {
        return listOf(
            Video(
                id = "category_a_video_0",
                name = "name",
                description = "description",
                uri = "https://example.com/valid",
                videoUri = "https://example.com/valid",
                thumbnailUri = "https://example.com/valid",
                backgroundImageUri = "https://example.com/valid",
                category = "category_a",
                videoType = VideoType.CLIP,
                duration = "PT00H01M",
                seriesUri = "https://example.com/valid",
                seasonUri = "https://example.com/valid"
            ),
            Video(
                id = "category_b_video_0",
                name = "name",
                description = "description",
                uri = "https://example.com/valid",
                videoUri = "https://example.com/valid",
                thumbnailUri = "https://example.com/valid",
                backgroundImageUri = "https://example.com/valid",
                category = "category_b",
                videoType = VideoType.CLIP,
                duration = "PT00H01M",
                seriesUri = "https://example.com/valid",
                seasonUri = "https://example.com/valid"
            ),
            Video(
                id = "category_a_video_1",
                name = "name",
                description = "description",
                uri = "https://example.com/valid",
                videoUri = "https://example.com/valid",
                thumbnailUri = "https://example.com/valid",
                backgroundImageUri = "https://example.com/valid",
                category = "category_a",
                videoType = VideoType.CLIP,
                duration = "PT00H01M",
                seriesUri = "https://example.com/valid",
                seasonUri = "https://example.com/valid"
            )
        )
    }
}
