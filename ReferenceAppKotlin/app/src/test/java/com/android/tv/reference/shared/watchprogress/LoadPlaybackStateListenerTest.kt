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
import com.android.tv.reference.playback.FakePlaybackStateMachine
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.datamodel.VideoType
import com.android.tv.reference.playback.VideoPlaybackState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Duration

@RunWith(AndroidJUnit4::class)
class LoadPlaybackStateListenerTest {

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var loadPlaybackStateListener: LoadPlaybackStateListener

    private lateinit var watchProgressDatabase: WatchProgressDatabase
    private lateinit var watchProgressRepository: WatchProgressRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        watchProgressDatabase =
            Room.inMemoryDatabaseBuilder(context, WatchProgressDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        val watchProgressDao = watchProgressDatabase.watchProgressDao()
        watchProgressRepository = WatchProgressRepository(watchProgressDao)

        loadPlaybackStateListener = LoadPlaybackStateListener(
            FakePlaybackStateMachine,
            watchProgressRepository,
        )
    }

    @After
    fun tearDown() {
        watchProgressDatabase.close()
        FakePlaybackStateMachine.reset()
        loadPlaybackStateListener.onDestroy()
    }

    @Test
    fun onChanged_loadState_setsStateToPrepare() {
        val expectedStartPosition = 10L
        runBlocking {
            watchProgressRepository.insert(WatchProgress(VIDEO.id, expectedStartPosition))
        }

        loadPlaybackStateListener.onChanged(VideoPlaybackState.Load(VIDEO))

        assertThat(FakePlaybackStateMachine.playbackState)
            .isEqualTo(VideoPlaybackState.Prepare(VIDEO, expectedStartPosition))
    }

    @Test
    fun onChanged_loadState_noWatchProgress_setsStateToPrepareAtPositionZero() {
        loadPlaybackStateListener.onChanged(VideoPlaybackState.Load(VIDEO))

        assertThat(FakePlaybackStateMachine.playbackState).isEqualTo(
            VideoPlaybackState.Prepare(
                VIDEO,
                startPosition = 0L
            )
        )
    }

    @Test
    fun onChanged_loadState_watchProgressIsDuration_setsStateToPrepareAtPositionZero() {
        runBlocking {
            watchProgressRepository.insert(
                WatchProgress(
                    VIDEO.id,
                    startPosition = Duration.parse(VIDEO.duration).toMillis()
                )
            )
        }

        loadPlaybackStateListener.onChanged(VideoPlaybackState.Load(VIDEO))

        assertThat(FakePlaybackStateMachine.playbackState).isEqualTo(
            VideoPlaybackState.Prepare(
                VIDEO,
                startPosition = 0L
            )
        )
    }

    @Test
    fun onChanged_unregistersWatchProgressObserverWhenNotInLoadState() {
        runBlocking {
            watchProgressRepository.insert(WatchProgress(VIDEO.id, startPosition = 10L))
        }
        loadPlaybackStateListener.onChanged(VideoPlaybackState.Load(VIDEO))

        // Progress the state after load.
        loadPlaybackStateListener.onChanged(VideoPlaybackState.Prepare(VIDEO, 10L))
        runBlocking {
            // This insert triggers another update to the WatchProgress Observer.
            watchProgressRepository.insert(WatchProgress(VIDEO.id, startPosition = 20L))
        }

        assertThat(FakePlaybackStateMachine.playbackStateHistory).hasSize(1)
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
