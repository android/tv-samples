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

import android.app.PendingIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.tv.reference.R
import com.android.tv.reference.auth.SignInViewModel.SignInStatus
import com.android.tv.reference.databinding.FragmentSignInBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException

/**
 * Fragment that allows the user to sign in using an email and password.
 */
class SignInFragment : Fragment() {
    private val viewModel: SignInViewModel by viewModels {
        SignInViewModelFactory(
            UserManager.getInstance(
                requireContext()
            )
        )
    }

    private val signInClient by lazy { Identity.getSignInClient(requireContext()) }
    // private val credentialSavingClient by lazy { Identity.getCredentialSavingClient(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignInBinding.inflate(inflater, container, false)
        viewModel.signInStatus.observe(
            viewLifecycleOwner,
            { status ->
                when (status) {
                    is SignInStatus.Success -> findNavController().popBackStack()
                    is SignInStatus.ShouldSavePassword -> startSavePasswordWithGoogle(
                        status.username,
                        status.password
                    )
                    is SignInStatus.Error -> {
                        val errorText = when (status) {
                            is SignInStatus.Error.InputError ->
                                getString(R.string.empty_username_or_password)
                            is SignInStatus.Error.InvalidPassword ->
                                getString(R.string.invalid_credentials)
                            is SignInStatus.Error.ServerError -> getString(R.string.server_error)
                            is SignInStatus.Error.OneTapInvalid ->
                                getString(R.string.invalid_credentials)
                            is SignInStatus.Error.OneTapError -> getString(R.string.onetap_error)
                            else -> getString(R.string.unknown_error)
                        }
                        binding.signInError.text = getString(R.string.sign_in_error, errorText)
                    }
                }
            }
        )
        binding.signInIntro.text = getString(R.string.sign_in_intro, MockAuthClient.MOCK_USER_EMAIL)
        binding.signInButton.setOnClickListener {
            val username = binding.usernameEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            viewModel.signInWithPassword(username, password)
        }
        startGoogleOneTapRequest()
        return binding.root
    }

    /*
      Initiates Google's One Tap flow.
    */
    private fun startGoogleOneTapRequest() {
        val request = BeginSignInRequest.Builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.Builder().setSupported(true).build()
            ).build()
        signInClient.beginSignIn(request)
            .addOnSuccessListener { launchGoogleOneTapUi(it.pendingIntent) }
            .addOnFailureListener { viewModel.processOneTapError(it) }
    }

    /*
      Launches the UI overlay associated with Google's One Tap flow. The user is prompted to use
      existing saved credentials. On success, attempt to immediately use these to sign in.
    */
    private fun launchGoogleOneTapUi(pendingIntent: PendingIntent) {
        val request = IntentSenderRequest.Builder(pendingIntent.intentSender).build()
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            try {
                viewModel.signInWithOneTap(signInClient.getSignInCredentialFromIntent(it.data))
            } catch (e: ApiException) {
                viewModel.processOneTapError(e)
            }
        }.launch(request)
    }

    /*
      Initiates Google's password saving flow.
    */
    private fun startSavePasswordWithGoogle(email: String, password: String) {
        if (isPlayServicesUnavailable()) {
            viewModel.finishSavePassword()
            return
        }
        viewModel.finishSavePassword()
        /*
        val request = SavePasswordRequest.builder()
            .setSignInPassword(SignInPassword(email, password))
            .build()
        credentialSavingClient.savePassword(request)
            .addOnSuccessListener { launchSavePasswordUi(it.pendingIntent) }
            .addOnFailureListener { viewModel.finishSavePassword() }
         */
    }

    /*
      Launches the UI overlay associated with Google's password saving flow. Regardless of success
      result, the sign in fragment is dismissed afterwards.
    */
    private fun launchSavePasswordUi(pendingIntent: PendingIntent) {
        val request = IntentSenderRequest.Builder(pendingIntent.intentSender).build()
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { viewModel.finishSavePassword() }.launch(request)
    }

    private fun isPlayServicesUnavailable() = GoogleApiAvailability.getInstance()
        .isGooglePlayServicesAvailable(requireContext()) != ConnectionResult.SUCCESS
}
