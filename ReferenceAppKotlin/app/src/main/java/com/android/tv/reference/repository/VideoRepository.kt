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
}
