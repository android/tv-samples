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
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.datamodel.VideoType

/**
 * Interface to define methods to interact with different data sources.
 */
interface VideoRepository {
    val application: Application

    /**
     * Return all videos available from a specific source.
     * @return List<Video>
     */
    fun getAllVideos(): List<Video>

    /**
     * Returns a Video for the passed [id] or null if there is no matching video.
     */
    fun getVideoById(id: String): Video?

    /**
     * Returns a Video for the passed [uri] or null if there is no matching video.
     */
    fun getVideoByVideoUri(uri: String): Video?

    /**
     *  Returns all TV episodes from the same TV Series.
     */
    fun getAllVideosFromSeries(seriesUri: String): List<Video>

    /**
     * Get next episode for the same TV Series.
     *  - if given episode is not last episode of current season, return next episode.
     *  - if given episode is last episode of current season, return first episode of next season.
     * Returns next episode or null if failed to find one.
     */
    fun getNextEpisodeInSeries(episode: Video): Video? {
        if (episode.videoType != VideoType.EPISODE) {
            return null
        }

        // Put all episodes of the same series in a map. With season number as the key and a
        // list of videos from the specified season as the value.
        val tvSeasonMap = getAllVideosFromSeries(episode.seriesUri).groupBy { it.seasonNumber }

        var nextEpisode: Video? = null
        val nextEpisodeNumber = (episode.episodeNumber.toInt() + 1).toString()

        // Searching for next episode in the same season.
        tvSeasonMap[episode.seasonNumber]?.let { currentSeason ->
            nextEpisode = currentSeason.firstOrNull { videoInCurrentSeason ->
                videoInCurrentSeason.episodeNumber == nextEpisodeNumber
            }
        }

        // If not found in previous step, checking if there's an episode available in next season.
        if (nextEpisode == null) {
            val nextSeasonNumber = (episode.seasonNumber.toInt() + 1).toString()
            tvSeasonMap[nextSeasonNumber]?.let { nextSeason ->
                nextEpisode = if (nextSeason.sortedBy { it.episodeNumber }.isNotEmpty()) {
                    nextSeason[0]
                } else {
                    null
                }
            }
        }
        return nextEpisode
    }
}
