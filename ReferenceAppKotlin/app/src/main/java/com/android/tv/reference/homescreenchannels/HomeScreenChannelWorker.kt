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
package com.android.tv.reference.homescreenchannels

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.tvprovider.media.tv.PreviewChannelHelper
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber

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
    }

    override fun doWork(): Result {
        Timber.v("HomeScreenChannelWorker started")

        // The INITIALIZE_PROGRAMS broadcast will not be sent on older versions of Android TV,
        // but you can also check the Android OS version to avoid doing the work on versions of
        // Android TV that don't have home screen channels in case you manually trigger the Worker.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Timber.d("Home screen channels are not supported before Android O; skipping work")
            // Return success because this doesn't need to be triggered again
            return Result.success()
        }

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
                Timber.v("Home screen channel already populated, worker finished")
                return Result.success()
            }

            // Any programs that are already in the channel should not be added again
            setOfProgramIdsToExclude.addAll(programIdsInChannel.getBrowsableProgramIds())
            setOfProgramIdsToExclude.addAll(programIdsInChannel.getNonBrowsableProgramIds())
        }
        val channelId = defaultChannel?.id ?: channelHelper.createHomeScreenDefaultChannel(context)

        // Populate the default channel with videos
        val videos = channelHelper.getVideosForDefaultChannel(
            applicationContext as Application,
            setOfProgramIdsToExclude,
            programsToAdd
        )
        Timber.v("Adding $videos to default channel $channelId")
        channelHelper.addProgramsToChannel(videos, channelId)

        Timber.v("Programs added, worker finished")
        return Result.success()
    }
}
