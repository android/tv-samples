package com.google.jetfit.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import com.google.jetfit.R

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RegimenInformationScaffold(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String,
    subtitle: String,
    title: String,
    description: String,
    topPadding: Dp = 196.dp,
    regimenMetrics: @Composable (ColumnScope) -> Unit,
    regimenActions: @Composable (RowScope) -> Unit,
    extra: @Composable (ColumnScope) -> Unit = {}
) {
    Box(modifier = modifier.background(color = MaterialTheme.colorScheme.background)) {
        CinematicBackground(
            Modifier.align(Alignment.TopEnd),
            imageUrl,
            contentDescription
        )
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(top = topPadding, start = 48.dp)
                    .width(484.dp),
            ) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
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

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(
    device = "id:tv_1080p",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_TELEVISION
)
@Composable
fun RegimenInformationScaffoldPreview() {
    RegimenInformationScaffold(
        imageUrl = "https://s3-alpha-sig.figma.com/img/5b76/28da/51c6b4c0076ea7b92c70d82dc1828425?Expires=1710720000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=M1NOL3tptpUBJ4qM0QbqmkCQBAKjWpNw3llpK70HUmcVyUPF9StrimFkFA32ziyk-X8GQz8VJHrT42ovbtj3ROiDwLBbLfpCbkuNaThYT5D0BAVkRZtSjkp~w3yDLQKdRSWp~1pn242mMj5ASFpYjL9udDM4JBHn9gjvzST7QGzvOHes9ZABFtimxVBC0Ot-eQDpDV7mbU9Pf5ROC2JTEd2LK-QG85N0Vv8cIFpUcPJGSFgR1tbHxMDv1GpKAx33eSGnH02~ow3R6sZm88wznn0AaPJoKwGGvU2ZJUVl6wbUD4JRt9gcs3q9FVFFEhSeoOpYbJSdqgWdzhPM-Lv-7Q__",
        contentDescription = "Poster",
        subtitle = "Hugo Wright  |  Challenge",
        title = "30 Days of HIIT & mindfulness",
        description = "Build your full body endurance with high-intensity training drills, kick boxing and more. Quick workouts to warm up before or cool down after your run.",
        regimenMetrics = {
            RegimenMetrics(
                Modifier.padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                MetricItem("30", "Days")
                MetricItem("Easy", "Intensity")
                MetricItem("15", "Minutes per day")
            }
        },
        regimenActions = {
            Button(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.play_icon),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Play icon"
                )
                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = "Start session",
                    style = MaterialTheme.typography.labelLarge
                )
            }
            OutlinedButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.fav_icon),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "favorite icon"
                )
                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = "Add to favorites",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    ) {
        PlanTextWithIcon(
            modifier = Modifier.padding(top = 36.dp),
            subtitle = "Weekly plan",
            painter = painterResource(id = R.drawable.down_arrow_head_icon),
            contentDescription = "down arrow head icon"
        )
    }
}