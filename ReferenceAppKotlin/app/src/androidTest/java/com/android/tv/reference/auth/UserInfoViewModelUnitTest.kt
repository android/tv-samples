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

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@RunWith(AndroidJUnit4::class)
@SmallTest
class UserInfoViewModelUnitTest {
    private val identityServer = MockAuthClient()

    @Mock
    private lateinit var identityStorage: UserInfoStorage

    @Mock
    private lateinit var mockApplicationContext: Application

    @Rule
    @JvmField
    val instantTaskRule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setupTests() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(mockApplicationContext.applicationContext).thenReturn(mockApplicationContext)
    }

    @Test
    fun signInWithPassword_isCorrect() {
        val viewModel = UserInfoViewModel(UserManager(identityServer, identityStorage))
        viewModel.signInWithPassword("user@gmail.com", "")
        Assert.assertTrue(viewModel.isSignedIn)
        Assert.assertNotNull(viewModel.userInfo.value)
        Assert.assertEquals("myUserToken", viewModel.userInfo.value!!.token)
    }

    @Test
    fun signInWithPassword_error() {
        val viewModel = UserInfoViewModel(UserManager(identityServer, identityStorage))
        viewModel.signInWithPassword("invalid@gmail.com", "")
        Assert.assertFalse(viewModel.isSignedIn)
        Assert.assertNull(viewModel.userInfo.value)
        Assert.assertEquals(UserInfoViewModel.SIGN_IN_ERROR_INVALID_PASSWORD, viewModel.signInError.value?.getContentIfNotHandled())
    }

    @Test
    fun readsFromStorage() {
        Mockito.`when`(identityStorage.readUserInfo())
            .thenReturn(UserInfo("myUserToken", "User"))
        val viewModel = UserInfoViewModel(UserManager(identityServer, identityStorage))
        Assert.assertTrue(viewModel.isSignedIn)
        Assert.assertNotNull(viewModel.userInfo.value)
        Assert.assertEquals("myUserToken", viewModel.userInfo.value!!.token)
    }
}
