package com.google.jetcatalog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationGraph(
    onThemeColorModeClick: () -> Unit,
    onFontScaleClick: () -> Unit
) {
    val navHostController = LocalNavController.current
    NavHost(navController = navHostController, startDestination = NavGraph.Home.routeName) {
        destinations.forEach { destination ->
            composable(destination.routeName) {
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

enum class NavGraph(
    val routeName: String,
    val composable: @Composable (appBar: @Composable () -> Unit) -> Unit
) {
    Home(
        routeName = "home",
        composable = { appBar ->
            Column {
                appBar()
                HomeGrid()
            }
        }
    ),

    // foundations
    Color(
        routeName = "color",
        composable = { appBar ->
            Column {
                appBar()
                ColorsScreen()
            }
        }
    ),
    Typography(
        routeName = "typography",
        composable = { appBar ->
            Column {
                appBar()
                TypographyScreen()
            }
        }
    ),
    Motion(
        routeName = "motion",
        composable = { appBar ->
            Column {
                appBar()
                WorkInProgressScreen()
            }
        }
    ),
    Interaction(
        routeName = "interaction",
        composable = { appBar ->
            Column {
                appBar()
                InteractionsScreen()
            }
        }
    ),

    // components
    Buttons(
        routeName = "buttons",
        composable = { appBar ->
            Column {
                appBar()
                ButtonsScreen()
            }
        }
    ),
    Cards(
        routeName = "cards",
        composable = { appBar ->
            Column {
                appBar()
                CardsScreen()
            }
        }
    ),
    Chips(
        routeName = "chips",
        composable = { appBar ->
            Column {
                appBar()
                ChipsScreen()
            }
        }
    ),
    Lists(
        routeName = "lists",
        composable = { appBar ->
            Column {
                appBar()
                ListsScreen()
            }
        }
    ),
    ImmersiveCluster(
        routeName = "immersive-cluster",
        composable = { appBar ->
            Box {
                ImmersiveListScreen()
                appBar()
            }
        }
    ),
    FeaturedCarousel(
        routeName = "featured-carousel",
        composable = { appBar ->
            Box {
                FeaturedCarouselScreen()
                appBar()
            }
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
    ModalDialog(
        routeName = "modal-dialog",
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

    // foundation
    NavGraph.Color,
    NavGraph.Typography,
    NavGraph.Motion,
    NavGraph.Interaction,

    // components
    NavGraph.Buttons,
    NavGraph.Cards,
    NavGraph.Chips,
    NavGraph.Lists,
    NavGraph.ImmersiveCluster,
    NavGraph.FeaturedCarousel,
    NavGraph.NavigationDrawer,
    NavGraph.TabRow,
    NavGraph.ModalDialog,
    NavGraph.TextFields,
    NavGraph.VideoPlayer,
)

val LocalNavController = compositionLocalOf<NavHostController> {
    throw Error("This should not be reached")
}
