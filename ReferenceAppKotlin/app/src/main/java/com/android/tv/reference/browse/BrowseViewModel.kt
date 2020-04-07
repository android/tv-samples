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

package com.android.tv.reference.browse

import android.text.method.BaseKeyListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.datamodel.VideoGroup

class BrowseViewModel : ViewModel() {

    var browseContent = MutableLiveData<List<VideoGroup>>()

    init {
        browseContent.value = temp__getFakeContentList()
    }

    /**
     * Temporary method for getting some fake content to work with
     */
    private fun temp__getFakeContentList() : List<VideoGroup> {
        val videoGroups = ArrayList<VideoGroup>();
        // TODO include several example URIs
        var videoCount = 0

        for (i in 1..3) {
            val videos = ArrayList<Video>()
            for (j in 0..5) {
                videos.add(Video(
                    name = "Video $videoCount",
                    videoUri = "https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload.mp4",
                    thumbnailUri = "https://android-tv-classics.firebaseapp.com/content/jazzed_honeymoon/poster_art_jazzed_honeymoon.jpg",
                    backgroundImageUri = "https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Instant%20Upload/bg.jpg",
                    category = "Category $i"
                ))
                videoCount++
            }
            videoGroups.add(VideoGroup("Category $i", videos))
        }

        return videoGroups
    }

}