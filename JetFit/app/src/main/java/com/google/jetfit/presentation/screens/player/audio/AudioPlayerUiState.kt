package com.google.jetfit.presentation.screens.player.audio

import javax.annotation.concurrent.Immutable

@Immutable
data class AudioPlayerUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val songUiState: SongUiState = SongUiState()
)

@Immutable
data class SongUiState(
    val id: String = "",
    val audioUrl: String = "",
    val title: String = "",
    val author: String = "",
    val imageUrl: String? = null,
    val date: String? = null,
)