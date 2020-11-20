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
package com.android.tv.reference

import android.app.Application
import android.os.StrictMode
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.cast.tv.CastReceiverContext
import timber.log.Timber

/**
 * Initializes libraries, such as Timber, and sets up application wide settings.
 */
class TvReferenceApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }

        // TODO(b/162013888): Plant a timber tree for non-debug builds.
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        /**
         * This initialises an instance of the CastReceiverContext object which is needed to
         * interact with Cast while in the TV app. This object allows for passing media signals
         * and the data load and so needs to exist while the app is in the foreground so that all
         * cast commands can be picked up by the TV App.
         */
        CastReceiverContext.initInstance((this))
        ProcessLifecycleOwner.get().lifecycle
            .addObserver(AppLifecycleObserver(ProcessLifecycleOwner.get().lifecycle))
    }

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build()
        )
    }

    /**
     * TVs only have at most one app in the foreground so we can use onResume/onPause.
     * For other form factors, this registration may vary.
     */
    class AppLifecycleObserver(val lifecycle: Lifecycle) : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            CastReceiverContext.getInstance().start()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() {
            CastReceiverContext.getInstance().stop()
        }
    }
}
