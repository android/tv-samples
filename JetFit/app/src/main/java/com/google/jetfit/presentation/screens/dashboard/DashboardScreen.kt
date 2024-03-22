package com.google.jetfit.presentation.screens.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.jetfit.presentation.screens.Screens
import com.google.jetfit.presentation.screens.favorite.FavoriteScreen
import com.google.jetfit.presentation.screens.training.TrainingScreen
import com.google.jetfit.presentation.screens.home.HomeScreen
import com.google.jetfit.presentation.screens.search.SearchScreen
import com.google.jetfit.presentation.screens.settings.SettingsScreen


@Composable
fun DashboardScreen(
    onBackPressed: () -> Unit,
    navController: NavHostController,
    onNavigateTo: (Screens) -> Unit,
    currentDestination: NavDestination?
) {
    BackPressHandledArea(onBackPressed = onBackPressed) {
        DashboardNavigationDrawer(
                modifier = Modifier,
                onNavigateTo = onNavigateTo,
                currentDestination = currentDestination,
        ) {
            NavHost(
                    navController = navController,
                    startDestination = Screens.Home(),
            ) {
                composable(Screens.Search()) {
                    SearchScreen()
                }
                composable(Screens.Home()) {
                    HomeScreen()
                }
                composable(Screens.Training()) {
                    TrainingScreen()
                }
                composable(Screens.Favorite()) {
                    FavoriteScreen()
                }
                composable(Screens.Settings()) {
                    SettingsScreen()
                }
            }
        }
    }
}

@Composable
private fun BackPressHandledArea(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) =
    Box(
            modifier = Modifier
                .onPreviewKeyEvent {
                    if (it.key == Key.Back && it.type == KeyEventType.KeyUp) {
                        onBackPressed()
                        true
                    } else {
                        false
                    }
                }
                .then(modifier),
            content = content
    )
