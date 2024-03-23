package com.google.jetfit.presentation.screens.profileSelector

import javax.annotation.concurrent.Immutable

@Immutable
data class ProfileSelectorUiState(
    val isLoading: Boolean = true,
    val profiles: List<ProfileUiState> = emptyList(),
    val lastFocusedItemId: String = "",
    val profileItemAlreadyFocused: Boolean = false,
    val error: String? = null,
)

@Immutable
data class ProfileUiState(
    val id: String,
    val name: String,
    val avatar: String,
)