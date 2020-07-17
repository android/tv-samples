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
package com.android.tv.reference.shared.watchprogress

import androidx.lifecycle.LiveData

/**
 * This repository (from the "repository pattern") abstracts the data source from the other classes
 * that just need the data.
 *
 * The main reason for doing this is to allow the ViewModel to retrieve data without knowing
 * where it is coming from. Since it doesn't care about the details, the repository can get data
 * from a local database, an online source, or anywhere that makes sense and the ViewModel does
 * not need to change.
 */
class WatchProgressRepository(private val watchProgressDao: WatchProgressDao) {

    fun getWatchProgressByVideoId(videoId: String): LiveData<WatchProgress> {
        return watchProgressDao.getWatchProgressByVideoId(videoId)
    }

    suspend fun insert(watchProgress: WatchProgress) {
        watchProgressDao.insert(watchProgress)
    }

    suspend fun deleteAll() {
        watchProgressDao.deleteAll()
    }
}
