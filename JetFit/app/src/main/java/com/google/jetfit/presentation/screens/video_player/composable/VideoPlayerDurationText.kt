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

package com.google.jetfit.presentation.screens.video_player.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.presentation.theme.JetFitTheme

@Composable
fun VideoPlayerDurationText(
    text: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight(500),
    color: Color = Color.White.copy(alpha = 0.87f)
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontWeight = fontWeight,
        fontFamily = FontFamily(Font(R.font.ibm_plex_sans_medium))
    )
}


@Preview(device = Devices.TV_1080p)
@Composable
fun PreviewVideoPlayerControllerText() {
    JetFitTheme {
        Row {
            VideoPlayerDurationText(text = "19:00")
            VideoPlayerDurationText(
                text = "/20:35",
                fontWeight = FontWeight(400),
                color = Color.White.copy(alpha = 0.60f)
            )
        }
    }
}