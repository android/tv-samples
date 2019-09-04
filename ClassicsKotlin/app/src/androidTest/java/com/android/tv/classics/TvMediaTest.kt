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

package com.android.tv.classics

import android.content.Context
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.android.tv.classics.models.TvMediaDatabase
import com.android.tv.classics.models.TvMediaMetadata
import com.android.tv.classics.models.TvMediaCollection
import com.android.tv.classics.utils.TvLauncherUtils
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Instrumented tests for TvMedia* things */
@RunWith(AndroidJUnit4::class)
class TvMediaTest {

    @get:Rule
    val activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    /** Make sure that the overridden [TvMediaMetadata.equals] method works as intended */
    @Test fun metadataEquality() {
        val metadata1 = TvMediaMetadata(
                id = "1", collectionId = "0", title = "title",
                contentUri = Uri.parse("http://example.com"))
        val metadata2 = TvMediaMetadata(
                id = "1", collectionId = "0", title = "title",
                contentUri = Uri.parse("http://example.com"), playbackPositionMillis = 1)
        assert(metadata1 == metadata2)
    }

    /** Insert and delete channel with a single program */
    @Test fun insertDeleteChannel() {
        val context = activityRule.activity as Context

        // Create a dummy collection
        val collection = TvMediaCollection(id = "1", title = "collection", description = "desc")

        // Create metadata to go into the collection
        val metadata = TvMediaMetadata(
                id = "0", collectionId = "1", title = "title",
                contentUri = Uri.parse("http://example.com"))

        // Create a channel with our collection and get back its ID
        val addedChannelId =
                TvLauncherUtils.upsertChannel(context, collection, listOf(metadata))!!

        // Now delete that channel from the launcher
        val deletedChannelId = TvLauncherUtils.removeChannel(context, collection)!!

        // Added and deleted channels should be equal
        assert(addedChannelId == deletedChannelId)
    }
}