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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Support
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.graphics.vector.ImageVector

enum class ProfileScreens(
    val icon: ImageVector,
    private val title: String? = null,
) {
    Accounts(Icons.Default.Person),
    About(Icons.Default.Info),
    Subtitles(Icons.Default.Subtitles),
    Language(Icons.Default.Translate),
    SearchHistory(title = "Search history", icon = Icons.Default.Search),
    HelpAndSupport(title = "Help and Support", icon = Icons.Default.Support);

    operator fun invoke() = name

    val tabTitle = title ?: name
}
