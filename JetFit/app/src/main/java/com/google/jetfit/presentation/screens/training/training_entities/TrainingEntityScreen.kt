package com.google.jetfit.presentation.screens.training.training_entities

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.jetfit.presentation.screens.training.training_entities.composables.ChallengeTabs
import com.google.jetfit.presentation.screens.training.training_entities.composables.RoundedGradientImage
import com.google.jetfit.presentation.screens.training.training_entities.composables.TrainingEntityDetails

@Composable
fun TrainingEntityScreen(viewModel: TrainigEntityViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    TrainingEntityContent(
        state = state,
        interactions = viewModel
    )
}

@Composable
private fun TrainingEntityContent(
    state: TrainingEntityUiState,
    interactions: TrainingEntityInteractions
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = !state.isChallengeTabsVisible) {
            Box(contentAlignment = Alignment.BottomStart) {
                RoundedGradientImage(imageUrl = state.imageUrl)
                TrainingEntityDetails(
                    state = state,
                    onClickStart = {},
                    onClickSecondaryButton = {},
                    onClickChallengesPlan = interactions::onClickShowChallengeTabs,
                    onClickRoutineFavourite = {}
                )
            }
        }
        AnimatedVisibility(visible = state.isChallengeTabsVisible) {
            ChallengeTabs(
                state = state,
                onClickBackChallenge = interactions::onClickShowChallengeTabs,
                onClickCard = {}
            )
        }

    }
}