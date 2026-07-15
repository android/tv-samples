package com.example.android.tvleanback.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun StudioBadge(
    studio: String,
    modifier: Modifier = Modifier
) {
    if (studio.isNotEmpty()) {
        Box(
            modifier = modifier
                .background(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = studio.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
        }
    }
}
