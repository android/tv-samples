package com.google.jetfit.presentation.screens.training

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyHorizontalGrid
import androidx.tv.foundation.lazy.grid.items
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import com.google.jetfit.components.CustomCard
import com.google.jetfit.components.CustomOutlineButton
import com.google.jetfit.presentation.screens.training.composable.CustomTabRow
import com.google.jetfit.presentation.screens.training.composable.FilterSideMenu
import com.google.jetfit.presentation.screens.training.composable.SideMenu
import com.google.jetfit.presentation.screens.training.composable.SortSideMenu

@Composable
fun TrainingScreen(
    viewModel: TrainingViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    TrainingContent(
            state = state,
            onClickFilter = viewModel::onFilterClicked,
            onDismissSideMenu = viewModel::onDismissSideMenu,
            onSelectedItem = viewModel::onSelectedSortedItem,
        onClickSortBy = viewModel::onSortedClicked,
        onChangeSelectedTab = viewModel::onChangeSelectedTab,
        onChangeFocusTab = viewModel::onChangeFocusTab,
    )

}
data class SectionTab(
    val id: Int,
    val title: String,
)

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun TrainingContent(
    state: TrainingUiState,
    onClickFilter: () -> Unit,
    onClickSortBy: () -> Unit,
    onDismissSideMenu: () -> Unit,
    onSelectedItem: (currentIndex: Int) -> Unit,
    onChangeSelectedTab: (index: Int) -> Unit,
    onChangeFocusTab: (index: Int) -> Unit,
) {
    SideMenu(onDismissSideMenu = onDismissSideMenu, isSideMenuExpended = state.isFilterExpended) {
        FilterSideMenu(
                onDismissSideMenu = onDismissSideMenu,
                filtrationFields = state.filterItems
        )
    }
    SideMenu(onDismissSideMenu = onDismissSideMenu, isSideMenuExpended = state.isSortExpended) {
        SortSideMenu(
                onDismissSideMenu = onDismissSideMenu,
                selectedIndex = state.selectedSortItem,
                onSelectedItem = onSelectedItem
        )
    }
    val tabs = listOf("Workout", "Series", "Challenges", "Routines")
    TvLazyColumn(
        Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                modifier = Modifier.padding(top = 50.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {


                Spacer(modifier = Modifier.width(20.dp))
                CustomTabRow(
                    tabs = tabs,
                    selectedTabIndex = state.selectedTab,
                    focusTabIndex = state.focusTabIndex,
                    onClick = onChangeSelectedTab,
                    onFocus = onChangeFocusTab,
                )

                Spacer(modifier = Modifier.weight(1F))
                CustomOutlineButton(text = "Filters", onClick = onClickFilter)
                Spacer(modifier = Modifier.width(14.dp))
                CustomOutlineButton(
                    text = "Sort by: ${TrainingUiState.SortItem.entries[state.selectedSortItem]}",
                    onClick = onClickSortBy
                )
                Spacer(modifier = Modifier.width(58.dp))

            }

        }

        item {
            TvLazyHorizontalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((544.75).dp),
                rows = TvGridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(32.dp)
            ) {
                items(state.workouts) { training ->
                    CustomCard(
                        imageUrl = training.imageUrl,
                        title = training.name,
                        timeText = training.duration,
                        typeText = training.intensity.level,
                        onClick = { },
                        titleTextStyle = MaterialTheme.typography.titleMedium,
                        timeTextStyle = MaterialTheme.typography.labelMedium,
                        typeTextStyle = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
    }
}
