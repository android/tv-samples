@file:OptIn(ExperimentalTvMaterial3Api::class)

package com.google.jetfit.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonColors
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text


@Composable
fun CustomFillButton(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = TextStyle(),
    onClick: () -> Unit,
    icon: Int? = null,
    buttonColor: ButtonColors = ButtonDefaults.colors(),
    iconPadding: Dp = 4.dp,
    iconTint: Color = Color.White
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        colors = buttonColor,
    ) {
        icon?.let { painterResource(id = it) }
            ?.let {
                Icon(
                    painter = it,
                    contentDescription = "Icon",
                    modifier.padding(end = iconPadding),
                    tint = iconTint
                )
            }
        Text(text = text, style = textStyle)
    }
}


@Preview
@Composable
fun PreviewCustomFillButton() {
    CustomFillButton(text = "Start session", onClick = {})
}