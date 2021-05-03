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

import com.android.tv.reference.shared.util.Result

/**
 * The authority that verifies the user's identity based on an existing token or credentials.
 * The verification is expected to be server-side.
 */
interface AuthClient {
    suspend fun validateToken(token: String): Result<UserInfo>
    suspend fun authWithPassword(username: String, password: String): Result<UserInfo>
    suspend fun authWithGoogleIdToken(idToken: String): Result<UserInfo>
    suspend fun invalidateToken(token: String): Result<Unit>
}

sealed class AuthClientError(message: String) : Exception(message) {
    object AuthenticationError : AuthClientError("Error authenticating user")
    data class ServerError(
        val errorCause: Exception
    ) : AuthClientError("Server error: ${errorCause.message}")
}

/**
 * A simple implementation of AuthClient that works without a server and is useful for testing
 * purposes.
 */
class MockAuthClient : AuthClient {
    private val mockUser = UserInfo("myUserToken", "A. N. Other")

    override suspend fun validateToken(token: String): Result<UserInfo> {
        if (token == mockUser.token) {
            return Result.Success(mockUser)
        }
        return Result.Error(AuthClientError.AuthenticationError)
    }

    override suspend fun authWithPassword(username: String, password: String): Result<UserInfo> {
        if (username == MOCK_USER_EMAIL) {
            return Result.Success(mockUser)
        }
        return Result.Error(AuthClientError.AuthenticationError)
    }

    override suspend fun authWithGoogleIdToken(idToken: String): Result<UserInfo> {
        if (idToken.isEmpty()) {
            return Result.Error(AuthClientError.AuthenticationError)
        }
        return Result.Success(mockUser)
    }

    override suspend fun invalidateToken(token: String): Result<Unit> = Result.Success(Unit)

    companion object {
        const val MOCK_USER_EMAIL = "user@gmail.com"
    }
}
