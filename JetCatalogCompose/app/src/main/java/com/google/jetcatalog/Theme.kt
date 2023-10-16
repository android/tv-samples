package com.google.jetcatalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

enum class Mode(val value: String) {
    Light("light"),
    Dark("dark")
}

data class ThemeMode(
    val mode: Mode,
    val title: String,
    val icon: Int
)

val themeModes = listOf(
    ThemeMode(mode = Mode.Dark, title = "Dark", icon = R.drawable.ic_dark_theme),
    ThemeMode(mode = Mode.Light, title = "Light", icon = R.drawable.ic_light_theme),
)

val LocalMode = compositionLocalOf { Mode.Dark }
val LocalThemeSeedColor = compositionLocalOf { PurpleSeedColor }

data class SeedColor(val name: String, val color: Color)

val PurpleSeedColor = SeedColor(name = "Default", color = Color(0xFF5E44D3))
val GreenSeedColor = SeedColor(name = "Shamrock", color = Color(0xFF386A20))
val BlueSeedColor = SeedColor(name = "Aegean", color = Color(0xFF004A77))
val RedSeedColor = SeedColor(name = "Rosewood", color = Color(0xFF9C4146))
val YellowSeedColor = SeedColor(name = "Dijan", color = Color(0xFF616200))

val seedColors = listOf(
    PurpleSeedColor,
    GreenSeedColor,
    BlueSeedColor,
    RedSeedColor,
    YellowSeedColor
)

@Composable
fun getComponentGridCardImage(
    component: Component,
    mode: Mode = LocalMode.current,
    seedColor: Color = LocalThemeSeedColor.current.color,
): Int {
    val colorIdentifier = seedColor.value.toString(16).slice(2 until 8)
    return LocalContext.current.resources.getIdentifier(
        "${component.imageArg}_${colorIdentifier}_${mode.value}",
        "drawable",
        "com.google.jetcatalog"
    )
}

data class FontScale(
    val scale: Float,
    val title: String,
)

val fontScales = listOf(
    FontScale(scale = 0.9f, title = "0.9x"),
    FontScale(scale = 1.0f, title = "1.0x"),
    FontScale(scale = 1.1f, title = "1.1x"),
    FontScale(scale = 1.2f, title = "1.2x"),
    FontScale(scale = 1.5f, title = "1.5x"),
)
