package com.example.android.tvleanback.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.android.tvleanback.R
import com.example.android.tvleanback.ui.components.ErrorState
import com.example.android.tvleanback.ui.components.LoadingIndicator
import com.example.android.tvleanback.ui.theme.TvLeanbackTheme
import kotlinx.coroutines.delay

@Composable
fun ErrorScreen(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1000)
        isLoading = false
    }

    TvLeanbackTheme {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xCC101413)) // Translucent dark background
        ) {
            if (isLoading) {
                LoadingIndicator()
            } else {
                val dismissFocusRequester = remember { FocusRequester() }
                LaunchedEffect(Unit) {
                    dismissFocusRequester.requestFocus()
                }
                ErrorState(
                    message = stringResource(id = R.string.error_fragment_message),
                    onRetry = onDismiss,
                    buttonModifier = Modifier.focusRequester(dismissFocusRequester)
                )
            }
        }
    }
}
