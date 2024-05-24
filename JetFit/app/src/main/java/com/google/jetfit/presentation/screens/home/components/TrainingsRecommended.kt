package com.google.jetfit.presentation.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyHorizontalGrid
import androidx.tv.foundation.lazy.grid.items
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.components.CustomCard
import com.google.jetfit.data.entities.Training

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TrainingsRecommended(
    modifier: Modifier = Modifier,
    state: List<Training>,
    onClick: (id: String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Text(
            modifier = Modifier.padding(start = 32.dp),
            text = stringResource(R.string.recommended_for_you),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        TvLazyHorizontalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .height(370.dp),
            rows = TvGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(horizontal = 32.dp)
        ) {
            items(state) { training ->
                CustomCard(
                    modifier = modifier.width(196.dp),
                    imageUrl = training.imageUrl,
                    title = training.title,
                    timeText = training.time,
                    typeText = training.instructor,
                    onClick = { onClick(training.id) }
                )
            }
        }
    }
}