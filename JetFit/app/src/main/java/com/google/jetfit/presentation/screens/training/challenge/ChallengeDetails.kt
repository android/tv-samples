package com.google.jetfit.presentation.screens.training.challenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.components.PlanTextWithIcon
import com.google.jetfit.presentation.screens.training.composables.MetricItem

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ChallengeDetails(
    state: ChallengeUiState,
) {
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
                text = state.description,
                modifier = Modifier.padding(top = 20.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                Modifier.padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                MetricItem(state.numberOfDays, "Days")
                MetricItem(state.intensity, "Intensity")
                MetricItem(state.minutesPerDay, "Minutes per day")
            }
            Row(
                modifier = Modifier.padding(top = 28.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.play_icon),
                            contentDescription = "Play icon"
                        )
                        Text(text = "Start session", modifier = Modifier.padding(start = 6.dp))
                    }
                }
                OutlinedButton(
                    onClick = { },
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.fav_icon),
                            contentDescription = "favorite icon",
                        )
                        Text(
                            text = "Add to favorites",
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
            }
            PlanTextWithIcon(
                modifier = Modifier.padding(top = 36.dp),
                subtitle = "Weekly plan",
                painter = painterResource(id = R.drawable.down_arrow_head_icon),
                contentDescription = "down arrow head icon"
            )
        }
    }
}