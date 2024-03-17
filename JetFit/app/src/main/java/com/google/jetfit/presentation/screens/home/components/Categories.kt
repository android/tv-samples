package com.google.jetfit.presentation.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.google.jetfit.R
import com.google.jetfit.data.entities.Category
import com.google.jetfit.presentation.theme.JetFitTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun Categories(
    categories: List<Category>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Text(
            modifier = Modifier.padding(start = 32.dp),
            text = stringResource(R.string.categories),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        TvLazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 32.dp)
        ) {
            items(categories) { category ->
                CategoryItem(
                    modifier = modifier,
                    category = category,
                    onClick = { onClick(category.id) }
                )
            }
        }
    }
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    onClick: () -> Unit
) {
    val gradiantColors = arrayOf(
        .6f to MaterialTheme.colorScheme.surfaceVariant,
        1f to Color.Transparent
    )
    Card(
        colors = CardDefaults.colors(Color.Transparent),
        onClick = onClick,
    ) {
        Box(
            modifier = Modifier.clip(MaterialTheme.shapes.small),
            contentAlignment = Alignment.CenterStart
        ) {
            AsyncImage(
                modifier = modifier
                    .size(280.dp, 80.dp)
                    .drawWithContent {
                        drawContent()
                        drawRect(Brush.horizontalGradient(colorStops = gradiantColors))
                    },
                model = category.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    modifier = Modifier.width(180.dp),
                    text = category.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.width(200.dp),
                    text = category.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun CategoriesPreview() {
    JetFitTheme {
        Categories(
            categories = listOf(
                Category(
                    id = "1",
                    imageUrl = "https://s3-alpha-sig.figma.com/img/5b76/28da/51c6b4c0076ea7b92c70d82dc1828425?Expires=1710720000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=M1NOL3tptpUBJ4qM0QbqmkCQBAKjWpNw3llpK70HUmcVyUPF9StrimFkFA32ziyk-X8GQz8VJHrT42ovbtj3ROiDwLBbLfpCbkuNaThYT5D0BAVkRZtSjkp~w3yDLQKdRSWp~1pn242mMj5ASFpYjL9udDM4JBHn9gjvzST7QGzvOHes9ZABFtimxVBC0Ot-eQDpDV7mbU9Pf5ROC2JTEd2LK-QG85N0Vv8cIFpUcPJGSFgR1tbHxMDv1GpKAx33eSGnH02~ow3R6sZm88wznn0AaPJoKwGGvU2ZJUVl6wbUD4JRt9gcs3q9FVFFEhSeoOpYbJSdqgWdzhPM-Lv-7Q__",
                    title = "Yoga & Pilates",
                    description = "There are many benefits to yoga and Pilates, including increased..."
                ),
                Category(
                    id = "2",
                    imageUrl = "https://s3-alpha-sig.figma.com/img/9e9e/d62b/548d491bf82b615ac52bb7b20d5370e4?Expires=1710720000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=WkhebeHHgGiGxNoLU1V2VpriPOZagVf52bfaK~tadSr-C8DmUe5N~lcK8-cN~v2fwO2Y4pcekzf8EsIR72q1KQaOzYe~qcfUpb2VO8PD-vff-jJG2MQxQcOnQfZShjx4K9ttiOg2AfWYwMfMClsxrKHmk0gVDZl1owjwqKzRILlWC8zNZyLjzGijaOo0gHFRmpYMtXOcCn~IQhDkVo-X6lRx-4jb45AiMKLWLxjU3zjCFf1VLW1MbnaSNoyoPj4~4lIsXoTFcrAdQFrGhcjhArEgC7LgEMnJDJDigxipLJ3-wN5uuYNS7bz5CfsD1QzlYnjnp-ZsNbmT9kuEnVx-Dg__",
                    title = "Strength training",
                    description = "Strength training makes you stronger, helps you control your..."
                ),
            ),
            onClick = {}
        )
    }
}
