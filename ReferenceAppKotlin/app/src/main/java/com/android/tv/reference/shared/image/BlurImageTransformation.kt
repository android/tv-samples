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

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.FloatRange
import com.squareup.picasso.Transformation

/**
 * Picasso Transformation that blurs an image using RenderScript
 *
 * This is helpful when you have a busy background or a dynamic background that you don't know
 * the contents of (e.g., fetching an image from a server where that image could be anything). If
 * you don't know whether the image will have enough contrast with text on top, you should also
 * overlay it with a color to ensure sufficient contrast.
 */
class BlurImageTransformation(
    context: Context,
    @FloatRange(from = 1.0, to = 25.0) val radius: Float = 25f
) :
    Transformation {

    private val imageBlurManager = ImageBlurManager(context)

    // Key for Picasso to identify this transformation
    private val key: String by lazy {
        "BlurImageTransformation($radius)"
    }

    override fun key(): String {
        return key
    }

    override fun transform(source: Bitmap?): Bitmap {
        requireNotNull(source)
        return imageBlurManager.blur(source, radius).also {
            source.recycle()
        }
    }
}
