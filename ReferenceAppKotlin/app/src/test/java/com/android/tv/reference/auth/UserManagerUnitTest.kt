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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.tv.reference.shared.util.Result
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy

class UserManagerUnitTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authClient: AuthClient

    @Mock
    private lateinit var mockIdentityStorage: UserInfoStorage

    @Spy
    private lateinit var spyIdentityStorage: UserInfoStorage

    private val validUser = UserInfo("validToken", "Name")

    @Before
    fun setupTests() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(runBlocking { authClient.authWithPassword("valid@gmail.com", "foo") })
            .thenReturn(Result.Success(validUser))
        Mockito.`when`(runBlocking { authClient.authWithPassword("invalid@gmail.com", "bar") })
            .thenReturn(Result.Error(AuthClientError.AuthenticationError))
        Mockito.`when`(runBlocking { authClient.validateToken("validToken") })
            .thenReturn(Result.Success(validUser))
    }

    @Test
    fun authWithPassword_signsInWithValidPasswordAndSignsOut() {
        val userManager = UserManager(authClient, spyIdentityStorage)
        runBlocking { userManager.authWithPassword("valid@gmail.com", "foo") }
        assertThat(userManager.isSignedIn()).isTrue()
        assertThat(userManager.userInfo.value).isNotNull()
        assertThat(userManager.userInfo.value!!.token).isEqualTo("validToken")
        Mockito.verify(spyIdentityStorage).writeUserInfo(validUser)
        runBlocking { userManager.signOut() }
        assertThat(userManager.isSignedIn()).isFalse()
        assertThat(userManager.userInfo.value).isNull()
        Mockito.verify(spyIdentityStorage).clearUserInfo()
    }

    @Test
    fun authWithPassword_failsWithInvalidPassword() {
        val userManager = UserManager(authClient, mockIdentityStorage)
        runBlocking { userManager.authWithPassword("invalid@gmail.com", "bar") }
        assertThat(userManager.isSignedIn()).isFalse()
        assertThat(userManager.userInfo.value).isNull()
    }

    @Test
    fun constructor_readsFromStorage() {
        Mockito.`when`(mockIdentityStorage.readUserInfo()).thenReturn(validUser)
        val userManager = UserManager(authClient, mockIdentityStorage)
        assertThat(userManager.isSignedIn()).isTrue()
        assertThat(userManager.userInfo.value).isNotNull()
        assertThat(userManager.userInfo.value!!.token).isEqualTo("validToken")
    }
}
