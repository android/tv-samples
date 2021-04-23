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
package com.android.tv.reference.watchnext

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.WorkManagerTestInitHelper
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.datamodel.VideoType
import com.android.tv.reference.playback.VideoPlaybackState
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WatchNextPlaybackStateListenerTest {

    private val context = ApplicationProvider.getApplicationContext() as Context

    private val watchNextPlaybackStateListener = WatchNextPlaybackStateListener(context)

    private lateinit var workManager: WorkManager

    @Before
    fun setUp() {
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
        workManager = WorkManager.getInstance(context)
    }

    @Test
    fun onChanged_pauseState_enqueuesWorkRequest() {
        watchNextPlaybackStateListener.onChanged(VideoPlaybackState.Pause(VIDEO, position = 10L))

        val workInfos: List<WorkInfo> =
            workManager.getWorkInfosByTag(WatchNextWorker::class.java.name).get()
        assertThat(workInfos).hasSize(1)
    }

    @Test
    fun onChanged_endState_enqueuesWorkRequest() {
        watchNextPlaybackStateListener.onChanged(VideoPlaybackState.End(VIDEO))

        val workInfos: List<WorkInfo> =
            workManager.getWorkInfosByTag(WatchNextWorker::class.java.name).get()
        assertThat(workInfos).hasSize(1)
    }

    @Test
    fun onChanged_nonPauseOrEndState_doesNotEnqueWork() {
        watchNextPlaybackStateListener.onChanged(VideoPlaybackState.Load(VIDEO))

        val workInfos: List<WorkInfo> =
            workManager.getWorkInfosByTag(WatchNextWorker::class.java.name).get()
        assertThat(workInfos).isEmpty()
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
