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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.tv.reference.databinding.FragmentSignInBinding

/**
 * Fragment that allows the user to sign in using an email and password.
 */
class SignInFragment : Fragment() {
    private val identityProvider = IdentityProvider()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSignInBinding.inflate(inflater, container, false)
        binding.signInButton.setOnClickListener {
            val username = binding.usernameEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            if (identityProvider.validateCredentials(username, password)) {
                Toast.makeText(requireContext(), "TODO: Handle success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "TODO: Handle invalid", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
}
