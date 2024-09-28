@file:OptIn(ExperimentalTvMaterial3Api::class)

package com.google.jetfit.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.ButtonBorder
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.IconButtonDefaults
import com.google.jetfit.R

@Composable
fun CustomFillIconButton(
    modifier: Modifier = Modifier,
    icon: Int,
    onClick: () -> Unit,
    border : ButtonBorder = IconButtonDefaults.border(),
    buttonColor: Color = Color.White
) {
    IconButton(onClick = { onClick() }, modifier = modifier, border = border, colors = IconButtonDefaults.colors(
        buttonColor
    )) {
        Icon(painter = painterResource(id = icon), contentDescription = "icon",)
    }
}

@Preview
@Composable
fun PreviewCustomFillIconButton() {
    CustomFillIconButton(icon = R.drawable.fav_icon, onClick = { /*TODO*/ })
}