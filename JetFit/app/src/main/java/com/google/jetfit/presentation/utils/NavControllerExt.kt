package com.google.jetfit.presentation.utils

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.google.jetfit.presentation.screens.Screens


fun NavController.navigateTo(screen: Screens) {

    val currentRoute: String? = this.currentBackStackEntry?.destination?.route

    val route = screen.routePath?.let { routePath ->
        screen.name.replaceAfter("/", routePath)
    } ?: screen.name

    Log.d("navigation", "navigateTo: ${screen.name}")

    navigate(route) {

        Log.d("navigation", "findStartDestination: ${graph.findStartDestination()}")

        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }

        launchSingleTop = true
        restoreState = true

        if (screen.clearBackStack && !currentRoute.isNullOrEmpty()) {
            popUpTo(currentRoute) {
                inclusive = false
            }
        }
    }
}
