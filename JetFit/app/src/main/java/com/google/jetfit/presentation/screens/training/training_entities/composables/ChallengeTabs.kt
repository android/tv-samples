package com.google.jetfit.presentation.screens.training.training_entities.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.itemsIndexed
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Tab
import androidx.tv.material3.TabDefaults
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.components.CustomCard
import com.google.jetfit.presentation.screens.training.training_entities.TrainingEntityUiState

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChallengeTabs(
    state: TrainingEntityUiState,
    onClickBackChallenge: () -> Unit,
    onClickCard: (Int) -> Unit,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    Box(modifier = Modifier.fillMaxSize()) {
        RoundedGradientImage(
            modifier = Modifier.alpha(.5f),
            imageUrl = state.imageUrl
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
        ) {
            ChallengesPlanButton(
                modifier = Modifier.padding(start = 48.dp),
                subtitle = stringResource(R.string.challenge_details),
                iconId = R.drawable.up_arrow_head_icon,
                onClick = onClickBackChallenge
            )
            Text(
                modifier = Modifier.padding(top = 52.dp, start = 48.dp),
                text = stringResource(R.string.weekly_plan),
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            TabRow(
                modifier = Modifier
                    .padding(top = 24.dp, start = 48.dp)
                    .focusRestorer(),
                selectedTabIndex = selectedTabIndex,
                separator = { Spacer(modifier = Modifier.width(16.dp)) },
                indicator = { tabPositions, doesTabRowHaveFocus ->
                    ChallengeTabRowIndicator(
                        currentTabPosition = tabPositions[selectedTabIndex],
                        doesTabRowHaveFocus = doesTabRowHaveFocus,
                        activeColor = MaterialTheme.colorScheme.secondary,
                    )
                }
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
                modifier = Modifier
                    .padding(top = 40.dp)
                    .focusRestorer(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(horizontal = 48.dp)
            ) {
                itemsIndexed(
                    state.weaklyPlans[selectedTabIndex].getOrDefault(
                        state.tabs[selectedTabIndex],
                        listOf()
                    )
                ) { index, item ->
                    CustomCard(
                        modifier = Modifier.width(196.dp),
                        imageUrl = item.imageUrl,
                        title = item.title,
                        timeText = item.time,
                        typeText = item.typeText,
                        onClick = { onClickCard(index) }
                    )
                }
            }
        }
    }
}