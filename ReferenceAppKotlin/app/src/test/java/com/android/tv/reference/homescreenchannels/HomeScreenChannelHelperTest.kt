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
package com.android.tv.reference.homescreenchannels

import android.database.Cursor
import android.media.tv.TvContract
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewChannelHelper
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.TvContractCompat
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.datamodel.VideoType
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class HomeScreenChannelHelperTest {

    @Mock
    private lateinit var previewChannelHelper: PreviewChannelHelper

    private lateinit var channelHelper: HomeScreenChannelHelper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        channelHelper = HomeScreenChannelHelper(previewChannelHelper)
    }

    @Test
    fun addProgramsToChannel() {
        // Prepare the test data
        val videos = listOf(
            Video(
                id = TEST_VIDEO_ID,
                name = TEST_VIDEO_NAME,
                description = TEST_VIDEO_DESCRIPTION,
                uri = TEST_VIDEO_URI,
                videoUri = TEST_VIDEO_PLACEHOLDER_URI,
                thumbnailUri = TEST_VIDEO_THUMBNAIL_URI,
                backgroundImageUri = TEST_VIDEO_PLACEHOLDER_URI,
                category = "",
                videoType = VideoType.EPISODE,
                duration = TEST_VIDEO_DURATION,
                seriesUri = TEST_VIDEO_SERIES_URI,
                seasonUri = TEST_VIDEO_SEASON_URI,
                seasonNumber = TEST_VIDEO_SEASON_NUMBER,
                episodeNumber = TEST_VIDEO_EPISODE_NUMBER
            )
        )
        val testChannelId = 123L

        // Call the method
        channelHelper.addProgramsToChannel(videos, testChannelId)

        // Capture the argument passed to publishPreviewProgram
        val captor = ArgumentCaptor.forClass(PreviewProgram::class.java)
        Mockito.verify(previewChannelHelper).publishPreviewProgram(captor.capture())

        // Verify the correct metadata was set
        val program = captor.value
        assertThat(program.internalProviderId).isEqualTo(TEST_VIDEO_ID)
        assertThat(program.title).isEqualTo(TEST_VIDEO_NAME)
        assertThat(program.description).isEqualTo(TEST_VIDEO_DESCRIPTION)
        assertThat(program.intentUri.toString()).isEqualTo(TEST_VIDEO_URI)
        assertThat(program.type).isEqualTo(TvContractCompat.PreviewPrograms.TYPE_TV_EPISODE)
        assertThat(program.channelId).isEqualTo(testChannelId)
    }

    @Test
    fun getChannelByInternalProviderId_withMatch() {
        // Prepare all the mock channels
        val rightId = "right_id"
        val correctPreviewChannel = createMockPreviewChannel(rightId)
        val allChannels = listOf(
            createMockPreviewChannel("123"),
            correctPreviewChannel,
            createMockPreviewChannel("456"),
            createMockPreviewChannel("xyz")
        )

        // The all channels list includes the correct mock mixed in
        Mockito.`when`(previewChannelHelper.allChannels).thenReturn(allChannels)

        // Get the preview channel and verify it matches
        val returnedChannel = channelHelper.getChannelByInternalProviderId(rightId)
        assertThat(returnedChannel).isEqualTo(correctPreviewChannel)
    }

    @Test
    fun getChannelByInternalProviderId_withoutMatch() {
        // Prepare all the mock channels
        val allChannels = listOf(
            createMockPreviewChannel("123"),
            createMockPreviewChannel("456"),
            createMockPreviewChannel("xyz")
        )

        // The all channels list does not include what we want
        Mockito.`when`(previewChannelHelper.allChannels).thenReturn(allChannels)

        // Verify we correct get no preview channel back since none match the ID
        val returnedChannel = channelHelper.getChannelByInternalProviderId("an id not in the list")
        assertThat(returnedChannel).isNull()
    }

    @Test
    fun getProgramIdsFromCursor_validItems() {
        // Create the mock
        val cursorMock = Mockito.mock(Cursor::class.java)

        // Set up the fake data for the cursor to return
        val idColumnIndex = 0
        val browsableColumnIndex = 1
        var currentCursorPosition = -1
        val testIds = arrayOf("first id", "second id", "third id")
        val testBrowsableInts = arrayOf(1, 0, 1)

        // Set up the cursor to use the mock data
        Mockito.`when`(cursorMock.moveToNext()).thenAnswer {
            currentCursorPosition++
            currentCursorPosition < testIds.size
        }
        Mockito.`when`(
            cursorMock.getColumnIndex(TvContract.PreviewPrograms.COLUMN_INTERNAL_PROVIDER_ID)
        )
            .thenReturn(idColumnIndex)
        Mockito.`when`(cursorMock.getColumnIndex(TvContract.PreviewPrograms.COLUMN_BROWSABLE))
            .thenReturn(browsableColumnIndex)
        Mockito.`when`(cursorMock.getString(idColumnIndex)).then { testIds[currentCursorPosition] }
        Mockito.`when`(cursorMock.getInt(browsableColumnIndex))
            .then { testBrowsableInts[currentCursorPosition] }

        // Calculate the expected results
        var expectedBrowsableCount = 0
        testBrowsableInts.forEach { expectedBrowsableCount += it }
        val expectedSetOfBrowsableIds = HashSet<String>()
        val expectedSetOfNonBrowsableIds = HashSet<String>()
        testIds.zip(testBrowsableInts) { id, browsable ->
            if (browsable == 0) expectedSetOfNonBrowsableIds.add(
                id
            ) else expectedSetOfBrowsableIds.add(id)
        }

        // Trigger the method
        val programIdsInChannel = channelHelper.getProgramIdsFromCursor(cursorMock)

        // Verify the output count and IDs match
        assertThat(programIdsInChannel.getBrowsableProgramIds())
            .containsExactlyElementsIn(expectedSetOfBrowsableIds)
        assertThat(programIdsInChannel.getNonBrowsableProgramIds())
            .containsExactlyElementsIn(expectedSetOfNonBrowsableIds)
    }

    @Test
    fun getProgramIdsInChannel_emptyCursor() {
        // Create the mock
        val cursorMock = Mockito.mock(Cursor::class.java)

        // Mock moveToNext to return false to signify no data
        Mockito.`when`(cursorMock.moveToNext()).thenReturn(false)

        // Trigger the method
        val programIdsInChannel = channelHelper.getProgramIdsFromCursor(cursorMock)

        // Verify the output is an empty ProgramIdsInChannel
        assertThat(programIdsInChannel.getBrowsableProgramIds()).isEmpty()
        assertThat(programIdsInChannel.getNonBrowsableProgramIds()).isEmpty()
    }

    private fun createMockPreviewChannel(internalProviderId: String): PreviewChannel {
        val mock = Mockito.mock(PreviewChannel::class.java)
        Mockito.`when`(mock.internalProviderId).thenReturn(internalProviderId)
        return mock
    }

    companion object {
        private const val TEST_VIDEO_ID = "TESTID123"
        private const val TEST_VIDEO_NAME = "Test Video"
        private const val TEST_VIDEO_DESCRIPTION = "In a world where test videos...."
        private const val TEST_VIDEO_URI =
            "https://atv-reference-app.firebaseapp.com/content/$TEST_VIDEO_ID"
        private const val TEST_VIDEO_PLACEHOLDER_URI = "https://example.com/uri"
        private const val TEST_VIDEO_THUMBNAIL_URI = "https://example.com/thumbnail"
        private const val TEST_VIDEO_SERIES_URI = "https://example.com/series"
        private const val TEST_VIDEO_SEASON_URI = "https://example.com/season"
        private const val TEST_VIDEO_SEASON_NUMBER = "1"
        private const val TEST_VIDEO_EPISODE_NUMBER = "1"
        private const val TEST_VIDEO_DURATION = "PT00H08M"
    }
}
