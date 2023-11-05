package com.google.jetcatalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.rememberNavController

@Composable
fun AppProviders(
    seedColor: SeedColor,
    themeMode: Mode,
    layoutDirection: LayoutDirection,
    fontScale: Float,
    content: @Composable () -> Unit
) {
    val navHostController = rememberNavController()
    CompositionLocalProvider(
        LocalThemeSeedColor provides seedColor,
        LocalMode provides themeMode,
        LocalLayoutDirection provides layoutDirection,
        LocalDensity provides Density(
            density = LocalDensity.current.density,
            fontScale = fontScale,
        ),
        LocalNavController provides navHostController
    ) {
        content()
    }
}
