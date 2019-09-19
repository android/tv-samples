/*
 * Copyright 2019 Google LLC
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

package com.android.tv.classics.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.android.parcel.Parcelize

/** Data class representing a background image displayed in the browsing page */
@Entity
@Parcelize
data class TvMediaBackground(
        /** User-provided identifier */
        @PrimaryKey val id: String,

        /** URI for the image */
        val uri: Uri
) : Parcelable

/** Data access object for the [TvMediaBackground] class */
@Dao
interface TvMediaBackgroundDAO {

    @Query("SELECT * FROM tvmediabackground")
    fun findAll(): List<TvMediaBackground>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg item: TvMediaBackground)

    @Update
    fun update(item: TvMediaBackground)

    @Delete
    fun delete(item: TvMediaBackground)

    @Query("DELETE FROM tvmediabackground")
    fun truncate()
}