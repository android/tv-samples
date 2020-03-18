/*
 * Copyright 2020 Google LLC
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

package com.android.tv.reference

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment

/**
 * FragmentActivity that displays the various fragments
 */
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set the correct starting destination for the NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        @Suppress("ConstantConditionIf")
        if (BuildConfig.FIREBASE_ENABLED) {
            // TODO Go to Firebase welcome/login screen
            navGraph.startDestination = R.id.browseFragment
        } else {
            // Firebase is not enabled, notify the user before starting
            navGraph.startDestination = R.id.noFirebaseFragment
        }
        navController.graph = navGraph
    }
}
