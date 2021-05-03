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
package com.android.tv.reference.homescreenchannels

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

/**
 * BroadcastReceiver that initializes the home screen channel.
 *
 * When installed from the Play Store, the system will send out a broadcast for
 * INITIALIZE_PROGRAMS, which is meant to trigger a BroadcastReceiver that sets
 * up the home screen channel(s) like this one.
 *
 * To test this during development, you can trigger the broadcast with ADB:
 *
 * <code>
 * adb shell am broadcast -a android.media.tv.action.INITIALIZE_PROGRAMS -n com.android.tv.reference/.homescreenchannels.HomeScreenChannelReceiver
 * </code>
 */
class HomeScreenChannelReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // If your receiver handles more than one action, check intent.action for INITIALIZE_PROGRAMS
        // Since this receiver has a single purpose, it can just trigger the Worker
        WorkManager.getInstance(context)
            .enqueue(OneTimeWorkRequest.Builder(HomeScreenChannelWorker::class.java).build())
    }
}
