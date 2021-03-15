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
package com.android.tv.reference.playnext

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.tv.reference.repository.VideoRepositoryFactory
import com.android.tv.reference.shared.datamodel.VideoType
import timber.log.Timber

/**
 * Worker triggered by WorkManager to add/update/remove video to Play Next Channel.
 *
 * <code>
 * WorkManager.getInstance(context).enqueue(OneTimeWorkRequest.Builder(PlayNextWorker::class.java).build())
 * </code>
 */
class PlayNextWorker(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {

    /**
     * Worker thread to add/update/remove content from Play Next Channel.
     * Events triggered from player state change events &
     * playback lifecycle events (onPause) are consumed here.
     */
    @SuppressLint("RestrictedApi")
    // Suppress RestrictedApi due to https://b.corp.google.com/issues/138150076
    override fun doWork(): Result {

        // Step 1 : get the video information from the "inputData".
        val videoId = inputData.getString(PlayNextHelper.VIDEO_ID)
        val watchPosition =
            inputData.getLong(PlayNextHelper.CURRENT_POSITION, /* defaultValue= */ 0)
        val state = inputData.getString(PlayNextHelper.PLAYER_STATE)
        Timber.v("Work Manager called watch id $videoId , watchTime $watchPosition")

        // Step 2 : Check for invalid inputData.
        // If videoId is invalid, abort worker and return.
        if (videoId.isNullOrEmpty()) {
            Timber.e("Error.Invalid entry for Play Next. videoid: $videoId")
            return Result.failure()
        }
        // Check for invalid player state.
        if ((state != PlayNextHelper.PLAY_STATE_PAUSED) and
            (state != PlayNextHelper.PLAY_STATE_ENDED)
        ) {
            Timber.e("Error.Invalid entry for Play Next. Player state: $state")
            return Result.failure()
        }

        // Step 3: Get video object from videoId to be added to play next.
        val video =
            VideoRepositoryFactory.getVideoRepository(context.applicationContext as Application)
                .getVideoById(videoId)

        Timber.v(" Get video from video list: watched video id  $videoId ,retrieved vid $video.id")

        // Step 4 : Handle Play Next for different types of content.
        when (video?.videoType) {
            VideoType.MOVIE -> {
                Timber.v("Add Movie to Play Next : id = ${video.id}")
                PlayNextHelper.handlePlayNextForMovie(video, watchPosition.toInt(), state, context)
            }
            VideoType.EPISODE -> {
                Timber.v("Add Episode to Play Next : id = ${video.id}")
                PlayNextHelper.handlePlayNextForEpisode(
                    video, watchPosition.toInt(), state,
                    VideoRepositoryFactory.getVideoRepository(
                        context.applicationContext as Application), context)
            }
            VideoType.CLIP -> Timber.w(
                "NOT recommended to add Clips / Trailers /Short videos to Play Next "
            )
            else -> Timber.e("Invalid category for Video Type: ${video?.videoType}")
        }

        Timber.v("PlayNext Programs worker finished")
        return Result.success()
    }

}
