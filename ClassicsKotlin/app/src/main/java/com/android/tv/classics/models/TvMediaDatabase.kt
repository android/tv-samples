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

import android.content.Context
import android.net.Uri
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/** Custom converters used to store types not natively supported by Room in our database */
class TvMediaConverters {
    private val gson = Gson()

    @TypeConverter
    fun uriToString(value: Uri?): String? = value?.toString()

    @TypeConverter
    fun stringToUri(value: String?): Uri? = value?.let { Uri.parse(it) }

    @TypeConverter
    fun stringToStringList(value: String?): List<String> = value?.let {
        gson.fromJson<List<String>>(it, object : TypeToken<List<String>>(){}.type) } ?: listOf()

    @TypeConverter
    fun stringListToString(value: List<String>?): String? = value?.let { gson.toJson(it) }
}

/** Room database implementation */
@TypeConverters(TvMediaConverters::class)
@Database(version = 1, entities = [
    TvMediaMetadata::class, TvMediaCollection::class, TvMediaBackground::class])
abstract class TvMediaDatabase : RoomDatabase() {
    abstract fun metadata(): TvMediaMetadataDAO
    abstract fun collections(): TvMediaCollectionDAO
    abstract fun backgrounds(): TvMediaBackgroundDAO

    companion object {

        private val DATABASE_NAME = TvMediaDatabase::class.java.simpleName

        /** Singleton property */
        @Volatile private var INSTANCE: TvMediaDatabase? = null

        /**
         * Convenience method used to get an instance of our database, taken from official codelab:
         * https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#6
         */
        fun getInstance(context: Context): TvMediaDatabase = INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                    context.applicationContext, TvMediaDatabase::class.java, DATABASE_NAME)
                    .build().also { INSTANCE = it }
        }
     }


}