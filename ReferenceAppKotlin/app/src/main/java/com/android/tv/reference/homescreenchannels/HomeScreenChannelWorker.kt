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

package com.android.tv.reference.homescreenchannels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.tvprovider.media.tv.PreviewChannelHelper
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Worker triggered by WorkManager to add the home screen channel and its associated programs
 *
 * <code>
 * WorkManager.getInstance(context).enqueue(OneTimeWorkRequest.Builder(HomeScreenChannelWorker::class.java).build())
 * </code>
 */
class HomeScreenChannelWorker(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {

    companion object {
        private const val DEFAULT_CHANNEL_SIZE = 10
        private const val TAG = "HomeScreenChannelWorker"
    }

    override fun doWork(): Result {
        Log.v(TAG, "HomeScreenChannelWorker started")
        val previewChannelHelper = PreviewChannelHelper(context)
        val channelHelper = HomeScreenChannelHelper(previewChannelHelper)

        // IDs of programs already in the channel or that have been removed by the user
        val setOfProgramIdsToExclude = HashSet<String>()
        var programsToAdd = DEFAULT_CHANNEL_SIZE

        // Get or create the default channel
        val defaultChannel = channelHelper.getDefaultChannel()?.also {
            val programIdsInChannel = channelHelper.getProgramIdsInChannel(context, it.id)
            programsToAdd -= programIdsInChannel.getBrowsableProgramCount()

            // If the channel already has enough content, bail early
            if (programsToAdd <= 0) {
                Log.v(TAG, "Home screen channel already populated, worker finished")
                return Result.success()
            }

            // Any programs that are already in the channel should not be added again
            setOfProgramIdsToExclude.addAll(programIdsInChannel.getBrowsableProgramIds())
            setOfProgramIdsToExclude.addAll(programIdsInChannel.getNonBrowsableProgramIds())
        }
        val channelId = defaultChannel?.id ?: channelHelper.createHomeScreenDefaultChannel(context)

        // Populate the default channel with videos
        val videos = channelHelper.getVideosForDefaultChannel(applicationContext as Application, setOfProgramIdsToExclude, programsToAdd)
        Log.v(TAG, "Adding $videos to default channel $channelId")
        channelHelper.addProgramsToChannel(videos, channelId)

        Log.v(TAG, "Programs added, worker finished")
        return Result.success()
    }
}
