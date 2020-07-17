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
import org.junit.Assert
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
        Mockito.`when`(authClient.authWithPassword("valid@gmail.com", "foo"))
            .thenReturn(AuthClientResult.Success(validUser))
        Mockito.`when`(authClient.authWithPassword("invalid@gmail.com", "bar"))
            .thenReturn(AuthClientResult.Failure(AuthClientError.AuthenticationError))
        Mockito.`when`(authClient.validateToken("validToken"))
            .thenReturn(AuthClientResult.Success(validUser))
    }

    @Test
    fun authWithPassword_signsInWithValidPasswordAndSignsOut() {
        val userManager = UserManager(authClient, spyIdentityStorage)
        userManager.authWithPassword("valid@gmail.com", "foo")
        Assert.assertTrue(userManager.isSignedIn)
        Assert.assertNotNull(userManager.userInfo.value)
        Assert.assertEquals("validToken", userManager.userInfo.value!!.token)
        Mockito.verify(spyIdentityStorage).writeUserInfo(validUser)
        userManager.signOut()
        Assert.assertFalse(userManager.isSignedIn)
        Assert.assertNull(userManager.userInfo.value)
        Mockito.verify(spyIdentityStorage).clearUserInfo()
    }

    @Test
    fun authWithPassword_failsWithInvalidPassword() {
        val userManager = UserManager(authClient, mockIdentityStorage)
        userManager.authWithPassword("invalid@gmail.com", "bar")
        Assert.assertFalse(userManager.isSignedIn)
        Assert.assertNull(userManager.userInfo.value)
    }

    @Test
    fun constructor_readsFromStorage() {
        Mockito.`when`(mockIdentityStorage.readUserInfo()).thenReturn(validUser)
        val userManager = UserManager(authClient, mockIdentityStorage)
        Assert.assertTrue(userManager.isSignedIn)
        Assert.assertNotNull(userManager.userInfo.value)
        Assert.assertEquals("validToken", userManager.userInfo.value!!.token)
    }
}
