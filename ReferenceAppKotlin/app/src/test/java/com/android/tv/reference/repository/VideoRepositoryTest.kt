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
package com.android.tv.reference.repository

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.tv.reference.repository.FakeVideoRepository.Companion.episodes
import com.android.tv.reference.repository.FakeVideoRepository.Companion.video_movie
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VideoRepositoryTest {

    private lateinit var fakeRepository: FakeVideoRepository

    @Before
    fun setUp() {
        fakeRepository = FakeVideoRepository(
            ApplicationProvider.getApplicationContext() as Application)
    }

    @Test
    fun getNextEpisodeInSeason() {
        val nextEpisode = fakeRepository.getNextEpisodeInSeries(episodes[0])

        var expectedEpisode = episodes[1]

        assertThat(nextEpisode).isNotNull()
        assertThat(nextEpisode).isEqualTo(expectedEpisode)
    }

    @Test
    fun getNextEpisodeInNextSeason() {
        val nextEpisode = fakeRepository.getNextEpisodeInSeries(episodes[1])

        var expectedEpisode = episodes[2]

        assertThat(nextEpisode).isNotNull()
        assertThat(nextEpisode).isEqualTo(expectedEpisode)
    }

    @Test
    fun getNextEpisodeOfTheEnd() {
        val nextEpisode = fakeRepository.getNextEpisodeInSeries(episodes[2])

        assertThat(nextEpisode).isNull()
    }

    @Test
    fun getNextEpisodeOfMovie() {
        val nextEpisode = fakeRepository.getNextEpisodeInSeries(video_movie)

        assertThat(nextEpisode).isNull()
    }

}
