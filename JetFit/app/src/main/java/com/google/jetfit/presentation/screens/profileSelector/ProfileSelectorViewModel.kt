package com.google.jetfit.presentation.screens.profileSelector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetfit.data.entities.Profile
import com.google.jetfit.data.repositories.JetFitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSelectorViewModel @Inject constructor(
    private val jetFitRepository: JetFitRepository
) : ViewModel(), ProfileSelectorInteraction {

    private val _state: MutableStateFlow<ProfileSelectorUiState> by lazy {
        MutableStateFlow(ProfileSelectorUiState())
    }
    val state = _state.asStateFlow()

    init {
        getUserProfiles()
    }

    private fun getUserProfiles() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = false,
                    profiles = jetFitRepository.getUserProfiles().map {
                        it.toProfileUiState()
                    },
                )
            }
        }
    }

    private fun Profile.toProfileUiState() = ProfileUiState(
        id = id,
        name = name,
        avatar = avatar
    )


    override fun onClickProfileSelected(id: String) {
        //TODO("Not yet implemented")
    }

    override fun onClickSignIn() {
        //TODO("Not yet implemented")
    }
}