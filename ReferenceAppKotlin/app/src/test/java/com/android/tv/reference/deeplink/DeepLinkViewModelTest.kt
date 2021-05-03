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
package com.android.tv.reference.deeplink

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.tv.reference.repository.VideoRepository
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.datamodel.VideoType
import com.android.tv.reference.shared.util.Result
import com.squareup.moshi.JsonDataException
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class DeepLinkViewModelTest {
    private val testDeepLinkString = "https://example.com/video"
    private lateinit var testDeepLinkUri: Uri

    @Mock
    private lateinit var mockVideoRepository: VideoRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testDeepLinkUri = Uri.parse(testDeepLinkString)
    }

    @Test
    fun getDeepLinkResult_validResultReturnsSuccess() {
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
        doReturn(video).`when`(mockVideoRepository).getVideoById(testDeepLinkString)

        val expectedResult = Result.Success(video)
        val result = DeepLinkViewModel.getDeepLinkVideo(testDeepLinkUri, mockVideoRepository)

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun getDeepLinkResult_nullResultReturnsError() {
        doReturn(null).`when`(mockVideoRepository).getVideoById(testDeepLinkString)

        val result = DeepLinkViewModel.getDeepLinkVideo(testDeepLinkUri, mockVideoRepository)

        Assert.assertTrue(result is Result.Error)
    }

    @Test
    fun getDeepLinkResult_jsonDataExceptionReturnsError() {
        doThrow(JsonDataException()).`when`(mockVideoRepository).getVideoById(testDeepLinkString)

        val result = DeepLinkViewModel.getDeepLinkVideo(testDeepLinkUri, mockVideoRepository)

        Assert.assertTrue(result is Result.Error)
    }
}
