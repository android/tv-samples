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

import androidx.lifecycle.MutableLiveData
import com.android.tv.reference.R
import com.google.android.gms.auth.api.identity.SignInCredential

/**
 * Handles sign in and sign out process and exposes methods used to authenticate the user and
 * persist and retrieve user information.
 */
class UserManager(
    private val server: AuthClient,
    private val storage: UserInfoStorage
) {
    val userInfoLiveData = MutableLiveData<UserInfo?>(storage.readUserInfo())

    init {
        validateToken()
    }

    fun signOut() {
        userInfoLiveData.value?.let {
            server.invalidateToken(it.token)
            updateUserInfo(null)
        }
    }

    fun authWithPassword(username: String, password: String): Boolean {
        val userInfo = server.authWithPassword(username, password) ?: return false
        updateUserInfo(userInfo)
        return true
    }

    fun authWithGoogle(credential: SignInCredential): Boolean {
        TODO("Not yet implemented")
    }

    private fun validateToken() {
        userInfoLiveData.value?.let { updateUserInfo(server.validateToken(it.token)) }
    }

    private fun updateUserInfo(userInfo: UserInfo?) {
        userInfoLiveData.postValue(userInfo)
        userInfo?.let { storage.writeUserInfo(it) } ?: storage.clearUserInfo()
    }

    companion object {
        const val signInFragmentId = R.id.action_global_signInFragment
    }
}