package com.google.jetfit.presentation.screens.training

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import com.google.jetfit.components.CustomOutlineButton
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
            onClickSortBy = viewModel::onSortedClicked
    )

}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun TrainingContent(
    state: TrainingUiState,
    onClickFilter: () -> Unit,
    onClickSortBy: () -> Unit,
    onDismissSideMenu: () -> Unit,
    onSelectedItem: (currentIndex: Int) -> Unit
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
    Column(
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomOutlineButton(text = "Filters", onClick = onClickFilter)
        CustomOutlineButton(
                text = "Sort by: ${TrainingUiState.SortItem.entries[state.selectedSortItem]}",
                onClick = onClickSortBy
        )
    }
}
