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
import android.content.Context
import android.database.Cursor
import android.media.tv.TvContract.WatchNextPrograms.COLUMN_INTERNAL_PROVIDER_ID
import android.net.Uri
import androidx.tvprovider.media.tv.PreviewChannelHelper
import androidx.tvprovider.media.tv.TvContractCompat
import androidx.tvprovider.media.tv.TvContractCompat.PreviewPrograms
import androidx.tvprovider.media.tv.TvContractCompat.WatchNextPrograms
import androidx.tvprovider.media.tv.WatchNextProgram
import androidx.tvprovider.media.tv.WatchNextProgram.Builder
import androidx.tvprovider.media.tv.WatchNextProgram.fromCursor
import com.android.tv.reference.shared.datamodel.Video
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Helper class that simplifies interactions with the ATV home screen.
 */
object PlayNextHelper {

    // Values to approximate if video has started.
    private const val PLAY_NEXT_STARTED_MIN_PERCENTAGE = 0.03
    private const val PLAY_NEXT_STARTED_MIN_MINUTES = 2L

    // MetaData sent from Playback to process Play Next operations.
    internal const val VIDEO_ID = "VIDEO_ID"
    internal const val CURRENT_POSITION = "CURRENT_POSITION"
    internal const val DURATION = "DURATION"
    internal const val PLAYER_STATE = "PLAYER_STATE"

    // TODO(mayurkhin@) add sealed class/enum if more states are required
    // as a part of a performance/tech debt optimization
    internal const val PLAY_STATE_PAUSED = "STATE_PAUSED"
    internal const val PLAY_STATE_ENDED = "STATE_ENDED"

    /**
     * Add all relevant metadata which will be displayed on Play Next Channel.
     */
    private fun setBuilderMetadataForMovie(
        builder: Builder,
        video: Video,
        watchNextType: Int,
        watchPosition: Int,
        type: Int,
        duration: Int
    ): Builder {
        builder.setType(type)
            .setWatchNextType(watchNextType)
            .setLastPlaybackPositionMillis(watchPosition)
            .setLastEngagementTimeUtcMillis(System.currentTimeMillis())
            .setTitle(video.name)
            .setDurationMillis(duration)
            .setPreviewVideoUri(Uri.parse(video.videoUri))
            .setDescription(video.description)
            .setPosterArtUri(Uri.parse(video.thumbnailUri))
            // Intent uri used to deep link video when user clicks on play next item.
            .setIntentUri(Uri.parse(video.uri))
            /* The internalProviderId attribute must match the internal ID you provide in the
            Media PlaybackStateCompat.Actions feed. This allows Android TV to reconcile
            the asset more effectively and provides a high-confidence feature to users.
            Refer https://developer.android.com/training/tv/discovery/watch-next-programs?hl=tr*/
            .setInternalProviderId(video.id)
            // Use the contentId to recognize same content across different channels.
            .setContentId(video.id)
        return builder
    }

    /**
     * The user has "started" a video if they've watched more than 3% or 2 minutes,
     * whichever timestamp is earlier.
     * https://developer.android.com/training/tv/discovery/guidelines-app-developers
     */
    internal fun hasVideoStarted(duration: Int, currentPosition: Int): Boolean {
        Timber.v("Find if Video started: duration: $duration ,watchPosition: $currentPosition")

        // Return true if either X minutes or Y % have passed
        // Following formatting spans over multiple lines to accommodate max 100 limit
        val playNextMinStartedMillis = TimeUnit.MINUTES.toMillis(PLAY_NEXT_STARTED_MIN_MINUTES)
        // Check if either X minutes or Y% has passed
        val hasVideoStarted =
            (currentPosition >= (duration * PLAY_NEXT_STARTED_MIN_PERCENTAGE)) or
                (currentPosition >= playNextMinStartedMillis)
        return ((currentPosition <= duration) and hasVideoStarted)
    }

    /**
     * Retrieve all programs in Play Next row.
     */
    @SuppressLint("RestrictedApi")
    // Suppress RestrictedApi due to https://issuetracker.google.com/138150076
    internal fun getWatchNextPrograms(context: Context): List<WatchNextProgram> {
        val programs: MutableList<WatchNextProgram> = mutableListOf()
        val cursor = context.contentResolver.query(
            TvContractCompat.WatchNextPrograms.CONTENT_URI,
            WatchNextProgram.PROJECTION,
            /* selection = */ null,
            /* selectionArgs = */ null,
            /* sortOrder= */ null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    programs.add(fromCursor(cursor))
                } while (it.moveToNext())
            }
        }
        return programs
    }

    /**
     * Add unfinished program to play next.
     * Update the playback position if program already exists in Play Next Channel.
     */
    @SuppressLint("RestrictedApi")
    // Suppress RestrictedApi due to https://issuetracker.google.com/138150076
    @Synchronized
    internal fun insertOrUpdateVideoToPlayNext(
        video: Video,
        watchPosition: Int,
        duration: Int,
        context: Context
    ): Long {

        var programId = 0L
        // If we already have a program with this ID, use it as a base for updated program.
        // Avoid Duplicates.

        val existingProgram = getWatchNextProgramByVideoId(video.id, context)
        Timber.v(
            "insertOrUpdateToPlayNext, existingProgram = $existingProgram ,videoid = ${video.id}"
        )
        // If program exists,create builder with existing program.
        val programBuilder = existingProgram
            ?.let {
                Builder(it)
            } ?: Builder()

        // Set appropriate types for resuming movie.
        val updatedBuilder = setBuilderMetadataForMovie(
            programBuilder,
            video,
            WatchNextPrograms.WATCH_NEXT_TYPE_CONTINUE,
            watchPosition,
            PreviewPrograms.TYPE_MOVIE,
            duration
        )

        // Build the program with all the metadata
        val updatedProgram = updatedBuilder.build()

        // If the program is already in the Play next row, update it
        if (existingProgram != null) {
            programId = existingProgram.id
            PreviewChannelHelper(context).updateWatchNextProgram(updatedProgram, programId)
            Timber.v("Updated program in Play next row: ${updatedProgram.title}")
        }
        // Otherwise build the program and insert it into the channel
        else {
            try {
                programId = PreviewChannelHelper(context)
                    .publishWatchNextProgram(updatedProgram)
                Timber.v("Added New program to Play next row: ${updatedProgram.title}")
            } catch (exc: IllegalArgumentException) {
                Timber.e(
                    exc, "Unable to add program to Play next row. ${exc.localizedMessage}"
                )
                exc.printStackTrace()
            }
        }

        Timber.v("Final added/updated programId = $programId")
        return programId
    }

    /**
     *  Remove a Video object from the Play next row,
     *  Typically after user has finished watching the video.
     *  Returns the number of rows deleted or null if delete fails
     */
    @SuppressLint("RestrictedApi")
    // Suppress RestrictedApi due to https://issuetracker.google.com/138150076
    @Synchronized
    fun removeVideoFromPlayNext(context: Context, video: Video): Uri? {
        Timber.v("Trying to Removing content from Play next: ${video.name}")

        // Find the program with the matching ID for our metadata.
        val foundProgram = getWatchNextProgramByVideoId(video.id, context)
        if (foundProgram == null) {
            Timber.e(
                "Unable to delete. No program found with videoID ${video.id}"
            )
            return null
        }

        // Use the found program's URI to delete it from the content resolver
        return foundProgram.let {
            val programUri = TvContractCompat.buildWatchNextProgramUri(it.id)
            // delete returns the number of rows deleted.
            val deleteCount = context.contentResolver.delete(
                programUri, null, null
            )

            if (deleteCount == 1) {
                Timber.v("Content successfully removed from Play next")
                programUri
            } else {
                Timber.e("Content failed to be removed from Play next, delete count $deleteCount")
                null
            }
        }
    }

    /**
     * Query the play next list and find the program with given videoId.
     * Return null if not found.
     */
    @SuppressLint("RestrictedApi")
    // Suppress RestrictedApi due to https://issuetracker.google.com/138150076
    @Synchronized
    private fun getWatchNextProgramByVideoId(id: String, context: Context): WatchNextProgram? {
        return findFirstWatchNextProgram(context) { cursor ->
            (cursor.getString(cursor.getColumnIndex(COLUMN_INTERNAL_PROVIDER_ID)) == id)
        }
    }

    /**
     * Find the Watch Next program for given id.
     * Returns the first instance available.
     */
    @SuppressLint("RestrictedApi")
    // Suppress RestrictedApi due to https://issuetracker.google.com/138150076
    internal fun findFirstWatchNextProgram(context: Context, predicate: (Cursor) -> Boolean):
        WatchNextProgram? {

            val cursor = context.contentResolver.query(
                TvContractCompat.WatchNextPrograms.CONTENT_URI,
                WatchNextProgram.PROJECTION,
                /* selection = */ null,
                /* selectionArgs = */ null,
                /* sortOrder= */ null
            )
            cursor?.use {
                if (it.moveToFirst()) {
                    do {
                        if (predicate(cursor)) {
                            return fromCursor(cursor)
                        }
                    } while (it.moveToNext())
                }
            }
            return null
        }
}
