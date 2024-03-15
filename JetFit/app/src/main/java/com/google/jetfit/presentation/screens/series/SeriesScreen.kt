package com.google.jetfit.presentation.screens.series

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.components.MetricItem
import com.google.jetfit.components.RegimenInformationScaffold
import com.google.jetfit.components.RegimenMetrics
import com.google.jetfit.presentation.theme.JetFitTheme


@Composable
fun SeriesScreen() {
    val seriesViewModel: SeriesViewModel = hiltViewModel()
    val state by seriesViewModel.state.collectAsState()
    SeriesScreenContent(state, seriesViewModel)
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SeriesScreenContent(
    state: SeriesUiState,
    interactionListener: SeriesInteractionListener
) {
    RegimenInformationScaffold(
        imageUrl = state.posterUrl,
        contentDescription = "Series poster",
        subtitle = state.subtitle,
        title = state.title,
        description = state.description,
        topPadding = 200.dp,
        regimenMetrics = {
            RegimenMetrics(
                Modifier.padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                MetricItem(state.numberOfWeeks, "Weeks")
                MetricItem(state.numberOfClasses, "Classes")
                MetricItem(state.intensity, "Intensity")
                MetricItem(state.minutesPerDay, "Minutes per day")
            }
        },
        regimenActions = {
            Button(onClick = { interactionListener.onStartProgramClicked() }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.play_icon),
                        contentDescription = "Play icon"
                    )
                    Text(text = "Start program", modifier = Modifier.padding(start = 6.dp))
                }
            }
            OutlinedButton(onClick = { interactionListener.onRecommendScheduleClicked() }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.message_icon),
                        contentDescription = "favorite icon",
                    )
                    Text(
                        text = "Recommend schedule",
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }
        }
    )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_TELEVISION,
    device = "id:tv_1080p"
)
@Composable
fun SeriesScreenPreview() {
    JetFitTheme {
        SeriesScreen()
    }
}