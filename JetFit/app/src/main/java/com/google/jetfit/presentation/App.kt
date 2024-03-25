package com.google.jetfit.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.jetfit.presentation.screens.Screens
import com.google.jetfit.presentation.screens.favorites.FavoritesScreen
import com.google.jetfit.presentation.screens.player.audio.AudioPlayerScreen
import com.google.jetfit.presentation.screens.player.video.VideoPlayerScreen
import com.google.jetfit.presentation.screens.profileSelector.ProfileSelectorScreen
import com.google.jetfit.presentation.utils.navigateTo
import com.google.jetfit.presentation.utils.navigationDrawerGraph

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun App(
    navController: NavHostController,
    onBackPressed: () -> Unit
) {
    NavHost(
        navController = navController,
        route = "root_host",
        startDestination = Screens.ProfileSelector(),
        builder = {
            navigationDrawerGraph(
                    onNavigateToRoot = navController::navigateTo,
                    onBackPressed = onBackPressed
            )
            composable(
                route = Screens.VideoPlayer(),
                arguments = listOf(
                    navArgument("") {
                        type = NavType.StringType
                    }
                )
            ) {
                VideoPlayerScreen(onBackPressed = onBackPressed)
            }
            composable(
                route = Screens.AudioPlayer(),
                arguments = listOf(
                    navArgument("") {
                        type = NavType.StringType
                    }
                )
            ) {
                AudioPlayerScreen(onBackPressed = onBackPressed)
            }
            composable(
                route = Screens.ProfileSelector()
            ) {
                ProfileSelectorScreen(onBackPressed = onBackPressed)
            }
            composable(
                route = Screens.Favorite()
            ){
                FavoritesScreen(onBackPressed = onBackPressed)
            }
        }
    )
}