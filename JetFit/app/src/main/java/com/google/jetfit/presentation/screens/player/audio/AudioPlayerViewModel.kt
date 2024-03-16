package com.google.jetfit.presentation.screens.player.audio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetfit.data.repositories.JetFitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioPlayerViewModel @Inject constructor(
    private val jetFitRepository: JetFitRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AudioPlayerUiState())
    val state get() = _state.asStateFlow()

    init {
        getSongById()
    }

    private fun getSongById() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val song = jetFitRepository.getSongById("123456sdasdsa")
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        songUiState = SongUiState(
                            title = song.title,
                            author = song.author,
                            audioUrl = song.audioUrl,
                            id = song.id,
                            imageUrl = song.imageUrl,
                            date = song.createdDate,
                        )
                    )
                }
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(isLoading = false, error = e.message.toString())
            }
        }
    }
}