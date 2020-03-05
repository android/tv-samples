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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.android.tv.classics.models.TvMediaDatabase
import com.android.tv.classics.workers.TvMediaSynchronizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


/** Entry point for the Android TV application */
class MainActivity : FragmentActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val activity = this
        val db = TvMediaDatabase.getInstance(this)

        // Navigates to other fragments based on Intent's action
        // [MainActivity] is the main entry point for all intent filters
        if (intent.action == Intent.ACTION_VIEW || intent.action == Intent.ACTION_SEARCH) {
            val uri = intent.data ?: Uri.EMPTY
            Log.d(TAG, "Intent ${intent.action} received: $uri")
            when (uri.pathSegments.firstOrNull()) {

                // Navigates to now playing screen for chosen "program"
                "program" -> GlobalScope.launch {
                    uri.lastPathSegment?.let { db.metadata().findById(it) }?.let { metadata ->
                        metadata.playbackPositionMillis = intent.extras?.getLong("Progress")
                                ?.coerceAtLeast(metadata.playbackPositionMillis ?: 0)
                        Log.d(TAG, "Navigating to now playing for $metadata")
                        withContext(Dispatchers.Main) {
                            Navigation.findNavController(activity, R.id.fragment_container)
                                    .navigate(NavGraphDirections.actionToNowPlaying(metadata))
                        }
                    }
                }

                // Scrolls to chosen "channel" in browse fragment
                "channel" -> GlobalScope.launch {
                    val channelId = uri.lastPathSegment
                    Log.d(TAG, "Navigating to browser for channel $channelId")
                    withContext(Dispatchers.Main) {
                        Navigation.findNavController(activity, R.id.fragment_container)
                                .navigate(NavGraphDirections.actionToMediaBrowser()
                                        .setChannelId(channelId))
                    }
                }

                else -> Log.w(TAG, "VIEW intent received but unrecognized URI: $uri")
            }
        }

        // Syncs the home screen channels hourly
        // NOTE: It's very important to keep our content fresh in the user's home screen
        WorkManager.getInstance(baseContext).enqueue(
                PeriodicWorkRequestBuilder<TvMediaSynchronizer>(1, TimeUnit.HOURS)
                        .setInitialDelay(1, TimeUnit.HOURS)
                        .setConstraints(Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build())
                        .build())
    }

}
