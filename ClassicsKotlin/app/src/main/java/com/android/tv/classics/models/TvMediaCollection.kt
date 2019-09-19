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

/** Data class representing a collection of playable metadata */
@Entity
@Parcelize
data class TvMediaCollection(

        /** User-provided identifier for this collection */
        @PrimaryKey val id: String,

        /** Title displayed to user */
        val title: String,

        /** Description of this collection */
        val description: String,

        /** URI for the album / poster art corresponding to this collection */
        val artUri: Uri? = null

) : Parcelable

/** Data access object for the [TvMediaCollection] class */
@Dao
interface TvMediaCollectionDAO {

    @Query("SELECT * FROM tvmediacollection")
    fun findAll(): List<TvMediaCollection>

    @Query("SELECT * FROM tvmediacollection WHERE id LIKE :id LIMIT 1")
    fun findById(id: String): TvMediaCollection?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg item: TvMediaCollection)

    @Update
    fun update(item: TvMediaCollection)

    @Delete
    fun delete(item: TvMediaCollection)

    @Query("DELETE FROM tvmediacollection")
    fun truncate()
}