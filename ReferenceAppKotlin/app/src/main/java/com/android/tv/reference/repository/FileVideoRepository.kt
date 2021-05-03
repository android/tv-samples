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
import com.android.tv.reference.R
import com.android.tv.reference.shared.datamodel.Video

/**
 * VideoRepository implementation to read video data from a file saved on /res/raw
 */
class FileVideoRepository(override val application: Application) : VideoRepository {
    // Underscore name to allow lazy loading since "getAllVideos" matches the getter name otherwise
    private val _allVideos: List<Video> by lazy {
        val jsonString = readJsonFromFile()
        VideoParser.loadVideosFromJson(jsonString)
    }

    private fun readJsonFromFile(): String {
        val inputStream = application.resources.openRawResource(R.raw.api)
        return inputStream.bufferedReader().use {
            it.readText()
        }
    }

    override fun getAllVideos(): List<Video> {
        return _allVideos
    }

    override fun getVideoById(id: String): Video? {
        val jsonString = readJsonFromFile()
        return VideoParser.findVideoFromJson(jsonString, id)
    }

    override fun getVideoByVideoUri(uri: String): Video? {
        return getAllVideos()
            .firstOrNull { it.videoUri == uri }
    }

    override fun getAllVideosFromSeries(seriesUri: String): List<Video> {
        return getAllVideos().filter { it.seriesUri == seriesUri }
    }
}
