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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.google.jetstream.BuildConfig
import com.google.jetstream.data.util.StringConstants

@Composable
fun AboutSection() {
    with(StringConstants.Composable.Placeholders) {
        Column(modifier = Modifier.padding(horizontal = 72.dp)) {
            Text(
                text = AboutSectionTitle,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                modifier = Modifier
                    .graphicsLayer { alpha = 0.8f }
                    .padding(top = 16.dp),
                text = AboutSectionDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.border.copy(alpha = 0.6f))
            )
            Text(
                modifier = Modifier
                    .graphicsLayer { alpha = 0.6f }
                    .padding(top = 16.dp),
                text = AboutSectionAppVersionTitle,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = BuildConfig.VERSION_NAME,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
