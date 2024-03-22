package com.google.jetfit.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.jetfit.presentation.screens.Screens
import com.google.jetfit.presentation.screens.player.audio.AudioPlayerScreen
import com.google.jetfit.presentation.screens.player.video.VideoPlayerScreen
import com.google.jetfit.presentation.utils.navigateTo
import com.google.jetfit.presentation.utils.navigationDrawerGraph

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun App(
    onBackPressed: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        route = "root_host",
        startDestination = Screens.Dashboard(),
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
        }
    )
}