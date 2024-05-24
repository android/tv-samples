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
import com.google.jetfit.presentation.screens.more_options.MoreOptionsScreen
import com.google.jetfit.presentation.screens.favorites.FavoritesScreen
import com.google.jetfit.presentation.screens.home.HomeScreen
import com.google.jetfit.presentation.screens.player.audio.AudioPlayerScreen
import com.google.jetfit.presentation.screens.player.video.VideoPlayerScreen
import com.google.jetfit.presentation.screens.profileSelector.ProfileSelectorScreen
import com.google.jetfit.presentation.screens.subscription.SubscriptionScreen
import com.google.jetfit.presentation.screens.training.TrainingScreen
import com.google.jetfit.presentation.screens.training.training_entities.TrainingEntityScreen
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
            ) {
                VideoPlayerScreen()
            }
            composable(
                route = Screens.AudioPlayer(),
            ) {
                AudioPlayerScreen()
            }
            composable(
                route = Screens.ProfileSelector()
            ) {
                ProfileSelectorScreen(onBackPressed = onBackPressed)
            }

            composable(
                route = Screens.MoreOptions(),
                arguments = listOf(
                    navArgument(""){
                        type = NavType.StringType
                    }
                )
            ){
                MoreOptionsScreen(
                    onBackPressed = onBackPressed,
                    onStartClick = { navController.navigate(Screens.AudioPlayer()) },
                    onFavouriteClick = { navController.navigate(Screens.Favorite()) }
                    )
            }
            composable(
                route = Screens.Favorite()
            ){
                FavoritesScreen(onBackPressed = onBackPressed,
                    onStartWorkout = { navController.navigate(Screens.VideoPlayer()) })
            }
            composable(
                route = Screens.Home(),
                arguments = listOf(
                    navArgument("") {
                        type = NavType.StringType
                    }
                )
            ) {
                HomeScreen(
                    onStartSessionCLick = {
                        navController.navigate(Screens.TrainingEntity())
                    },
                    onCardClick = {
                        navController.navigate(Screens.MoreOptions())
                    }
                )
            }
            composable(
                route = Screens.Training(),
                arguments = listOf(
                    navArgument("") {
                        type = NavType.StringType
                    }
                )
            ) {
                TrainingScreen()
            }

            composable(
                route = Screens.Subscription(),
                arguments = listOf(
                    navArgument("") {
                        type = NavType.StringType
                    }
                )
            ) {
                SubscriptionScreen(onBackPressed = onBackPressed)
            }
            composable(
                route = Screens.TrainingEntity(),
                arguments = listOf(
                    navArgument("") {
                        type = NavType.StringType
                    }
                )
            ) {
                TrainingEntityScreen(
                    onClickStart = {
                        navController.navigate(Screens.VideoPlayer())
                    }
                )
            }
        }
    )
}