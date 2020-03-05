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

package com.android.tv.classics.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.DatabaseUtils
import android.net.Uri
import android.util.Log
import com.android.tv.classics.models.TvMediaDatabase
import com.android.tv.classics.models.TvMediaMetadata

/** Expose metadata to the system's global search and Google Assistant */
class TvMediaProvider : ContentProvider() {
    companion object {
        private val TAG = TvMediaProvider::class.java.simpleName
    }

    // Initialize database upon first query when context will be available
    private val database: TvMediaDatabase by lazy {
        TvMediaDatabase.getInstance(context!!)
    }

    override fun onCreate() = true

    override fun query(
            uri: Uri,
            projection:
            Array<String>?,
            selection: String?,
            selectionArgs: Array<String>?,
            sortOrder: String?
    ): Cursor? = if (uri.pathSegments.firstOrNull() == "search") {
        // We only consider a query as valid if it's in the form of <authority>/search/<query>
        val result = selectionArgs?.firstOrNull()?.let { selector ->
            // Perform light processing of the query selector as we send the request to the database
            database.metadata().contentProviderQuery(TvMediaMetadata.searchableText(selector))
        }
        val stringResult = DatabaseUtils.dumpCursorToString(result)
        Log.d(TAG, "Handling query for $uri; \"${selectionArgs?.firstOrNull()}\" --> count=${result?.count}; result=$stringResult")
        result
    } else {
        throw IllegalArgumentException("Invalid URI: $uri")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? =
            throw UnsupportedOperationException("Requested operation not supported")

    override fun update(
            uri: Uri,
            values: ContentValues?,
            selection: String?,
            selectionArgs: Array<String>?): Int =
            throw UnsupportedOperationException("Requested operation not supported")

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int =
            throw UnsupportedOperationException("Requested operation not supported")

    /** We do not implement resolution of MIME type given a URI */
    override fun getType(uri: Uri): String? = null

}
