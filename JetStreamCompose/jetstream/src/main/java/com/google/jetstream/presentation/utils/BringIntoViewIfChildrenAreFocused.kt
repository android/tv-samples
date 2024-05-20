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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.relocation.BringIntoViewResponder
import androidx.compose.foundation.relocation.bringIntoViewResponder
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.toSize

@Suppress("IllegalExperimentalApiUsage") // TODO (b/233188423): Address before moving to beta
@OptIn(ExperimentalFoundationApi::class)
// ToDo: Migrate to Modifier.Node and stop using composed function.
internal fun Modifier.bringIntoViewIfChildrenAreFocused(
    paddingValues: PaddingValues = PaddingValues()
): Modifier = composed(
    inspectorInfo = debugInspectorInfo { name = "bringIntoViewIfChildrenAreFocused" },
    factory = {
        val pxOffset = with(LocalDensity.current) {
            val y = (paddingValues.calculateBottomPadding() - paddingValues.calculateTopPadding())
                .toPx()
            Offset.Zero.copy(y = y)
        }
        var myRect: Rect = Rect.Zero
        val responder = object : BringIntoViewResponder {
            // return the current rectangle and ignoring the child rectangle received.
            @ExperimentalFoundationApi
            override fun calculateRectForParent(localRect: Rect): Rect {
                return myRect
            }

            // The container is not expected to be scrollable. Hence the child is
            // already in view with respect to the container.
            @ExperimentalFoundationApi
            override suspend fun bringChildIntoView(localRect: () -> Rect?) {
            }
        }

        this
            .onSizeChanged {
                val size = it.toSize()
                myRect = Rect(pxOffset, size)
            }
            .bringIntoViewResponder(responder)
    }
)
