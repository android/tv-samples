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

import android.app.SearchManager
import android.database.Cursor
import android.media.tv.TvContentRating
import android.net.Uri
import android.os.Parcelable
import android.provider.BaseColumns
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import androidx.tvprovider.media.tv.BasePreviewProgram
import androidx.tvprovider.media.tv.TvContractCompat
import kotlinx.android.parcel.Parcelize
import java.util.Locale

/**
 * Data class representing a piece of content metadata (title, content URI, state-related fields
 * [playback position], etc.)
 */
@Entity
@Parcelize
data class TvMediaMetadata(
        /** User-provided identifier for this piece of content */
        @PrimaryKey val id: String,

        /** Each metadata item can only be part of one collection */
        var collectionId: String,

        /** Title displayed to user */
        var title: String,

        /** Store a searchable version of the title as a property] */
        val searchableTitle: String = searchableText(title),

        /** URI for the content to be played */
        var contentUri: Uri,

        /** Author of the metadata content */
        var author: String? = null,

        /** Year in which the metadata content was released */
        var year: Int? = null,

        /** Duration in seconds of the metadata content */
        var playbackDurationMillis: Long? = null,

        /** Current playback position for this piece of content */
        var playbackPositionMillis: Long? = null,

        /** Content ratings (e.g. G, PG, R) */
        var ratings: List<String>? = null,

        /** Content genres, from TvContractCompat.Programs.Genres */
        var genres: List<String>? = null,

        /** Short description of the content shown to users */
        var description: String? = null,

        /** Track or episode number for this piece of metadata */
        var trackNumber: Int? = null,

        /** URI pointing to the album or poster art */
        var artUri: Uri? = null,

        /**
         * Aspect ratio for the art, must be one of the constants under
         * [TvContractCompat.PreviewPrograms]. Defaults to movie poster.
         */
        var artAspectRatio: Int = TvContractCompat.PreviewPrograms.ASPECT_RATIO_MOVIE_POSTER,

        /** Flag indicating if it's hidden from home screen channel */
        var hidden: Boolean = false,

        /** Flag indicating if it's added to watch next channel */
        var watchNext: Boolean = false,

        /** The type of program. Defaults to movie, must be one of PreviewProgramColumns.TYPE_... */
        var programType: Int = TvContractCompat.PreviewProgramColumns.TYPE_MOVIE

) : Parcelable {

    /**
     * Determine if an instance of this class carries state based on whether the fields below have
     * anything other than the default values.
     */
    private fun isStateless() = playbackDurationMillis == null && !hidden && !watchNext

    /** Compares only fields not related to the state */
    override fun equals(other: Any?): Boolean = if (isStateless()) {
        super.equals(other)
    } else {
        copy(playbackDurationMillis = null, hidden = false, watchNext = false).equals(other)
    }

    /** We must override [hashCode] if we override the [equals] function */
    override fun hashCode(): Int = if (isStateless()) {
        super.hashCode()
    } else {
        copy(playbackDurationMillis = null, hidden = false, watchNext = false).hashCode()
    }

    /** Helper function used to copy as much information as possible into a program builder */
    fun <T : BasePreviewProgram.Builder<*>>copyToBuilder(builder: T) {

        // Basic metadata
        builder.setContentId(id).setTitle(title).setType(programType)

        // Author (director for movies, performer for songs)
        author?.let { builder.setAuthor(it) }

        // Blurb shown under the media title in the program card
        description?.let { builder.setDescription(it) }

        // Release date, possible formats are  "yyyy", "yyyy-MM-dd", and "yyyy-MM-ddTHH:mm:ssZ"
        year?.let { builder.setReleaseDate(it.toString())}

        // Track / episode number
        trackNumber?.let { builder.setEpisodeNumber(it) }

        // Duration of content, set in milliseconds
        playbackDurationMillis?.let { builder.setDurationMillis(it.toInt()) }

        // Position of playback, set in milliseconds
        playbackPositionMillis?.let { builder.setLastPlaybackPositionMillis(it.toInt()) }

        // Album / poster art, which will have a specific aspect ratio
        artUri?.let {
            builder.setPosterArtUri(it)
            builder.setPosterArtAspectRatio(artAspectRatio)
        }

        // Content ratings for this specific metadata
        ratings?.let {
            // Process each of the ratings in the set
            val ratings = it.map {
                // First chunk of each rating is the system, second the actual ratings
                val parts = it.split(".", limit = 2)
                val ratingSystem = parts.get(0)  // E.g. TV_US
                val ratingValue = parts.getOrNull(1)  // E.g. PG
                ratingValue?.let { rating ->
                    TvContentRating.createRating("com.android.tv",
                            ratingSystem,  "${ratingSystem}_$rating") }
            }.filterNotNull()
            builder.setContentRatings(ratings.toTypedArray())

        } ?: builder.setContentRatings(arrayOf(TvContentRating.createRating(
                // Fallback to UNRATED if content ratings is null
                "null", "null", "null", null)))
    }

    companion object {
        /** Convenience function used to maximize search matches by ignoring punctuation symbols */
        fun searchableText(text: String) =
                text.replace(Regex("[^A-Za-z0-9 ]"), "").toLowerCase(Locale.getDefault())
    }
}

/**  Data access object for the [TvMediaMetadata] class */
@Dao
interface TvMediaMetadataDAO {

    /**
     * Builds a projection for content provider search results using the mappings described in
     * https://developer.android.com/training/tv/discovery/searchable.html#columns.
     */
    @Query("SELECT " +
            "id as ${BaseColumns._ID}, " +
            "id as ${SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID}, " +
            "title as ${SearchManager.SUGGEST_COLUMN_TEXT_1}, " +
            "description as ${SearchManager.SUGGEST_COLUMN_TEXT_2}, " +
            "artUri as ${SearchManager.SUGGEST_COLUMN_RESULT_CARD_IMAGE}, " +
            "year as ${SearchManager.SUGGEST_COLUMN_PRODUCTION_YEAR}, " +
            "playbackDurationMillis as ${SearchManager.SUGGEST_COLUMN_DURATION} " +
            "FROM tvmediametadata WHERE :title LIKE '%' || searchableTitle || '%'")
    fun contentProviderQuery(title: String): Cursor?

    @Query("SELECT * FROM tvmediametadata")
    fun findAll(): List<TvMediaMetadata>

    @Query("SELECT * FROM tvmediametadata WHERE id = :id LIMIT 1")
    fun findById(id: String): TvMediaMetadata?

    @Query("SELECT * FROM tvmediametadata WHERE collectionId = :collectionId")
    fun findByCollection(collectionId: String): List<TvMediaMetadata>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg metadata: TvMediaMetadata)

    @Update
    fun update(metadata: TvMediaMetadata)

    @Delete
    fun delete(metadata: TvMediaMetadata)

    @Query("DELETE FROM tvmediametadata")
    fun truncate()
}


