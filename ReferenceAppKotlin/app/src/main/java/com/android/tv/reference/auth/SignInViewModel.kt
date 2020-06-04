package com.android.tv.reference.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import java.lang.Exception

class SignInViewModel(private val userManager: UserManager) :
    ViewModel() {
    private val signInStatusMutable = MutableLiveData<SignInStatus>();
    val signInStatus: LiveData<SignInStatus> = signInStatusMutable;

    fun signInWithPassword(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            signInStatusMutable.value = SignInStatus.Error.InputError
            return
        }
        signInStatusMutable.value = authWithPassword(username, password)
    }

    fun signInWithOneTap(credential: SignInCredential) {
        signInStatusMutable.value = authWithOneTapCredential(credential)
    }

    private fun authWithPassword(username: String, password: String): SignInStatus =
        when (val result = userManager.authWithPassword(username, password)) {
            is UserManager.AuthResult.Success -> SignInStatus.ShouldSavePassword(username, password)
            is UserManager.AuthResult.Failure -> {
                when (result.error) {
                    is AuthClientError.AuthenticationError -> SignInStatus.Error.InvalidPassword
                    else -> SignInStatus.Error.ServerError
                }
            }
        }

    private fun authWithGoogleIdToken(credential: SignInCredential): SignInStatus {
        TODO("Not yet implemented")
    }

    private fun authWithOneTapCredential(credential: SignInCredential) =
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
