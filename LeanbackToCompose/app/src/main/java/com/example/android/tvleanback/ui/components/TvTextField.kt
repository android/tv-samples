package com.example.android.tvleanback.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TvTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    focusRequester: FocusRequester = remember { FocusRequester() },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    onImeAction: (() -> Unit)? = null,
    onUpPress: (() -> Boolean)? = null,
    onDownPress: (() -> Boolean)? = null
) {
    val focusManager = LocalFocusManager.current

    Surface(
        onClick = { focusRequester.requestFocus() },
        modifier = modifier,
        shape = ClickableSurfaceDefaults.shape(shape = MaterialTheme.shapes.small),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
            pressedContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.01f),
        border = ClickableSurfaceDefaults.border(
            border = androidx.tv.material3.Border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.border),
                shape = MaterialTheme.shapes.small
            ),
            focusedBorder = androidx.tv.material3.Border(
                border = BorderStroke(2.dp, Color.White),
                shape = MaterialTheme.shapes.small
            )
        )
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = visualTransformation,
            placeholder = if (placeholder != null) {
                {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            } else null,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onPreviewKeyEvent { event ->
                    when (event.key) {
                        Key.Enter, Key.NumPadEnter, Key.DirectionCenter -> {
                            if (event.type == KeyEventType.KeyUp) {
                                if (onImeAction != null) {
                                    onImeAction()
                                } else {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            }
                            true // Consume both KeyDown and KeyUp to handle Enter/Select on TV remotes
                        }
                        Key.DirectionDown -> {
                            if (event.type == KeyEventType.KeyDown) {
                                val handled = onDownPress?.invoke() ?: false
                                if (!handled) {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            }
                            true
                        }
                        Key.DirectionUp -> {
                            if (event.type == KeyEventType.KeyDown) {
                                val handled = onUpPress?.invoke() ?: false
                                if (!handled) {
                                    focusManager.moveFocus(FocusDirection.Up)
                                }
                            }
                            true
                        }
                        Key.Back -> {
                            if (event.type == KeyEventType.KeyDown) {
                                focusManager.moveFocus(FocusDirection.Exit)
                            }
                            true
                        }
                        else -> false
                    }
                },
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onAny = {
                    if (onImeAction != null) {
                        onImeAction()
                    } else {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                }
            ),
            maxLines = 1,
            textStyle = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}
