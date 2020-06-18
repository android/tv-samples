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

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.tv.reference.repository.VideoRepository
import com.android.tv.reference.repository.VideoRepositoryFactory
import com.android.tv.reference.shared.event.SingleUseEvent
import com.squareup.moshi.JsonDataException

/**
 * A ViewModel attached at the Activity level when the app is given a deep link.
 *
 * This ViewModel is responsible for validating that the deep link points to a real video
 * that we have metadata for and loading the corresponding Video object.
 */
class DeepLinkViewModel(application: Application, deepLinkUri: Uri) : ViewModel() {
    private val videoRepository = VideoRepositoryFactory.getVideoRepository(application)
    val deepLinkResult = MutableLiveData<SingleUseEvent<DeepLinkResult>>()

    init {
        deepLinkResult.value = SingleUseEvent(getDeepLinkVideo(deepLinkUri, videoRepository))
    }

    companion object {
        private const val TAG = "DeepLinkViewModel"

        fun getDeepLinkVideo(deepLinkUri: Uri, videoRepository: VideoRepository): DeepLinkResult {
            try {
                videoRepository.getVideoById((deepLinkUri.toString()))?.let {
                    return DeepLinkResult.Success(it)
                }
            } catch (e: JsonDataException) {
                Log.w(TAG, "Failed to load deep link for $deepLinkUri", e)
            }
            return DeepLinkResult.Error
        }
    }
}

class DeepLinkViewModelFactory(private val application: Application, private val deepLinkUri: Uri) :
    ViewModelProvider.AndroidViewModelFactory(
        application
    ) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DeepLinkViewModel(application, deepLinkUri) as T
    }
}
