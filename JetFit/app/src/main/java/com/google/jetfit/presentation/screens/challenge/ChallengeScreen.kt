package com.google.jetfit.presentation.screens.challenge

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Tab
import androidx.tv.material3.TabDefaults
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.components.CinematicBackground
import com.google.jetfit.components.MetricItem
import com.google.jetfit.components.PlanTextWithIcon
import com.google.jetfit.components.RegimenCardItem
import com.google.jetfit.components.RegimenInformationLayout
import com.google.jetfit.components.RegimenMetrics
import com.google.jetfit.components.UnderlinedIndicator
import com.google.jetfit.presentation.theme.JetFitTheme

@Composable
fun ChallengeScreen() {
    val challengeViewModel: ChallengeViewModel = hiltViewModel()
    val state by challengeViewModel.state.collectAsState()
    ChallengeScreenContent(state, challengeViewModel)
}

@Composable
fun ChallengeScreenContent(
    state: ChallengeUiState,
    interactionListener: ChallengeInteractionListener
) {
    val alpha: Float by animateFloatAsState(
        if (state.shouldShowDetails) 1f else 0.2f,
        label = "alpha effect"
    )
    Box(modifier = Modifier.fillMaxSize()) {
        CinematicBackground(
            Modifier.align(Alignment.TopEnd),
            "https://figmage.com/images/FbgDhnXP05yKpdsLJfTQo.png",
            "poster",
            imagePassThrough = alpha
        )
        AnimatedVisibility(
            visible = state.shouldShowDetails,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            ChallengeDetails(
                state,
                interactionListener
            )
        }
        AnimatedVisibility(
            visible = !state.shouldShowDetails,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            ChallengeTabs(
                state,
                interactionListener
            )
        }
    }

}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ChallengeDetails(
    state: ChallengeUiState,
    interactionListener: ChallengeInteractionListener
) {
    RegimenInformationLayout(
        cinematicBackground = {},
        regimenInformation = {
            Text(
                text = state.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = state.title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = state.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        regimenMetrics = {
            RegimenMetrics(
                Modifier.padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                MetricItem(state.numberOfDays, "Days")
                MetricItem(state.intensity, "Intensity")
                MetricItem(state.minutesPerDay, "Minutes per day")
            }
        },
        regimenActions = {
            Button(
                onClick = { interactionListener.onStartSessionClicked() },
                modifier = Modifier.onKeyEvent {
                    when (it.type) {
                        KeyEventType.KeyDown -> {
                            interactionListener.onDownPressed()
                            true
                        }

                        else -> false
                    }
                }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.play_icon),
                        contentDescription = "Play icon"
                    )
                    Text(text = "Start session", modifier = Modifier.padding(start = 6.dp))
                }
            }
            OutlinedButton(
                onClick = { interactionListener.onAddToFavoritesClicked() },
                modifier = Modifier.onKeyEvent {
                    when (it.key) {
                        Key.DirectionDown -> {
                            interactionListener.onDownPressed()
                            true
                        }

                        else -> false
                    }
                }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.fav_icon),
                        contentDescription = "favorite icon",
                    )
                    Text(
                        text = "Add to favorites",
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }
        }
    ) {
        PlanTextWithIcon(
            modifier = Modifier.padding(top = 36.dp),
            subtitle = "Weekly plan",
            painter = painterResource(id = R.drawable.down_arrow_head_icon),
            contentDescription = "down arrow head icon"
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChallengeTabs(
    state: ChallengeUiState,
    interactionListener: ChallengeInteractionListener
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                PlanTextWithIcon(
                    modifier = Modifier.padding(start = 48.dp, top = 24.dp),
                    subtitle = "Challenge details",
                    painter = painterResource(id = R.drawable.up_arrow_head_icon),
                    contentDescription = "Up arrow head icon"
                )
                Text(
                    text = "Weekly plan",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 52.dp, start = 48.dp)
                )
                TabRow(
                    selectedTabIndex = state.selectedTabIndex,
                    separator = { Spacer(modifier = Modifier.width(16.dp)) },
                    indicator = { tabPositions, doesTabRowHaveFocus ->
                        UnderlinedIndicator(
                            currentTabPosition = tabPositions[state.selectedTabIndex],
                            doesTabRowHaveFocus = doesTabRowHaveFocus,
                            activeColor = MaterialTheme.colorScheme.secondary,
                        )
                    },
                    modifier = Modifier
                        .padding(top = 24.dp, start = 48.dp)
                        .focusRestorer()
                        .onKeyEvent {
                            when (it.key) {
                                Key.DirectionUp -> {
                                    interactionListener.onUpPressed()
                                    true
                                }

                                else -> false
                            }
                        }
                ) {
                    state.tabs.forEachIndexed { index, tab ->
                        key(index) {
                            Tab(
                                selected = state.selectedTabIndex == index,
                                onFocus = { interactionListener.onTabChanged(index) },
                                colors = TabDefaults.underlinedIndicatorTabColors(
                                    contentColor = MaterialTheme.colorScheme.secondary,
                                    selectedContentColor = MaterialTheme.colorScheme.secondary,
                                    inactiveContentColor = MaterialTheme.colorScheme.onSurface,
                                )
                            ) {
                                Text(
                                    text = tab,
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                        }
                    }
                }
                TvLazyRow(
                    modifier = Modifier.padding(top = 40.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    contentPadding = PaddingValues(horizontal = 48.dp)
                ) {
                    items(
                        state.weaklyPlans[state.selectedTabIndex].getOrDefault(
                            state.tabs[state.selectedTabIndex],
                            listOf()
                        )
                    ) { workoutItemUiState ->
                        RegimenCardItem(
                            onclick = { interactionListener.onWorkoutClicked(workoutItemUiState.id) },
                            imageUrl = workoutItemUiState.imageUrl,
                            title = workoutItemUiState.title,
                            subtitle = workoutItemUiState.subtitle
                        )
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

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_TELEVISION,
    device = "id:tv_1080p"
)
@Composable
fun ChallengeTabsPreview() {
    val challengeViewModel: ChallengeViewModel = hiltViewModel()
    val state by challengeViewModel.state.collectAsState()
    JetFitTheme {
        ChallengeTabs(state, challengeViewModel)
    }
}

