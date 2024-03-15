package com.google.jetfit.presentation.screens.routine

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
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.OutlinedIconButton
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.components.MetricItem
import com.google.jetfit.components.RegimenInformationScaffold
import com.google.jetfit.components.RegimenMetrics
import com.google.jetfit.presentation.theme.JetFitTheme

@Composable
fun RoutineScreen() {
    RoutineScreenContent()
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RoutineScreenContent() {
    RegimenInformationScaffold(
        imageUrl = "https://figmage.com/images/FbgDhnXP05yKpdsLJfTQo.png",
        contentDescription = "Routine poster",
        subtitle = "Rachel Wright  |  Yoga",
        title = "10 Morning Exercises",
        description = "Don’t let mornings put you in a bad mood! Make your day so much better by launching yourself off your bed and getting in to a full-on workout mode.",
        topPadding = 200.dp,
        regimenMetrics = {
            RegimenMetrics(
                modifier = Modifier.padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                MetricItem("10 min", "Duration")
                MetricItem("Medium", "Intensity")
            }
        },
        regimenActions = {
            Button(onClick = { /*TODO*/ }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.play_icon),
                        contentDescription = "Play icon"
                    )
                    Text(text = "Start routine", modifier = Modifier.padding(start = 6.dp))
                }
            }
            OutlinedButton(onClick = { /*TODO*/ }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.bell_icon),
                        contentDescription = "Bell icon"
                    )
                    Text(text = "Setup a daily reminder", modifier = Modifier.padding(start = 6.dp))
                }
            }
            OutlinedIconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.fav_icon),
                    contentDescription = "Favorite icon",
                )
            }
        }
    )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_TELEVISION,
    device = "id:tv_1080p"
)
@Composable
fun RoutineScreenPreview() {
    JetFitTheme {
        RoutineScreen()
    }
}