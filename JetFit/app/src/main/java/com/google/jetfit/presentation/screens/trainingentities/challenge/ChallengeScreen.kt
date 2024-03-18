package com.google.jetfit.presentation.screens.trainingentities.challenge

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.jetfit.components.ImageWithGradient
import com.google.jetfit.presentation.screens.trainingentities.composables.bringIntoViewIfChildrenAreFocused
import com.google.jetfit.presentation.theme.JetFitTheme

@Composable
fun ChallengeScreen() {
    val challengeViewModel: ChallengeViewModel = hiltViewModel()
    val state by challengeViewModel.state.collectAsState()
    ChallengeScreenContent(state)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChallengeScreenContent(
    state: ChallengeUiState,
) {
    val pagerState = rememberPagerState { state.pages.size }
    val alpha: Float by animateFloatAsState(
        if (pagerState.currentPage == 0) 1f else 0.2f,
        label = "alpha effect"
    )
    Box(modifier = Modifier.fillMaxSize()) {
        ImageWithGradient(
            Modifier.align(Alignment.TopEnd),
            state.imageUrl,
            "Challenge Image",
            imagePassThrough = alpha
        )
        VerticalPager(state = pagerState, beyondBoundsPageCount = 1) { pageIndex->
            when(state.pages[pageIndex]) {
                Page.Details -> {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .bringIntoViewIfChildrenAreFocused()
                    ) {
                        ChallengeDetails(state)
                    }
                }
                Page.Tabs -> {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .bringIntoViewIfChildrenAreFocused()) {
                        ChallengeTabs(state)
                    }
                }
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_TELEVISION,
    device = "id:tv_4k"
)
@Composable
fun ChallengeScreenPreview() {
    JetFitTheme {
        ChallengeScreen()
    }
}