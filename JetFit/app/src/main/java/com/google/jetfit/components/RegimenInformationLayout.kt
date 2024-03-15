package com.google.jetfit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RegimenInformationLayout(
    modifier: Modifier = Modifier,
    cinematicBackground: @Composable (BoxScope) -> Unit,
    regimenInformation: @Composable (ColumnScope) -> Unit,
    topPadding: Dp = 196.dp,
    regimenMetrics: @Composable (ColumnScope) -> Unit,
    regimenActions: @Composable (RowScope) -> Unit,
    extra: @Composable (ColumnScope) -> Unit = {}
) {
    Box(modifier = modifier) {
        cinematicBackground(this)
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(top = topPadding, start = 48.dp)
                    .width(484.dp),
            ) {
                regimenInformation(this)
                regimenMetrics(this)
                Row(
                    modifier = Modifier.padding(top = 28.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    regimenActions(this)
                }
                extra(this)
            }
        }
    }
}