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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.theme.JetStreamCardShape

@Composable
fun SearchHistorySection() {
    with(StringConstants.Composable.Placeholders) {
        LazyColumn(modifier = Modifier.padding(horizontal = 72.dp)) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = SearchHistorySectionTitle,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Button(onClick = { /* Clear search history */ }) {
                        Text(text = SearchHistoryClearAll)
                    }
                }
            }
            items(SampleSearchHistory.size) { index ->
                ListItem(
                    modifier = Modifier.padding(top = 8.dp),
                    selected = false,
                    onClick = {},
                    headlineContent = {
                        Text(
                            text = SampleSearchHistory[index],
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    shape = ListItemDefaults.shape(shape = JetStreamCardShape)
                )
            }
        }
    }
}
