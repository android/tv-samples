package com.google.jetfit.presentation.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.presentation.theme.surface
import com.google.jetfit.presentation.theme.surfaceContainerHigh

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state = viewModel.settingsUIState.collectAsState()

    val selectedItem = remember { mutableStateOf<SettingsItemUIState?>(null) }

    SettingsContent(
        state = state.value,
        selectedItem = selectedItem,
        updateSetting = viewModel::updateSetting
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SettingsContent(
    state: SettingsUIState,
    selectedItem: MutableState<SettingsItemUIState?>,
    updateSetting: (SettingsItemUIState) -> Unit = {}
) {
    Row(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = stringResource(id = R.string.settings),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 64.dp, start = 32.dp)
            )

            TvLazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = stringResource(id = state.appSettings.first),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                items(state.appSettings.second) {
                    SettingsItem(item = it, selectedItem = selectedItem, updateSetting = updateSetting)
                }

                item {
                    Text(
                        text = stringResource(id = state.personalSettings.first),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                items(state.personalSettings.second) {
                    SettingsItem(item = it, selectedItem = selectedItem, updateSetting = updateSetting)
                }
            }
        }

        AnimatedVisibility(
            visible = selectedItem.value != null,
            enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)),
            exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)),
            modifier = Modifier.weight(1f)
        ) {
            selectedItem.value?.let {
                SettingsDetail(item = selectedItem.value!!, updateSetting = updateSetting)
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SettingsItem(
    item: SettingsItemUIState,
    selectedItem: MutableState<SettingsItemUIState?>,
    updateSetting: (SettingsItemUIState) -> Unit
) {
    ListItem(
        selected = false,
        onClick = {
            selectedItem.value = item
            updateSetting(item)
                  },
        leadingContent = {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        trailingContent = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.value,
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    modifier = Modifier.size(ListItemDefaults.IconSize),
                    contentDescription = "back icon"
                )
            }
        },
        headlineContent = {},
        colors = ListItemDefaults.colors(
            containerColor = surfaceContainerHigh.copy(alpha = 0.3f),
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedContentColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = ListItemDefaults.shape(shape = RoundedCornerShape(8.dp)),
    )
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SubSettingItem(
    item: SettingsItemUIState,
    selectedItem: MutableState<SettingsItemUIState?>,
    updateSetting: (SettingsItemUIState) -> Unit
) {
    item.possibleValues.forEach { subSetting ->
        ListItem(
            onClick = {
                val updatedItem = item.copy(value = subSetting)
                updateSetting(updatedItem)
                selectedItem.value = updatedItem
            },
            selected = selectedItem.value?.value == subSetting,
            leadingContent = {
                Text(
                    text = subSetting,
                    style = MaterialTheme.typography.titleMedium,
                )
            },
            trailingContent = {
                if (selectedItem.value?.value == subSetting) {
                    Icon(
                        Icons.Default.CheckCircle,
                        modifier = Modifier.size(ListItemDefaults.IconSize),
                        contentDescription = "selected icon"
                    )
                }
            },
            headlineContent = {},
            colors = ListItemDefaults.colors(
                containerColor = surface,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedContentColor = MaterialTheme.colorScheme.surfaceVariant,
                selectedContainerColor = surface
            ),
            modifier = Modifier.padding(4.dp)
        )
    }
}
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SettingsDetail(
    item: SettingsItemUIState,
    updateSetting: (SettingsItemUIState) -> Unit
) {
    Column(
        Modifier
            .background(Color(0xFF0B0F0E))
            .fillMaxHeight(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 64.dp, start = 32.dp)
        )
        TvLazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listOf(item)) {
                SubSettingItem(
                    updateSetting = updateSetting,
                    item = item,
                    selectedItem = remember { mutableStateOf(item) })
            }
        }
    }
}