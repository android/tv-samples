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

package com.android.tv.reference.shared.datamodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Represents a video that can be played in the app
 */
@Parcelize
class Video(val name: String,
    val description: String,
    val videoUri: String,
    val thumbnailUri: String,
    @SerializedName("backgroundUri") val backgroundImageUri: String,
    val category: String
) : Parcelable {

    // TODO remove method and include ID in the constructor
    public fun getId(): String {
        return videoUri
    }
    // TODO remove method and include the deep link in the constructor
    public fun getDeepLink(): String {
        return videoUri
    }

    override fun toString(): String {
        return "Video(name='$name')";
    }
}
