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
package com.android.tv.reference.playback

import android.support.v4.media.session.MediaSessionCompat
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.datamodel.VideoType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

class SingleVideoQueueNavigatorTest {

    @Test
    fun getMediaDescription() {
        val video = Video(
            id = "an ID",
            name = "Amazing Video Name",
            description = "What a description!",
            uri = TEST_VIDEO_PLACEHOLDER_URI,
            videoUri = TEST_VIDEO_PLACEHOLDER_URI,
            thumbnailUri = TEST_VIDEO_PLACEHOLDER_URI,
            backgroundImageUri = TEST_VIDEO_PLACEHOLDER_URI,
            category = "Category",
            videoType = VideoType.EPISODE,
            duration = "PT00H10M",
            seriesUri = "Amazing Video Series",
            seasonUri = "Amazing Video Season",
            seasonNumber = "1",
            episodeNumber = "1"
        )
        val mediaSession = Mockito.mock(MediaSessionCompat::class.java)
        val simpleQueueNavigator = SingleVideoQueueNavigator(video, mediaSession)
        val mediaDescriptionCompat = simpleQueueNavigator.getMediaDescription(video)

        // Verify all expected fields were copied.
        assertEquals(video.id, mediaDescriptionCompat.mediaId)
        assertEquals(video.name, mediaDescriptionCompat.title)
        assertEquals(video.description, mediaDescriptionCompat.description)
    }

    companion object {
        private const val TEST_VIDEO_PLACEHOLDER_URI = "https://example.com/uri"
    }
}
