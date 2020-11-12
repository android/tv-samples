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
package com.android.tv.reference.playnext

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.tvprovider.media.tv.TvContractCompat
import androidx.tvprovider.media.tv.WatchNextProgram
import com.android.tv.reference.playnext.PlayNextTest.FakeInMemoryTvProvider.Companion.TEST_VIDEO_DURATION_MILLIS
import com.android.tv.reference.playnext.PlayNextTest.FakeInMemoryTvProvider.Companion.TEST_VIDEO_ID
import com.android.tv.reference.playnext.PlayNextTest.FakeInMemoryTvProvider.Companion.TEST_VIDEO_NAME
import com.android.tv.reference.playnext.PlayNextTest.FakeInMemoryTvProvider.Companion.TEST_VIDEO_PLAYBACK_POSITION_MILLIS
import com.android.tv.reference.playnext.PlayNextTest.FakeInMemoryTvProvider.Companion.video_movie
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.datamodel.VideoType.MOVIE
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import java.time.Duration

/**
 * Test cases to test adding / updating / removing content
 * from Play Next channel in Home Screen.
 */
@RunWith(AndroidJUnit4::class)
class PlayNextTest {

    private lateinit var fakeInMemoryTvProvider: FakeInMemoryTvProvider
    companion object {
        private const val DELETE_FAILED = -1
    }

    @Before
    fun setUp() {
        // Register fakeInMemoryTvProvider to use as a mock for TvContentProvider
        // for all robolectric tests.
        fakeInMemoryTvProvider = Robolectric.setupContentProvider(
            FakeInMemoryTvProvider::class.java,
            TvContractCompat.AUTHORITY
        )
    }

    @Test
    /**
     * Test adding unfinished video to Play Next
     */
    fun insertVideoToPlayNext() {

        // Actual : Call the method.
        PlayNextHelper.insertOrUpdateVideoToPlayNext(
            video_movie, TEST_VIDEO_PLAYBACK_POSITION_MILLIS,
            TEST_VIDEO_DURATION_MILLIS, ApplicationProvider.getApplicationContext()
        )

        // Expected : verify if program was added to play next.
        val watchList =
            PlayNextHelper.getWatchNextPrograms(ApplicationProvider.getApplicationContext())
        val program =
            watchList.firstOrNull { it.internalProviderId == TEST_VIDEO_ID }

        // Verify the correct metadata was set.
        assertThat(program).isNotNull()
        assertThat(program?.internalProviderId).isEqualTo(TEST_VIDEO_ID)
        assertThat(program?.title).isEqualTo(TEST_VIDEO_NAME)
    }

    @Test
    /**
     * Remove finished video from Play next channel.
     */
    fun removeVideoFromPlayNext() {

        PlayNextHelper.insertOrUpdateVideoToPlayNext(
            video_movie,
            TEST_VIDEO_PLAYBACK_POSITION_MILLIS,
            TEST_VIDEO_DURATION_MILLIS,
            ApplicationProvider.getApplicationContext()
        )

        // Actual : Call the method for delete.
        val deletedId = PlayNextHelper.removeVideoFromPlayNext(
            ApplicationProvider.getApplicationContext(), video_movie
        )

        // Expected : (deletedId is returned if delete was successful,
        // null is returned if delete fails in actual code.
        assertThat(deletedId).isNotNull()
        // -1 is returned if fake content provider fails to delete.
        assertThat(deletedId).isNotEqualTo(DELETE_FAILED)
    }

    @Test
    /**
     * Update video metadata in Play next channel.
     * Example: Update playback time.
     */
    fun updateVideoInPlayNext() {

        // Actual : Call the method.
        // Add the entry to Play next row.
        PlayNextHelper.insertOrUpdateVideoToPlayNext(
            video_movie, TEST_VIDEO_PLAYBACK_POSITION_MILLIS,
            TEST_VIDEO_DURATION_MILLIS, ApplicationProvider.getApplicationContext()
        )

        // Update the video data. eg: update playback position by 2 minutes.
        // This value will be picked while inserting the video back.
        val updatedVideoPlaybackPosition =
                TEST_VIDEO_PLAYBACK_POSITION_MILLIS +
                Duration.ofMinutes(2).toMillis().toInt()

        // Call the updated method.
        PlayNextHelper.insertOrUpdateVideoToPlayNext(
            video_movie, updatedVideoPlaybackPosition,
            TEST_VIDEO_DURATION_MILLIS, ApplicationProvider.getApplicationContext()
        )

        // Expected : verify if program was updated in play next.
        val watchList =
            PlayNextHelper.getWatchNextPrograms(ApplicationProvider.getApplicationContext())
        val program =
            watchList.firstOrNull { it.internalProviderId == TEST_VIDEO_ID }

        // Verify the correct updated metadata was set
        assertThat(program).isNotNull()
        assertThat(program?.internalProviderId).isEqualTo(TEST_VIDEO_ID)
        assertThat(program?.title).isEqualTo(TEST_VIDEO_NAME)
        assertThat(program?.lastPlaybackPositionMillis).isEqualTo(updatedVideoPlaybackPosition)
    }

    /**
     * This acts like a fake provider mirroring the TvContentProvider
     * (since we set the same authority).
     * For RoboElectric tests, by default fake provider will be used wherever the code refers to
     * TvContentProvider. This creates a new instance every time and clears out when done,
     * hence acts like in-memory.
     */
    class FakeInMemoryTvProvider : ContentProvider() {

        private val columns = arrayOf(
            TvContractCompat.WatchNextPrograms._ID,
            TvContractCompat
                .WatchNextPrograms.COLUMN_TITLE,
            TvContractCompat.WatchNextPrograms.COLUMN_INTERNAL_PROVIDER_ID,
            TvContractCompat.WatchNextPrograms.COLUMN_LAST_PLAYBACK_POSITION_MILLIS
        )

        // For testing purpose, mimic a mutable list rather than an actual db.
        private val valuesInMemory = mutableListOf<WatchNextProgram>()
        //Persist rowId to auto increment id for every insert
        private var rowId = 0L

        override fun insert(uri: Uri, values: ContentValues?): Uri? {
            //insert a new row
            rowId++

            val program = WatchNextProgram.Builder()
                .setId(rowId)
                .setTitle(values?.getAsString(TvContractCompat.WatchNextPrograms.COLUMN_TITLE))
                .setInternalProviderId(
                    values?.getAsString(
                        TvContractCompat.WatchNextPrograms
                            .COLUMN_INTERNAL_PROVIDER_ID
                    )
                )
                .setLastPlaybackPositionMillis(
                    values!!.getAsInteger(
                        TvContractCompat.WatchNextPrograms.COLUMN_LAST_PLAYBACK_POSITION_MILLIS
                    )
                )
                .build()
            this.valuesInMemory.add(program)
            return TvContractCompat.buildWatchNextProgramUri(rowId)
        }

        override fun query(
            uri: Uri,
            projection: Array<out String>?,
            selection: String?,
            selectionArgs: Array<out String>?,
            sortOrder: String?
        ): Cursor? {
            val cursor = MatrixCursor(columns)
            valuesInMemory.forEach {
                cursor.addRow(
                    arrayOf(
                        it.id,
                        it.title,
                        it.internalProviderId,
                        it.lastPlaybackPositionMillis
                    )
                )
            }
            return cursor
        }

        override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
            val id = uri.lastPathSegment?.toLong() ?: -1L
            val removed = this.valuesInMemory.removeIf { it.id == id }
            return if (removed)
                id.toInt()
            else
                DELETE_FAILED
        }

        override fun onCreate(): Boolean {
            // This is an in-memory fake so create is trivial.
            return true
        }

        override fun update(
            uri: Uri,
            values: ContentValues?,
            selection: String?,
            selectionArgs: Array<out String>?
        ): Int {
            val id = uri.lastPathSegment?.toLong() ?: return 0

            val program = WatchNextProgram.Builder()
                .setId(id)
                .setTitle(values?.getAsString(TvContractCompat.WatchNextPrograms.COLUMN_TITLE))
                .setInternalProviderId(
                    values?.getAsString(
                        TvContractCompat
                            .WatchNextPrograms.COLUMN_INTERNAL_PROVIDER_ID
                    )
                )
                .setLastPlaybackPositionMillis(
                    values!!.getAsInteger(
                        TvContractCompat.WatchNextPrograms.COLUMN_LAST_PLAYBACK_POSITION_MILLIS
                    )
                )
                .build()

            // Since kotlin collections don't directly provide a replace function, for testing
            // purpose, remove existing and add new entry to avoid complexity.
            this.valuesInMemory.removeIf { it.id == id }
            this.valuesInMemory.add(program)
            return 1
        }

        override fun getType(uri: Uri): String? {
            // This is an in-memory fake so getType is trivial.
            return ""
        }

        companion object {

            // values taken from api.json from resources.
            const val TEST_VIDEO_ID =
                "https://atv-reference-app.firebaseapp.com/movies-tech/seomb-seo-mythbusting-101"
            var TEST_VIDEO_NAME = "SEO Mythbusting 101"
            private const val TEST_VIDEO_DESCRIPTION =
                "In this first - introductory - episode of SEO Mythbusting, Martin Splitt (WebMaster Trends Analyst, Google) and his guest Juan Herrera (Angular GDE, Wed Developer at Parkside) discuss the very basics of SEO."
            private const val TEST_VIDEO_URI =
                "https://storage.googleapis.com/atv-reference-app-videos/movies-tech/seomb-seo-mythbusting-101.mp4"
            private const val TEST_VIDEO_PLACEHOLDER_URI =
                "https://storage.googleapis.com/atv-reference-app-videos/movies-tech/seomb-seo-mythbusting-101.mp4"
            private const val TEST_VIDEO_THUMBNAIL_URI =
                "https://storage.googleapis.com/atv-reference-app-videos/movies-tech/seomb-seo-mythbusting-101-thumbnail.png"

            // Adjust the playback position and duration with the time you want to test
            // Add approximate duration and playback position.
            var TEST_VIDEO_DURATION_MILLIS = Duration.ofMinutes(40).toMillis().toInt()
            var TEST_VIDEO_PLAYBACK_POSITION_MILLIS = Duration.ofMinutes(17).toMillis().toInt()
            var TEST_VIDEO_DURATION = "PT00H10M"

            var video_movie = Video(
                id = TEST_VIDEO_ID,
                name = TEST_VIDEO_NAME,
                description = TEST_VIDEO_DESCRIPTION,
                uri = TEST_VIDEO_URI,
                videoUri = TEST_VIDEO_PLACEHOLDER_URI,
                thumbnailUri = TEST_VIDEO_THUMBNAIL_URI,
                backgroundImageUri = TEST_VIDEO_PLACEHOLDER_URI,
                category = "",
                videoType = MOVIE,
                duration = TEST_VIDEO_DURATION
            )
        }
    }
}
