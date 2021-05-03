/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.tv.reference.shared.image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.squareup.picasso.Transformation

/**
 * Picasso Transformation that applies a solid color on top of an image.
 *
 * By default, the solid color is black at 70% opacity. This will ensure text of near white
 * (#EEEEEE) displayed on top of this image has a minimum contrast of ratio of 7.29. You can pass
 * in a different target color or a different adjustment amount based on the requirements of your
 * text and images.
 *
 * Note that busy backgrounds (i.e., backgrounds that have a lot of lines, text, or sharp color
 * variations) should be blurred to make the text on top more readable. If the background is
 * already soft (e.g., a gradient, an image that was blurred on the server, etc.), this
 * transformation is enough to ensure readable text.
 */
class OverlayImageTransformation(
    private val targetColor: Int = Color.BLACK,
    private val adjustmentAmount: Float = 0.7f
) : Transformation {

    // Color to paint on top of the image
    private val overlayColor: Int by lazy {
        val overlayAlpha = (adjustmentAmount * 255).toInt()
        Color.argb(
            overlayAlpha,
            Color.red(targetColor),
            Color.green(targetColor),
            Color.blue(targetColor)
        )
    }

    // Key for Picasso to identify this transformation
    private val key: String by lazy {
        "BackgroundTransformation($overlayColor)"
    }

    private val paint = Paint()

    override fun key(): String {
        return key
    }

    override fun transform(source: Bitmap?): Bitmap {
        requireNotNull(source)
        val mutableBitmap: Bitmap
        val canvas: Canvas

        if (source.isMutable) {
            // Source can be reused if it's mutable
            mutableBitmap = source
            canvas = Canvas(mutableBitmap)
        } else {
            // Not mutable, so create a copy and recycle the source
            mutableBitmap = Bitmap.createBitmap(source.width, source.height, source.config)
            canvas = Canvas(mutableBitmap)
            canvas.drawBitmap(source, 0f, 0f, paint)
            source.recycle()
        }

        // Draw over the Bitmap to shift it toward the target color
        canvas.drawColor(overlayColor)

        return mutableBitmap
    }
}
