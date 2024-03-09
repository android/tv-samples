package com.google.jetfit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun CustomChips(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    backgroundSelected: Color = MaterialTheme.colorScheme.onBackground,
    backgroundUnselected: Color = MaterialTheme.colorScheme.background,
    textColorSelected: Color = MaterialTheme.colorScheme.background,
    textColorUnselected: Color = MaterialTheme.colorScheme.onBackground,
    radius: Dp = 16.dp,
    textPadding: PaddingValues = PaddingValues(8.dp),
    text: String,
    onClick: () -> Unit = {}
) {

    Box(
        modifier = modifier.background(
            color = if (isSelected) backgroundSelected else backgroundUnselected,
            shape = RoundedCornerShape(radius)
        ).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = if (isSelected) textColorSelected else textColorUnselected,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(textPadding)
        )
    }
}

@Preview
@Composable
fun PreviewCustomChips() {
    Row {
        CustomChips(
            modifier = Modifier.padding(8.dp),
            isSelected = true,
            text = "Workout",
            textPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        )
        CustomChips(
            modifier = Modifier.padding(8.dp),
            isSelected = false,
            text = "Series",
            textPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}