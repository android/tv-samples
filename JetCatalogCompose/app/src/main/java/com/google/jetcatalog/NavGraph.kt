package com.google.jetcatalog

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationGraph(
    onThemeColorModeClick: () -> Unit,
    onFontScaleClick: () -> Unit
) {
    val navHostController = LocalNavController.current
    NavHost(navController = navHostController, startDestination = NavGraph.Home.routeName) {
        destinations.forEach { destination ->
            composable(destination.routeName) {
                Column {
                    destination.composable {
                        AppBar(
                            onThemeColorModeClick = onThemeColorModeClick,
                            onFontScaleClick = onFontScaleClick
                        )
                    }
                }
            }
        }
    }
}

enum class NavGraph(
    val routeName: String,
    val composable: @Composable (appBar: @Composable () -> Unit) -> Unit
) {
    Home(
        routeName = "home",
        composable = { appBar ->
            appBar()
            ComponentsGrid()
        }
    ),

    Buttons(
        routeName = "buttons",
        composable = { appBar ->
            appBar()
            ButtonsScreen()
        }
    ),
    Cards(
        routeName = "cards",
        composable = { appBar ->
            appBar()
            CardsScreen()
        }
    ),
    Chips(
        routeName = "chips",
        composable = { appBar ->
            appBar()
            WorkInProgressScreen()
        }
    ),
    Lists(
        routeName = "lists",
        composable = { appBar ->
            appBar()
            WorkInProgressScreen()
        }
    ),
    ImmersiveCluster(
        routeName = "immersive-cluster",
        composable = {
            WorkInProgressScreen()
        }
    ),
    FeaturedCarousel(
        routeName = "featured-carousel",
        composable = {
            WorkInProgressScreen()
        }
    ),
    NavigationDrawer(
        routeName = "nav-drawer",
        composable = {
            WorkInProgressScreen()
        }
    ),
    TabRow(
        routeName = "tab-row",
        composable = {
            WorkInProgressScreen()
        }
    ),
    ModalDrawer(
        routeName = "modal-drawer",
        composable = {
            WorkInProgressScreen()
        }
    ),
    TextFields(
        routeName = "text-fields",
        composable = {
            WorkInProgressScreen()
        }
    ),
    VideoPlayer(
        routeName = "video-player",
        composable = {
            WorkInProgressScreen()
        }
    ),
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
