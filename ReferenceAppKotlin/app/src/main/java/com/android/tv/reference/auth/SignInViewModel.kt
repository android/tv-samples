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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.tv.reference.shared.util.Result
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel(private val userManager: UserManager) :
    ViewModel() {
    private val signInStatusMutable = MutableLiveData<SignInStatus>()
    val signInStatus: LiveData<SignInStatus> = signInStatusMutable

    fun signInWithPassword(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            signInStatusMutable.value = SignInStatus.Error.InputError
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                signInStatusMutable.postValue(authWithPassword(username, password))
            }
        }
    }

    fun signInWithOneTap(credential: SignInCredential) =
        viewModelScope.launch(Dispatchers.IO) {
            signInStatusMutable.postValue(authWithOneTapCredential(credential))
        }

    private suspend fun authWithPassword(username: String, password: String): SignInStatus =
        when (val result = userManager.authWithPassword(username, password)) {
            is Result.Success -> SignInStatus.ShouldSavePassword(username, password)
            is Result.Error -> {
                when (result.exception) {
                    is AuthClientError.AuthenticationError -> SignInStatus.Error.InvalidPassword
                    else -> SignInStatus.Error.ServerError
                }
            }
        }

    private fun authWithGoogleIdToken(credential: SignInCredential): SignInStatus {
        TODO("Not yet implemented")
    }

    private suspend fun authWithOneTapCredential(credential: SignInCredential) =
        when {
            credential.googleIdToken != null -> authWithGoogleIdToken(credential)
            credential.password != null -> authWithPassword(credential.id, credential.password!!)
            else -> SignInStatus.Error.OneTapInvalid
        }

    fun processOneTapError(exception: Exception) {
        if (exception is ApiException && exception.statusCode == CommonStatusCodes.CANCELED) {
            // Cancelled is not an error
            return
        }
        signInStatusMutable.value = SignInStatus.Error.OneTapError(exception)
    }

    fun finishSavePassword() {
        if (signInStatus.value is SignInStatus.ShouldSavePassword) {
            signInStatusMutable.value = SignInStatus.Success
        }
    }

    sealed class SignInStatus {
        object Success : SignInStatus()
        class ShouldSavePassword(val username: String, val password: String) : SignInStatus()
        sealed class Error : SignInStatus() {
            object InputError : Error()
            object InvalidPassword : Error()
            object ServerError : Error()
            object OneTapInvalid : Error()
            class OneTapError(val exception: Exception) : Error()
        }
    }
}

class SignInViewModelFactory(private val userManager: UserManager) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var viewModel: T? = null
        if (modelClass.isAssignableFrom(modelClass)) {
            viewModel = modelClass.cast(SignInViewModel(userManager))!!
        }
        return viewModel ?: throw IllegalArgumentException("Unknown ViewModel class")
    }
}
