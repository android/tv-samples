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
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * An auth client that connects to a real server.
 */
class RemoteAuthClient : AuthClient {
    companion object {
        const val BASE_URL = "https://example.com"
    }

    private val service = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(IdentityService::class.java)

    override suspend fun validateToken(token: String) =
        wrapResult { service.validateToken(token) }

    override suspend fun authWithPassword(username: String, password: String) =
        wrapResult { service.authWithPassword(username, password) }

    override suspend fun authWithGoogleIdToken(idToken: String) =
        wrapResult { service.authWithGoogleIdToken(idToken) }

    override suspend fun invalidateToken(token: String) =
        wrapResult { service.invalidateToken(token) }

    private suspend fun <T> wrapResult(f: suspend () -> T): Result<T> =
        try {
            Result.Success(f())
        } catch (exception: Exception) {
            Result.Error(AuthClientError.ServerError(exception))
        }

    private interface IdentityService {
        @POST("/validateToken")
        suspend fun validateToken(@Body token: String): UserInfo

        @POST("/authWithPassword")
        suspend fun authWithPassword(@Body username: String, @Body password: String): UserInfo

        @POST("/validateToken")
        suspend fun authWithGoogleIdToken(@Body idToken: String): UserInfo

        @POST("/invalidateToken")
        suspend fun invalidateToken(@Body token: String): Unit
    }
}
