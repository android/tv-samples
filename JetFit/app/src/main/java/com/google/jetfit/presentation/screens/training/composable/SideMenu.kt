package com.google.jetfit.presentation.screens.training.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.jetfit.presentation.theme.surfaceContainerHigh

@Composable
fun SideMenu(
    isSideMenuExpended: Boolean = false,
    onDismissSideMenu: () -> Unit,
    content: @Composable () -> Unit
){
    AnimatedVisibility(
            visible = isSideMenuExpended,
            modifier = Modifier.fillMaxHeight(),
            enter = slideInHorizontally(
                    initialOffsetX = { it / 2 },
                    animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                    )
            ),
            exit = slideOutHorizontally(
                    targetOffsetX = { 400 },
                    animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                    )
            )
    ) {
        Dialog(
                onDismissRequest = onDismissSideMenu,
                properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                        usePlatformDefaultWidth = false
                )
        ) {
            Box(contentAlignment = Alignment.TopEnd, modifier = Modifier.fillMaxSize()
                .onPreviewKeyEvent {
                    if (it.key == Key.DirectionLeft && it.type == KeyEventType.KeyUp) {
                        onDismissSideMenu()
                        true
                    } else {
                        false
                    }
                }
            ) {

                Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.4f)
                    .background(surfaceContainerHigh),
                ){
                    content()
                }
            }
        }
    }
}
