package com.google.jetfit.presentation.screens.training.composable

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.CardLayoutDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.StandardCardLayout
import androidx.tv.material3.Text
import coil.compose.AsyncImage

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TrainingCardItem(
    onclick: () -> Unit,
    imageUrl: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    StandardCardLayout(
        imageCard = { interactionSource ->
            CardLayoutDefaults.ImageCard(
                onClick = { onclick() }, interactionSource = interactionSource,
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Training image",
                    modifier = Modifier.aspectRatio(16f / 9f)
                )
            }
        },
        modifier = modifier.width(196.dp),
        title = {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        },
        subtitle = { Text(text = subtitle, modifier = Modifier.fillMaxWidth()) }
    )
}

@Preview
@Composable
fun CardItemPreview() {
    TrainingCardItem(
        {},
        "https://figmage.com/images/FbgDhnXP05yKpdsLJfTQo.png",
        "Upper-body basics",
        "14 Min  |  Intensity •"
    )
}