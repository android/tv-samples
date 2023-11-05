package com.google.jetcatalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationGraph() {
    val navHostController = LocalNavController.current
    NavHost(navController = navHostController, startDestination = NavGraph.Home.routeName) {
        destinations.forEach { destination ->
            composable(destination.routeName) {
                destination.composable()
            }
        }
    }
}

enum class NavGraph(val routeName: String, val composable: @Composable () -> Unit) {
    Home("home", { ComponentsGrid() }),

    Buttons("buttons", { ButtonsScreen() }),
    Cards("cards", { WorkInProgressScreen() }),
    Chips("chips", { WorkInProgressScreen() }),
    Lists("lists", { WorkInProgressScreen() }),
    ImmersiveCluster("immersive-cluster", { WorkInProgressScreen() }),
    FeaturedCarousel("featured-carousel", { WorkInProgressScreen() }),
    NavigationDrawer("nav-drawer", { WorkInProgressScreen() }),
    TabRow("tab-row", { WorkInProgressScreen() }),
    ModalDrawer("modal-drawer", { WorkInProgressScreen() }),
    TextFields("text-fields", { WorkInProgressScreen() }),
    VideoPlayer("video-player", { WorkInProgressScreen() }),
}

val destinations = listOf(
    NavGraph.Home,
    NavGraph.Buttons,
    NavGraph.Cards,
    NavGraph.Chips,
    NavGraph.Lists,
    NavGraph.ImmersiveCluster,
    NavGraph.FeaturedCarousel,
    NavGraph.NavigationDrawer,
    NavGraph.TabRow,
    NavGraph.ModalDrawer,
    NavGraph.TextFields,
    NavGraph.VideoPlayer,
)

val LocalNavController = compositionLocalOf<NavHostController> {
    throw Error("This should not be reached")
}
