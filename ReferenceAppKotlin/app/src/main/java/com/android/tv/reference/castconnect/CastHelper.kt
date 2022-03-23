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
import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
import com.android.tv.reference.shared.datamodel.Video
import com.google.android.gms.cast.tv.CastReceiverContext
import com.google.android.gms.cast.tv.media.MediaManager

/**
 * Helper class to handle and process interactions of the app with the Cast SDK.
 */
class CastHelper(val loadVideo: (Video) -> Unit, private val application: Application) {

    private val castReceiverContext = CastReceiverContext.getInstance()

    /**
     * Check if the intent is recognized by the Cast SDK. If so, process the intent and return
     * true, otherwise return false.
     */
    fun validateAndProcessCastIntent(intent: Intent): Boolean {
        val mediaManager: MediaManager = castReceiverContext.mediaManager

        mediaManager.setMediaLoadCommandCallback(
            CastMediaLoadCommandCallback(
                { videoToCast, mediaLoadRequestData ->

                    // Callback passed to MediaLoadCommandCallback to load the video for playback
                    // after Cast intent is processed.
                    loadVideo(videoToCast)

                    // Update media metadata and state
                    mediaManager.setDataFromLoad(mediaLoadRequestData)
                    mediaManager.broadcastMediaStatus()
                },
                application
            )
        )

        // Pass the intent to the Cast SDK through MediaManager object of CastReceiverContext.
        if (mediaManager.onNewIntent(intent)) {
            // The Cast SDK recognizes the intent and calls the MediaLoadCommandCallback, so return
            // true.
            return true
        }

        // Clears all overrides in the modifier, additional status not included in cast.
        mediaManager.mediaStatusModifier.clear()

        // The intent was not recognized by the Cast SDK, so return false.
        return false
    }
}
