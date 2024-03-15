package com.google.jetfit.presentation.screens.workout


import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.components.MetricItem
import com.google.jetfit.components.RegimenInformationScaffold
import com.google.jetfit.components.RegimenMetrics
import com.google.jetfit.presentation.theme.JetFitTheme

@Composable
fun WorkoutScreen() {
    WorkoutScreenContent()
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun WorkoutScreenContent() {
    RegimenInformationScaffold(
        imageUrl = "https://figmage.com/images/FbgDhnXP05yKpdsLJfTQo.png",
        contentDescription = "Workout poster",
        subtitle = "Charlotte Aldridge  |  Strength",
        title = "Glutes and leg strength",
        description = "Strong legs do more than look good. Even the simplest daily movements like walking require leg strength. But you may wonder where to begin. Let’s dive in.",
        regimenMetrics = {
            RegimenMetrics(
                modifier = Modifier.padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                MetricItem("34 min", "Duration")
                MetricItem("Hard", "Intensity")
            }
        },
        regimenActions = {
            Button(
                onClick = {  }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.play_icon),
                        contentDescription = "Play icon"
                    )
                    Text(text = "Start workout", modifier = Modifier.padding(start = 6.dp))
                }
            }
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_TELEVISION,
    device = "id:tv_1080p"
)
@Composable
fun WorkoutScreenPreview() {
    JetFitTheme {
        WorkoutScreen()
    }
}