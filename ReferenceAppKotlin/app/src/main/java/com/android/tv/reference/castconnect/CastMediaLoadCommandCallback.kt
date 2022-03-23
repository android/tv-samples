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
package com.android.tv.reference.castconnect

import android.app.Application
import com.android.tv.reference.repository.VideoRepository
import com.android.tv.reference.repository.VideoRepositoryFactory
import com.android.tv.reference.shared.datamodel.Video
import com.google.android.gms.cast.MediaError
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.tv.media.MediaException
import com.google.android.gms.cast.tv.media.MediaLoadCommandCallback
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import timber.log.Timber

/**
 * MediaLoadCommandCallback.onLoad() is invoked when the MediaManager detects the intent is a
 * load request, this method receives the load request's data and converts it to a video object.
 * Once converted, the video is played by the local player. The MediaManager is then updated
 * with the MediaLoadRequest and broadcasts the MediaStatus to the connected senders.
 *
 * The load request's data comes from the sender app, such as the mobile or web complimentary apps
 * and thus the data contract should be already defined most likely would be sharing the same data
 * catalogue. The load request data is a media info object and as long as that object is populated
 * with the right fields, this receiver part would remain the same. What varies is the actual data
 * values in the MediaInfo fields.
 */
class CastMediaLoadCommandCallback(
    var onLoaded: (Video, MediaLoadRequestData) -> Unit,
    private val application: Application
) :
    MediaLoadCommandCallback() {

    override fun onLoad(
        senderId: String?,
        mediaLoadRequestData: MediaLoadRequestData
    ): Task<MediaLoadRequestData> {
        return Tasks.call {
            var videoToPlay = convertLoadRequestToVideo(
                mediaLoadRequestData, VideoRepositoryFactory.getVideoRepository(application)
            )
            if (videoToPlay != null) {
                onLoaded(videoToPlay, mediaLoadRequestData)
            } else {
                Timber.w("Failed to convert cast load request to application-specific video")
            }

            mediaLoadRequestData
        }
    }

    /**
     * Retrieve the appropriate application-specific content or media object from the MediaInfo
     * object that is passed from the sender application. The format of fields populated in the
     * MediaInfo object follow a defined contract that is decided between the sender and the
     * receiver, and is specific to each application so as to be able to identify the appropriate
     * content at the receiver's end. The contentId parameter is an application-specific unique
     * identifier for the content that is used here. Several other parameters of MediaInfo class can
     * also be used to provide information such as contentUrl, duration, metadata, tracks and
     * breaks. For more details on the available parameters, refer to the following link -
     * https://developers.google.com/cast/docs/reference/chrome/chrome.cast.media.MediaInfo
     */
    fun convertLoadRequestToVideo(
        mediaLoadRequestData: MediaLoadRequestData,
        videoRepository: VideoRepository
    ): Video? {
        val mediaInfo: MediaInfo = mediaLoadRequestData.mediaInfo ?: return null
        return videoRepository.getVideoById(mediaInfo.contentId)
    }
}
