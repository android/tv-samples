package com.google.jetfit.presentation.screens.training.series

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.components.ImageWithRadialGradient
import com.google.jetfit.presentation.screens.training.composables.MetricItem
import com.google.jetfit.presentation.screens.training.composables.RegimenDescription
import com.google.jetfit.presentation.screens.training.composables.RegimenSubtitle
import com.google.jetfit.presentation.screens.training.composables.RegimenTitle
import com.google.jetfit.presentation.theme.JetFitTheme

@Composable
fun SeriesScreen(seriesViewModel: SeriesViewModel = hiltViewModel()) {
    val state by seriesViewModel.state.collectAsState()
    SeriesScreenContent(state)
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SeriesScreenContent(
    state: SeriesUiState,
) {
    Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
        ImageWithRadialGradient(
            modifier = Modifier.align(Alignment.TopEnd),
            imageUrl = state.imageUrl,
            contentDescription = "Series poster"
        )
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(top = 200.dp, start = 48.dp)
                    .width(484.dp),
            ) {
                RegimenSubtitle(subtitle = state.subtitle)
                RegimenTitle(title = state.title, modifier = Modifier.padding(top = 4.dp))
                RegimenDescription(state.description, Modifier.padding(top = 20.dp))
                Row(
                    modifier = Modifier.padding(top = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    MetricItem(state.numberOfWeeks, "Weeks")
                    MetricItem(state.numberOfClasses, "Classes")
                    MetricItem(state.intensity, "Intensity")
                    MetricItem(state.minutesPerDay, "Minutes per day")
                }
                Row(
                    modifier = Modifier.padding(top = 28.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(onClick = {}) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.play_icon),
                                contentDescription = "Play icon"
                            )
                            Text(text = "Start program", modifier = Modifier.padding(start = 6.dp))
                        }
                    }
                    OutlinedButton(onClick = { }) {
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
            }
        }
    }
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