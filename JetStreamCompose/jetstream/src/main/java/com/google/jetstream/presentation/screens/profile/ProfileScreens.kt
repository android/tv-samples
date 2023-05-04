/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.presentation.screens.profile

import androidx.annotation.DrawableRes
import com.google.jetstream.R

enum class ProfileScreens(
    private val title: String? = null,
    @DrawableRes val icon: Int
) {
    Accounts(icon = R.drawable.ic_profile_accounts),
    About(icon = R.drawable.ic_profile_about),
    Subtitles(icon = R.drawable.ic_profile_subtitles),
    Language(icon = R.drawable.ic_profile_preferred_language),
    SearchHistory(title = "Search history", icon = R.drawable.ic_search),
    HelpAndSupport(title = "Help and Support", icon = R.drawable.ic_profile_support);

    operator fun invoke() = name

    val tabTitle = title ?: name
}
