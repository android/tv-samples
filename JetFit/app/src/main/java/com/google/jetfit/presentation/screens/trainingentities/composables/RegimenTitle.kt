package com.google.jetfit.presentation.screens.trainingentities.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RegimenTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
}