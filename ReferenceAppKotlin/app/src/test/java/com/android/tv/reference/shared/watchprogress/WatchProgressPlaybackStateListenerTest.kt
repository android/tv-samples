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
package com.android.tv.reference.shared.watchprogress

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.tv.reference.coroutines.CoroutinesTestRule
import com.android.tv.reference.lifecycle.ext.getOrAwaitValue
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.datamodel.VideoType
import com.android.tv.reference.playback.VideoPlaybackState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class WatchProgressPlaybackStateListenerTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var playbackStateListener: WatchProgressPlaybackStateListener
    private lateinit var watchProgressDatabase: WatchProgressDatabase
    private lateinit var watchProgressRepository: WatchProgressRepository
    private lateinit var testScope: TestCoroutineScope

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        watchProgressDatabase =
            Room.inMemoryDatabaseBuilder(context, WatchProgressDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        val watchProgressDao = watchProgressDatabase.watchProgressDao()
        watchProgressRepository = WatchProgressRepository(watchProgressDao)

        testScope = TestCoroutineScope(coroutinesTestRule.testDispatcher)
        playbackStateListener = WatchProgressPlaybackStateListener(
            watchProgressRepository,
            testScope,
            coroutinesTestRule.testDispatcher
        )
    }

    @After
    fun tearDown() {
        watchProgressDatabase.close()
    }

    @Test
    fun onChanged_pause_updatesPosition() {
        val position = 10L

        testScope.runBlockingTest {
            playbackStateListener.onChanged(VideoPlaybackState.Pause(VIDEO, position))
        }

        val watchProgress =
            watchProgressRepository.getWatchProgressByVideoId(VIDEO.id).getOrAwaitValue()
        assertThat(watchProgress.startPosition).isEqualTo(position)
    }

    @Test
    fun onChanged_end_updatesPosition() {
        testScope.runBlockingTest {
            playbackStateListener.onChanged(VideoPlaybackState.End(VIDEO))
        }

        val watchProgress =
            watchProgressRepository.getWatchProgressByVideoId(VIDEO.id).getOrAwaitValue()
        assertThat(watchProgress.startPosition).isEqualTo(VIDEO.duration().toMillis())
    }

    companion object {
        private val VIDEO = Video(
            id = "id",
            name = "name",
            description = "description",
            uri = "https://example.com/id",
            videoUri = "https://example.com/video/id",
            thumbnailUri = "https://example.com/thumbnail/id",
            backgroundImageUri = "https://example.com/background/id",
            category = "Test",
            videoType = VideoType.MOVIE,
            duration = "PT00H10M" // 10 Minutes
        )
    }
}
