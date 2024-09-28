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
import androidx.tv.material3.OutlinedIconButton
import androidx.tv.material3.OutlinedIconButtonDefaults
import com.google.jetfit.R

@Composable
fun CustomOutlineIconButton(
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint : Color = Color.White,
    border : ButtonBorder = OutlinedIconButtonDefaults.border()
) {
    OutlinedIconButton( onClick = { onClick() },modifier = modifier, border = border
    ){
        Icon(painter = painterResource(id = icon), contentDescription = "icon", tint = iconTint )
    }
}

@Preview
@Composable
fun PreviewCustomOutlineIconButton() {
    CustomOutlineIconButton(icon = R.drawable.music_icon, onClick = { /*TODO*/ })
}