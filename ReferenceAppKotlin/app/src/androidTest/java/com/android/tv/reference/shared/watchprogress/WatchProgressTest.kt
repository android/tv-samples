/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


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
        watchProgressDatabase = Room.inMemoryDatabaseBuilder(context, WatchProgressDatabase::class.java).build()
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
        val watchProgressRead1 = watchProgressDao.getWatchProgressByVideoId(watchProgress1.videoId).blockForValue()
        val watchProgressRead2 = watchProgressDao.getWatchProgressByVideoId(watchProgress2.videoId).blockForValue()

        // Verify the data matches what was supposed to be inserted
        assertEquals(watchProgress1, watchProgressRead1)
        assertEquals(watchProgress2, watchProgressRead2)
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
        val watchProgressRead = watchProgressDao.getWatchProgressByVideoId(watchProgress.videoId).blockForValue()
        assertEquals(watchProgress, watchProgressRead)
    }

    @Test
    fun writeAndClear() = runBlocking {
        // Insert a WatchProgress
        val watchProgress = WatchProgress("test1", 0)
        watchProgressDao.insert(watchProgress)

        // Delete all entries
        watchProgressDao.deleteAll()

        // Verify the inserted entry is no longer present
        val watchProgressRead = watchProgressDao.getWatchProgressByVideoId(watchProgress.videoId).value
        assertNull(watchProgressRead)
    }
}

// Extension function to easily observe the LiveData value
private fun <T> LiveData<T>.blockForValue(): T? {
    var result: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(value: T?) {
            result = value
            removeObserver(this)
            latch.countDown()
        }
    }
    observeForever(observer)

    if (!latch.await(1, TimeUnit.SECONDS)) {
        removeObserver(observer)
        throw TimeoutException("LiveData value not set")
    }

    return result
}
