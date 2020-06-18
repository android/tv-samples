/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tv.reference.deeplink

import com.android.tv.reference.shared.datamodel.Video

/**
 * When checking a deep link, there are two possibilities: success or error. On success, the app
 * needs to receive the Video object to continue. The app doesn't handle different types of errors
 * differently, so it simply needs to know an error occurred.
 */
sealed class DeepLinkResult {
    data class Success(val video: Video) : DeepLinkResult()
    object Error : DeepLinkResult()
}
