package com.google.jetfit.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.jetfit.presentation.screens.Screens
import com.google.jetfit.presentation.screens.dashboard.DashboardScreen

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun NavGraphBuilder.navigationDrawerGraph(onNavigateToRoot: (Screens) -> Unit, onBackPressed: () -> Unit) {
    composable(route = Screens.Dashboard()) {

        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        DashboardScreen(
                onBackPressed = onBackPressed,
                navController = navController,
                currentDestination = navBackStackEntry?.destination,
                onNavigateTo = navController::navigateTo
        )
    }

}