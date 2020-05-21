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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.tv.reference.R
import com.android.tv.reference.databinding.FragmentProfileBinding

/**
 * Placeholder fragment that has a different behavior depending on whether the user is signed in.
 */
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: UserInfoViewModel by activityViewModels {
        UserInfoViewModelFactory(
            requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.signInButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(UserManager.signInFragmentId)
        })
        binding.signOutButton.setOnClickListener {
            viewModel.signOut()
        }
        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            binding.displayName.text = it?.displayName ?: getString(R.string.not_signed_in)
        })
        return binding.root
    }
}
