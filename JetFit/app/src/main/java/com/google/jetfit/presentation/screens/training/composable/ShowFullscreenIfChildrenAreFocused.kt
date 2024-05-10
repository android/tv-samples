package com.google.jetfit.presentation.screens.training.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewResponder
import androidx.compose.foundation.relocation.bringIntoViewResponder
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.debugInspectorInfo

@OptIn(ExperimentalFoundationApi::class)
internal fun Modifier.showFullscreenIfChildrenAreFocused(): Modifier = composed(
    inspectorInfo = debugInspectorInfo { name = "bringIntoViewIfChildrenAreFocused" },
    factory = {
        var myRect: Rect = Rect.Zero
        this
            .onSizeChanged {
                myRect = Rect(Offset.Zero, Offset(it.width.toFloat(), it.height.toFloat()))
            }
            .bringIntoViewResponder(
                remember {
                    object : BringIntoViewResponder {
                        @ExperimentalFoundationApi
                        override fun calculateRectForParent(localRect: Rect): Rect = myRect

                        @ExperimentalFoundationApi
                        override suspend fun bringChildIntoView(localRect: () -> Rect?) {
                        }
                    }
                }
            )
    }
)