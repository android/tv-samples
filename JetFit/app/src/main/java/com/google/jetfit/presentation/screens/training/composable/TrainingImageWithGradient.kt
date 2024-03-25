package com.google.jetfit.presentation.screens.training.composable

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import coil.compose.AsyncImage

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TrainingImageWithGradient(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String,
    width: Dp = 758.dp,
    height: Dp = 426.38.dp,
    startColor: Color = MaterialTheme.colorScheme.background.copy(alpha = 00f),
    endColor: Color = MaterialTheme.colorScheme.background,
    imagePassThrough: Float = 1f
) {
    Box(
        modifier = modifier
            .size(width, height)
            .clipToBounds()
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .alpha(imagePassThrough),
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop
        )
        Canvas(
            modifier = Modifier.size(DpSize(width = width, height = height))
        ) {
            scale(
                scaleY = 1f,
                scaleX = size.width / size.height,
                pivot = Offset((size.width), 0f)
            ) {
                drawRect(
                    Brush.radialGradient(
                        listOf(startColor, endColor),
                        center = Offset((size.width), 0f),
                        radius = (size.height),
                    )
                )
            }
        }

    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_TELEVISION,
    device = "spec:parent=tv_1080p"
)
@Composable
private fun ImageWithRadialGradientPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        TrainingImageWithGradient(
            Modifier.align(Alignment.TopEnd),
            "https://s3-alpha-sig.figma.com/img/5b76/28da/51c6b4c0076ea7b92c70d82dc1828425?Expires=1710720000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=M1NOL3tptpUBJ4qM0QbqmkCQBAKjWpNw3llpK70HUmcVyUPF9StrimFkFA32ziyk-X8GQz8VJHrT42ovbtj3ROiDwLBbLfpCbkuNaThYT5D0BAVkRZtSjkp~w3yDLQKdRSWp~1pn242mMj5ASFpYjL9udDM4JBHn9gjvzST7QGzvOHes9ZABFtimxVBC0Ot-eQDpDV7mbU9Pf5ROC2JTEd2LK-QG85N0Vv8cIFpUcPJGSFgR1tbHxMDv1GpKAx33eSGnH02~ow3R6sZm88wznn0AaPJoKwGGvU2ZJUVl6wbUD4JRt9gcs3q9FVFFEhSeoOpYbJSdqgWdzhPM-Lv-7Q__",
            "poster",
            width = 520.43.dp,
            height = 340.dp
        )
    }
}