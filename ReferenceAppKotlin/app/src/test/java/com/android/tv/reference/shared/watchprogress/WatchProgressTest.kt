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
import com.android.tv.reference.lifecycle.ext.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests the WatchProgress database interaction
 */
@RunWith(AndroidJUnit4::class)
class WatchProgressTest {

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var watchProgressDatabase: WatchProgressDatabase
    private lateinit var watchProgressDao: WatchProgressDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        watchProgressDatabase =
            Room.inMemoryDatabaseBuilder(context, WatchProgressDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        watchProgressDao = watchProgressDatabase.watchProgressDao()
    }

    @After
    fun tearDown() {
        watchProgressDatabase.close()
    }

    @Test
    fun basicWriteAndRead() = runBlocking {
        // Create and insert two WatchProgress instances
        val watchProgress1 = WatchProgress("test1", 123)
        val watchProgress2 = WatchProgress("test2", 0)
        watchProgressDao.insert(watchProgress1)
        watchProgressDao.insert(watchProgress2)

        // Read the WatchProgress data back out of the database
        val watchProgressRead1 =
            watchProgressDao.getWatchProgressByVideoId(watchProgress1.videoId).getOrAwaitValue()
        val watchProgressRead2 =
            watchProgressDao.getWatchProgressByVideoId(watchProgress2.videoId).getOrAwaitValue()

        // Verify the data matches what was supposed to be inserted
        assertThat(watchProgressRead1).isEqualTo(watchProgress1)
        assertThat(watchProgressRead2).isEqualTo(watchProgress2)
    }

    @Test
    fun writeOverwriteAndRead() = runBlocking {
        // Insert a WatchProgress
        val watchProgress = WatchProgress("test1", 0)
        watchProgressDao.insert(watchProgress)

        // Insert an update
        watchProgress.startPosition = 500
        watchProgressDao.insert(watchProgress)

        // Verify the update was stored
        val watchProgressRead =
            watchProgressDao.getWatchProgressByVideoId(watchProgress.videoId).getOrAwaitValue()
        assertThat(watchProgressRead).isEqualTo(watchProgress)
    }

    @Test
    fun writeAndClear() = runBlocking {
        // Insert a WatchProgress
        val watchProgress = WatchProgress("test1", 0)
        watchProgressDao.insert(watchProgress)

        // Delete all entries
        watchProgressDao.deleteAll()

        // Verify the inserted entry is no longer present
        val watchProgressRead =
            watchProgressDao.getWatchProgressByVideoId(watchProgress.videoId).value
        assertThat(watchProgressRead).isNull()
    }
}
