package com.google.jetfit.presentation.screens.training.workout

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.components.ImageWithRadialGradient
import com.google.jetfit.presentation.screens.training.composables.MetricItem
import com.google.jetfit.presentation.theme.JetFitTheme

@Composable
fun WorkoutScreen(viewmodel: WorkoutViewModel = hiltViewModel()) {
    val state: WorkoutUiState by viewmodel.state.collectAsState()
    WorkoutScreenContent(state)
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun WorkoutScreenContent(
    state: WorkoutUiState
) {
    Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
        ImageWithRadialGradient(
            Modifier.align(Alignment.TopEnd),
            state.imageUrl,
            "Workout image"
        )
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(top = 196.dp, start = 48.dp)
                    .width(484.dp),
            ) {
                Text(
                    text = state.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = state.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = state.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.padding(top = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    MetricItem(state.duration, "Duration")
                    MetricItem(state.intensity, "Intensity")
                }
                Row(
                    modifier = Modifier.padding(top = 28.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.play_icon),
                                contentDescription = "Play icon"
                            )
                            Text(text = "Start workout", modifier = Modifier.padding(start = 6.dp))
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
fun WorkoutScreenPreview() {
    JetFitTheme {
        WorkoutScreen()
    }
}