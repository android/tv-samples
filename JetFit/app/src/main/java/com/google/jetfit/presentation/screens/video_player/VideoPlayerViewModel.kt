package com.google.jetfit.presentation.screens.video_player

import androidx.lifecycle.ViewModel
import com.google.jetfit.data.repositories.JetFitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val jetFitRepository: JetFitRepository
) : ViewModel() {

}