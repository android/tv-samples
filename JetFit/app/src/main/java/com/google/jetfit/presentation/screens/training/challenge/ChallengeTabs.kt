package com.google.jetfit.presentation.screens.training.challenge

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Tab
import androidx.tv.material3.TabDefaults
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.components.PlanTextWithIcon
import com.google.jetfit.components.ChallengeTabsUnderlinedIndicator
import com.google.jetfit.presentation.screens.training.composables.TrainingCardItem
import com.google.jetfit.presentation.theme.JetFitTheme

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChallengeTabs(
    state: ChallengeUiState,
) {
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
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
                selectedTabIndex = selectedTabIndex,
                separator = { Spacer(modifier = Modifier.width(16.dp)) },
                indicator = { tabPositions, doesTabRowHaveFocus ->
                    ChallengeTabsUnderlinedIndicator(
                        currentTabPosition = tabPositions[selectedTabIndex],
                        doesTabRowHaveFocus = doesTabRowHaveFocus,
                        activeColor = MaterialTheme.colorScheme.secondary,
                    )
                },
                modifier = Modifier
                    .padding(top = 24.dp, start = 48.dp)
                    .focusRestorer(),
            ) {
                state.tabs.forEachIndexed { index, tab ->
                    key(index) {
                        Tab(
                            selected = selectedTabIndex == index,
                            onFocus = { selectedTabIndex = index },
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
                modifier = Modifier.padding(top = 40.dp).focusRestorer(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(horizontal = 48.dp)
            ) {
                items(
                    state.weaklyPlans[selectedTabIndex].getOrDefault(
                        state.tabs[selectedTabIndex],
                        listOf()
                    )
                ) { workoutItemUiState ->
                    TrainingCardItem(
                        onclick = { },
                        imageUrl = workoutItemUiState.imageUrl,
                        title = workoutItemUiState.title,
                        subtitle = workoutItemUiState.subtitle
                    )
                }
            }
        }
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
        ChallengeTabs(state)
    }
}