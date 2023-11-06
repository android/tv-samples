package com.google.jetcatalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.lightColorScheme
import com.google.jetcatalog.colorutils.Scheme.Companion.dark
import com.google.jetcatalog.colorutils.Scheme.Companion.light
import com.google.jetcatalog.colorutils.toColorScheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun App() {
    var themeMode by remember { mutableStateOf(Mode.Dark) }
    var seedColor by remember { mutableStateOf(PurpleSeedColor) }
    var layoutDirection by remember { mutableStateOf(LayoutDirection.Ltr) }
    var fontScale by remember { mutableFloatStateOf(1.0f) }

    var isThemeSelectorExpanded by remember { mutableStateOf(false) }
    var isFontScaleSelectorExpanded by remember { mutableStateOf(false) }

    val argbColor = seedColor.color.toArgb()
    val colorScheme = if (themeMode == Mode.Dark) dark(argbColor) else light(argbColor)

    AppProviders(
        seedColor = seedColor,
        themeMode = themeMode,
        layoutDirection = layoutDirection,
        fontScale = fontScale,
    ) {
        MaterialTheme(colorScheme = colorScheme.toColorScheme()) {
            ThemeAndColorModeSelector(
                isExpanded = isThemeSelectorExpanded,
                onClose = { isThemeSelectorExpanded = false },
                onSeedColorChange = { seedColor = it },
                onThemeModeChange = { themeMode = it },
            ) {
                FontScaleAndLayoutDirectionSelector(
                    isExpanded = isFontScaleSelectorExpanded,
                    onClose = { isFontScaleSelectorExpanded = false },
                    onLayoutDirectionChange = { layoutDirection = it },
                    onFontScaleChange = { fontScale = it }
                ) {
                    Surface(
                        Modifier.fillMaxSize(),
                        shape = RectangleShape
                    ) {
                        Column(Modifier.fillMaxSize()) {
                            NavigationGraph(
                                onThemeColorModeClick = {
                                    isThemeSelectorExpanded = true
                                },
                                onFontScaleClick = {
                                    isFontScaleSelectorExpanded = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
