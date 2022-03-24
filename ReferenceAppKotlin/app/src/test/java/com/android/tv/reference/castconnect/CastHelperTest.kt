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
package com.android.tv.reference.castconnect

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test cases to process intent for Cast and setting MediaSession token for Cast.
 */
@RunWith(AndroidJUnit4::class)
class CastHelperTest {

    @Test
    fun processCastIntent_invalidIntent() {
        val invalidIntent = Intent()

        // Set boolean variable isCallbackInvoked to indicate that the callback has not been
        // invoked yet
        var isCallbackInvoked = false

        val castHelperTest = CastHelper(
            {
                // Set boolean to true to detect that callback was invoked by the
                // MediaLoadCommandCallback
                isCallbackInvoked = true
            },
            ApplicationProvider.getApplicationContext()
        )

        // Pass an invalid intent to be processed by the Cast SDK
        val result = castHelperTest.validateAndProcessCastIntent(invalidIntent)

        // Assert that intent was not recognized by the cast SDK
        assertThat(result).isFalse()

        /**
         * Assert that the callback was not invoked to play the video since the
         * MediaLoadCommandCallback was not triggered, as the intent was not recognized by Cast SDK.
         */
        assertThat(isCallbackInvoked).isFalse()
    }
}
