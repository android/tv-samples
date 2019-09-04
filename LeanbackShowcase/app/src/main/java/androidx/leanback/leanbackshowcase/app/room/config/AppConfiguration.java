/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.leanback.leanbackshowcase.app.room.config;

/**
 * Various configuration option ot simulate different scenarios
 */
public class AppConfiguration {

    // used to simulate massive database
    public static boolean IS_DATABASE_ACCESS_LATENCY_ENABLED = false;

    // used to simulate network latency when we firstly need to fetch data using network
    public static boolean IS_NETWORK_LATENCY_ENABLED = false;

    // used to simulate file operation latency
    public static boolean IS_FILE_OPERATION_LATENCY_ENABLED = false;

    // used to simulate renting transaction latency
    public static boolean IS_RENTING_OPERATION_DELAY_ENABLED = false;

    // used to simulate network latency when the search operation is performed
    public static boolean IS_SEARCH_LATENCY_ENABLED = false;

    // only use small data set (live_movie_debug.json) for debugging
    public static boolean IS_DEBUGGING_VERSION = false;
}
