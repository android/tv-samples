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

package com.google.jetstream.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

val pairs = listOf(
    Coral to LightYellow,
    Red300 to BlueGray300,
    Pink300 to Gray300,
    Purple300 to Brown300,
    DeepPurple300 to DeepOrange300,
    Indigo300 to Orange300,
    Blue300 to Amber300,
    LightBlue300 to Yellow300,
    Cyan300 to Lime300,
    Teal300 to LightGreen300,
    Green300 to Coral,
)

@Composable
fun GradientBg() {
    Box(
        modifier = Modifier
            .background(Brush.radialGradient(pairs.random().toList()))
            .fillMaxWidth()
            .height(200.dp)
    )
}
