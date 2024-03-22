package com.google.jetfit.presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ModalNavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.NavigationDrawerItemColors
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.presentation.screens.Screens

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DashboardNavigationDrawer(
    modifier: Modifier = Modifier,
    currentDestination: androidx.navigation.NavDestination?,
    onNavigateTo: (Screens) -> Unit,
    content: @Composable ()-> Unit,
) {
    val screens = Screens.entries.filter { it.isNavigationDrawerItem }
    val closeDrawerWidth = 80.dp
    val backgroundContentPadding = 12.dp
    ModalNavigationDrawer(
        scrimBrush = Brush.horizontalGradient(listOf(
                        MaterialTheme.colorScheme.background,
                        Color.Transparent
                )),
        drawerContent = {
            Column(
                    Modifier
                        .fillMaxHeight()
                        .padding(12.dp)
                        .selectableGroup(),
                    verticalArrangement = Arrangement.Center
            ) {
                screens.forEachIndexed { index, screen ->
                    val selected: Boolean = currentDestination?.hierarchy?.any { it.route == screen.name } ?: false

                    NavigationDrawerItem(
                            colors = NavigationDrawerItemColors(
                                containerColor = Color.Transparent,
                                contentColor=MaterialTheme.colorScheme.onSurfaceVariant,
                                inactiveContentColor= MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedContainerColor=MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedContentColor=MaterialTheme.colorScheme.inverseOnSurface,
                                selectedContainerColor=MaterialTheme.colorScheme.secondaryContainer,
                                selectedContentColor=MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledContainerColor=MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledContentColor=MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledInactiveContentColor=MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedSelectedContainerColor=MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedSelectedContentColor=MaterialTheme.colorScheme.inverseOnSurface,
                                pressedContentColor=MaterialTheme.colorScheme.onSurfaceVariant,
                                pressedContainerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                pressedSelectedContainerColor=Color.Red,
                                pressedSelectedContentColor=MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                        modifier = Modifier.padding(bottom = 12.dp),
                        selected = selected,
                        onClick = { onNavigateTo(screen) },
                        content = { Text(screens[index].name) },
                        leadingContent = {
                            Icon(
                                painter = painterResource(id = screen.navigationDrawerIcon ?: R.drawable.home),
                                contentDescription = screen.name,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    )
                }
            }
        },
        modifier = Modifier
    ) {
        Box(modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(start = closeDrawerWidth + backgroundContentPadding),
            content = {content()}
        )
    }
}
