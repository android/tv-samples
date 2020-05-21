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

package com.android.tv.reference.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.tv.reference.shared.event.SingleUseEvent

class UserInfoViewModel(private val userManager: UserManager) :
    ViewModel() {
    val userInfo: LiveData<UserInfo?> = userManager.userInfoLiveData

    private val signInErrorMutable = MutableLiveData<SingleUseEvent<Int>>();
    val signInError: LiveData<SingleUseEvent<Int>> = signInErrorMutable;

    val isSignedIn: Boolean
        get() = userInfo.value != null

    fun signOut() = userManager.signOut()
    fun signInWithPassword(username: String, password: String) {
        if (!userManager.authWithPassword(username, password)) {
            signInErrorMutable.value = SingleUseEvent(SIGN_IN_ERROR_INVALID_PASSWORD)
        }
    }

    companion object {
        const val SIGN_IN_ERROR_INVALID_PASSWORD = 1
    }
}

class UserInfoViewModelFactory(private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var viewModel: T? = null
        if (modelClass.isAssignableFrom(modelClass)) {
            // TODO: Good candidate for dependency injection
            val userManager = UserManager(MockAuthClient(), DefaultUserInfoStorage(context))
            viewModel = modelClass.cast(UserInfoViewModel(userManager))!!
        }
        return viewModel ?: throw IllegalArgumentException("Unknown ViewModel class")
    }
}
