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
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.FloatRange
import androidx.annotation.WorkerThread

/**
 * Helper class to simplify blurring images
 *
 * Only the application context is retained, so it's safe to use this across Activity lifecycles.
 * The backing RenderScript is lazily created when a blur is requested, so it's also lightweight
 * to construct this class in cases where a blur is only sometimes needed.
 */
class ImageBlurManager(context: Context) {

    private val applicationContext = context.applicationContext

    private val renderScript by lazy {
        RenderScript.create(applicationContext)
    }

    private val blurScript by lazy {
        ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
    }

    /**
     * Returns a new blurred Bitmap based on the one passed in
     *
     * The passed Bitmap is NOT recycled by this method.
     */
    @WorkerThread
    fun blur(
        inputBitmap: Bitmap,
        @FloatRange(from = 1.0, to = 25.0) radius: Float = 25f
    ): Bitmap {
        val inputAllocation = Allocation.createFromBitmap(renderScript, inputBitmap)
        val outputAllocation = Allocation.createTyped(renderScript, inputAllocation.type)
        blurScript.setInput(inputAllocation)
        blurScript.setRadius(radius)
        blurScript.forEach(outputAllocation)
        val outputBitmap =
            Bitmap.createBitmap(inputBitmap.width, inputBitmap.height, inputBitmap.config)
        outputAllocation.copyTo(outputBitmap)
        inputAllocation.destroy()
        outputAllocation.destroy()
        return outputBitmap
    }
}
