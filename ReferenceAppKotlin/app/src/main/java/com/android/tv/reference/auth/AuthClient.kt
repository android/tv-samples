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

/**
 * The authority that verifies the user's identity based on an existing token or credentials.
 * The verification is expected to be server-side.
 */
interface AuthClient {
    fun validateToken(token: String): UserInfo?
    fun authWithPassword(username: String, password: String): UserInfo?
    fun authWithGoogleIdToken(idToken: String): UserInfo?
    fun invalidateToken(token: String)
}

/**
 * A simple implementation of AuthClient that works without a server and is useful for testing
 * purposes.
 */
class MockAuthClient : AuthClient {
    private val mockUser = UserInfo("myUserToken", "A. N. Other")
    private val mockUserEmail = "user@gmail.com"

    override fun validateToken(token: String): UserInfo? {
        if (token == mockUser.token) {
            return mockUser
        }
        return null
    }

    override fun authWithPassword(username: String, password: String): UserInfo? {
        if (username == mockUserEmail) {
            return mockUser
        }
        return null
    }

    override fun authWithGoogleIdToken(idToken: String): UserInfo? {
        TODO("Not yet implemented")
    }

    override fun invalidateToken(token: String) {}
}
