package com.google.jetfit.presentation.utils

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Immutable
data class Shadow(
    @Stable val offsetX: Dp = 0.dp,
    @Stable val offsetY: Dp = 0.dp,
    @Stable val radius: Dp,
    @Stable val color: Color,
)

fun Modifier.withShadow(
    shadow: Shadow,
    shape: Shape,
    showShadow: Boolean = true,
) = drawBehind {
    if (showShadow) {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            paint.asFrameworkPaint().apply {
                this.color = Color.Transparent.toArgb()
                setShadowLayer(
                    shadow.radius.toPx(),
                    shadow.offsetX.toPx(),
                    shadow.offsetY.toPx(),
                    shadow.color.toArgb(),
                )
            }
            val outline = shape.createOutline(size, layoutDirection, this)
            canvas.drawOutline(outline, paint)
        }
    }
}