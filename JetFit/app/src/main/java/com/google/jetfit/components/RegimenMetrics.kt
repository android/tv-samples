package com.google.jetfit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RegimenMetrics(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    metricItems: @Composable (RowScope) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        metricItems(this)
    }
}