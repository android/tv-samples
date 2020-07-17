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
package com.android.tv.reference.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

/**
 * An identity storage persists identity data such as user information and ID token locally.
 */
interface UserInfoStorage {
    fun readUserInfo(): UserInfo?
    fun writeUserInfo(userInfo: UserInfo)
    fun clearUserInfo()
}

/**
 * SharedPreferences-backed identity storage. Production apps should prefer a more sophisticated
 * mechanism such as Room.
 */
class DefaultUserInfoStorage(context: Context) : UserInfoStorage {
    private val KEY_PREFIX = "userInfo_"
    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    override fun readUserInfo(): UserInfo? {
        return getString("token")?.let { token ->
            val displayName = getString("displayName")
            return UserInfo(token, displayName ?: "")
        }
    }

    override fun writeUserInfo(userInfo: UserInfo) {
        putStrings(
            mapOf(
                "token" to userInfo.token,
                "displayName" to userInfo.displayName
            )
        )
    }

    override fun clearUserInfo() {
        putStrings(
            mapOf(
                "token" to null,
                "displayName" to null
            )
        )
    }

    private fun getString(key: String): String? =
        sharedPreferences.getString("${KEY_PREFIX}$key", null)

    private fun putString(key: String, value: String) = sharedPreferences.edit(true) {
        putString("${KEY_PREFIX}$key", value)
    }

    private fun putStrings(strings: Map<String, String?>) = sharedPreferences.edit(true) {
        strings.forEach {
            putString(it.key, it.value)
        }
    }
}
