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
package com.android.tv.reference.castconnect

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.tv.reference.repository.VideoRepository
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.datamodel.VideoType
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Test cases for converting load request from sender to video and
 * retrieving appropriate application-specific video object.
 */
@RunWith(AndroidJUnit4::class)
class CastMediaLoadCommandCallbackTest {

    @Mock
    private lateinit var mockVideoRepository: VideoRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun convertLoadRequestToVideo_returnsValidVideo() {
        val video = Video(
            id = "an_id",
            name = "name",
            description = "description",
            uri = "https://example.com/valid",
            videoUri = "https://example.com/valid",
            thumbnailUri = "https://example.com/valid",
            backgroundImageUri = "https://example.com/valid",
            category = "category",
            videoType = VideoType.MOVIE,
            duration = "PT00H10M",
            seriesUri = "https://example.com/valid",
            seasonUri = "https://example.com/valid"
        )

        val castMediaLoadCommandCallbackTest = CastMediaLoadCommandCallback(
            { _, _ -> }, ApplicationProvider.getApplicationContext()
        )

        val mediaToLoad = MediaInfo.Builder("https://example.com/watch/some-id").build()
        val mediaLoadRequestDataTest = MediaLoadRequestData.Builder()
            .setMediaInfo(mediaToLoad)
            .build()

        Mockito.doReturn(video).`when`(mockVideoRepository)
            .getVideoById(mediaLoadRequestDataTest.mediaInfo.contentId)

        val result = castMediaLoadCommandCallbackTest
            .convertLoadRequestToVideo(mediaLoadRequestDataTest, mockVideoRepository)

        assertThat(result).isEqualTo(video)
    }

    @Test
    fun convertLoadRequestToVideo_returnsNullInvalidVideo() {
        val castMediaLoadCommandCallbackTest = CastMediaLoadCommandCallback(
            { _, _ -> }, ApplicationProvider.getApplicationContext()
        )

        val mediaToLoad = MediaInfo.Builder("https://example.com/watch/some-id").build()
        val mediaLoadRequestDataTest = MediaLoadRequestData.Builder()
            .setMediaInfo(mediaToLoad)
            .build()

        Mockito.doReturn(null).`when`(mockVideoRepository)
            .getVideoById(mediaLoadRequestDataTest.mediaInfo.contentId)

        val result = castMediaLoadCommandCallbackTest
            .convertLoadRequestToVideo(mediaLoadRequestDataTest, mockVideoRepository)

        assertThat(result).isNull()
    }
}
