package com.google.jetfit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun CardWithInnerContent(
    modifier: Modifier = Modifier,
    imageUrl: String,
    title: String,
    description: String,
    borderColor: Color = MaterialTheme.colorScheme.onSurface,
    radius: Dp = 16.dp,
    cardAspectRatio: Float = 35f / 12f,
    onClick: () -> Unit = {},
) {

    Card(
        onClick = { onClick() },
        modifier = modifier
            .fillMaxWidth(.3f)
            .aspectRatio(cardAspectRatio)
            .background(Color.Transparent, RoundedCornerShape(radius))
            .padding(bottom = 8.dp)
            .border(width = 2.dp, color = borderColor.copy(.3f), shape = RoundedCornerShape(radius))
    ) {
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background
                )
        ) {
            Box {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                    alignment = Alignment.Center,
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(.6f))
                )
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth(.9f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCardWithInnerContent() {
    CardWithInnerContent(
        imageUrl = "https://live.staticflickr.com/3060/3304130387_1f3c41d5ab.jpg",
        title = "Aerobic & Cardio",
        description = "Get your blood pumping and build up your endurance with some..."
    )
}