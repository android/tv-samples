package com.google.jetfit.presentation.screens.training.training_entities.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.ContentScale
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import coil.compose.AsyncImage

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RoundedGradientImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    val radiantColors = listOf(
        MaterialTheme.colorScheme.background.copy(alpha = 0f),
        MaterialTheme.colorScheme.background
    )
    AsyncImage(
        modifier = modifier
            .fillMaxSize()
            .drawWithContent {
                drawContent()
                scale(
                    scaleY = 1f,
                    scaleX = size.height / size.height,
                    pivot = Offset((size.width), 0f)
                ) {
                    drawRect(
                        Brush.radialGradient(
                            radiantColors,
                            center = Offset((size.width - 150), 0f),
                            radius = (size.height),
                        )
                    )
                }
            },
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}