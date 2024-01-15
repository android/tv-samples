/*
 * Copyright 2024 Google LLC
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

import android.net.Uri
import androidx.tvprovider.media.tv.TvContractCompat.WatchNextPrograms
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.datamodel.VideoType
import com.android.tv.reference.watchnext.EngageWatchNextService.Companion.WatchNextVideo
import com.google.android.engage.common.datamodel.Image
import com.google.android.engage.video.datamodel.MovieEntity
import com.google.android.engage.video.datamodel.TvEpisodeEntity
import com.google.android.engage.video.datamodel.VideoEntity
import com.google.android.engage.video.datamodel.WatchNextType

object VideoToEngageEntityConverter {

    fun convertVideo(watchNextVideo: WatchNextVideo): VideoEntity {
        val video = watchNextVideo.video
        val watchPosition = watchNextVideo.watchPosition
        val watchNextType = watchNextVideo.watchNextType

        return when (video.videoType) {
            VideoType.MOVIE -> convertMovieEntity(
                video = video,
                watchPosition = watchPosition,
                watchNextType = watchNextType
            )

            VideoType.EPISODE -> convertTvEpisodeEntity(
                video = video,
                watchPosition = watchPosition,
                watchNextType = watchNextType
            )

            else -> {
                throw IllegalArgumentException(
                    "Conversion is not supported for Video Type: ${video.videoType}"
                )
            }
        }
    }

    private fun convertTvEpisodeEntity(
        video: Video,
        watchPosition: Int,
        watchNextType: Int
    ): TvEpisodeEntity {
        return TvEpisodeEntity
            .Builder()
            .setWatchNextType(convertToEngageWatchNextType(watchNextType))
            .setLastPlayBackPositionTimeMillis(watchPosition.toLong())
            .setLastEngagementTimeMillis(System.currentTimeMillis())
            .setName(video.name)
            .setDurationMillis(video.duration.toLong())
            .addPosterImage(Image.Builder().setImageUri(Uri.parse(video.thumbnailUri)).build())
            .setPlayBackUri(Uri.parse(video.uri))
            .setEntityId(video.id)
            .setSeasonNumber(video.seasonNumber)
            .setSeasonTitle("${video.category} Season ${video.seasonNumber}")
            .setEpisodeDisplayNumber(video.name)
            .build()
    }

    private fun convertMovieEntity(
        video: Video,
        watchPosition: Int,
        watchNextType: Int
    ): MovieEntity {
        return MovieEntity
            .Builder()
            .setWatchNextType(convertToEngageWatchNextType(watchNextType))
            .setLastPlayBackPositionTimeMillis(watchPosition.toLong())
            .setLastEngagementTimeMillis(System.currentTimeMillis())
            .setName(video.name)
            .setDurationMillis(video.duration.toLong())
            .addPosterImage(Image.Builder().setImageUri(Uri.parse(video.thumbnailUri)).build())
            .setPlayBackUri(Uri.parse(video.uri))
            .setEntityId(video.id)
            .build()
    }

    private fun convertToEngageWatchNextType(watchNextType: Int): Int {
        return when (watchNextType) {
            WatchNextPrograms.WATCH_NEXT_TYPE_NEXT -> WatchNextType.TYPE_NEXT
            WatchNextPrograms.WATCH_NEXT_TYPE_CONTINUE -> WatchNextType.TYPE_CONTINUE
            WatchNextPrograms.WATCH_NEXT_TYPE_NEW -> WatchNextType.TYPE_NEW
            else -> WatchNextType.TYPE_UNKNOWN
        }
    }
}
