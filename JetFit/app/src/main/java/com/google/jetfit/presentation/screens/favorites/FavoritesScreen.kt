package com.google.jetfit.presentation.screens.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyHorizontalGrid
import androidx.tv.foundation.lazy.grid.items
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.google.jetfit.R
import com.google.jetfit.components.CustomCardWithIntensity
import com.google.jetfit.components.CustomOutLinedButtonWithLeadingIcon
import com.google.jetfit.data.entities.FavWorkout
import com.google.jetfit.presentation.theme.onSurface
import com.google.jetfit.presentation.theme.popupShadow
import com.google.jetfit.presentation.theme.surfaceContainerHigh
import com.google.jetfit.presentation.theme.surfaceVariant
import com.google.jetfit.presentation.utils.shadowBox

@Composable
fun FavoritesScreen(
    onBackPressed: () -> Unit
) {

    val favoritesViewModel: FavoritesViewModel = hiltViewModel()
    val uiState by favoritesViewModel.uiState.collectAsStateWithLifecycle()
    val selectedItem by favoritesViewModel.selectedWorkout.collectAsStateWithLifecycle()

    when (val value = uiState) {
        is FavoritesScreenUiState.Ready -> {
            FavoritesScreenContent(
                modifier = Modifier,
                workoutsList = value.favoritesWorkouts,
                onWorkoutSelect = favoritesViewModel::onWorkoutSelect,
                onStartWorkout = favoritesViewModel::onStartWorkout,
                onRemoveWorkout = favoritesViewModel::onRemoveWorkout,
                selectedItem = selectedItem,
                onBackPressed = onBackPressed
            )
        }

        is FavoritesScreenUiState.Loading -> {
            Loading(modifier = Modifier.fillMaxSize())
        }

        is FavoritesScreenUiState.Error -> {
            Error(modifier = Modifier.fillMaxSize())
        }
    }
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun FavoritesScreenContent(
    modifier: Modifier = Modifier,
    workoutsList: List<FavWorkout>,
    selectedItem: FavWorkout? = null,
    onStartWorkout: (id: String) -> Unit,
    onRemoveWorkout: (id: String) -> Unit,
    onWorkoutSelect: (FavWorkout) -> Unit,
    onBackPressed: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Favorites",
            modifier = Modifier.padding(bottom = 8.dp, top = 56.dp, start = 32.dp, end = 32.dp),
            style = TextStyle(
                fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.LightGray
            )
        )
        TvLazyHorizontalGrid(
            contentPadding = PaddingValues(horizontal = 46.dp),
            rows = TvGridCells.Fixed(2),
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {
            items(items = workoutsList, key = { it.id }) { item ->
                CustomCardWithIntensity(modifier = Modifier
                    .width(196.dp)
                    .padding(horizontal = 12.dp),
                    imageUrl = item.image,
                    title = item.name,
                    timeText = item.duration,
                    typeText = "Intensity",
                    intensityLevel = item.intensity,
                    onClick = {
                        onWorkoutSelect(item)
                    })
            }
        }
        AnimatedVisibility(
            visible = selectedItem != null,
            enter = fadeIn(
                animationSpec = tween(300)
            ),
            exit = fadeOut(
                animationSpec = tween(300)
            ),
        ) {
            selectedItem?.let {
                WorkoutDetailsPopup(
                    workout = it,
                    onStartWorkout = onStartWorkout,
                    onRemoveWorkout = onRemoveWorkout,
                    onBackPressed = onBackPressed
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun WorkoutDetailsPopup(
    workout: FavWorkout,
    onStartWorkout: (id: String) -> Unit,
    onRemoveWorkout: (id: String) -> Unit,
    onBackPressed: () -> Unit
) {
    Dialog(onDismissRequest = onBackPressed) {
        Box(
            modifier = Modifier
                .background(surfaceContainerHigh, RoundedCornerShape(16.dp))
                .fillMaxWidth(0.75f)
                .fillMaxHeight(0.8f)
                .shadowBox(
                    color = popupShadow,
                    blurRadius = 40.dp,
                    offset = DpOffset(0.dp, 8.dp)
                )
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.88f),
                model = workout.image,
                contentDescription = null,
                contentScale = ContentScale.Inside
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(surfaceVariant.copy(alpha = 0.35f))
                    .padding(horizontal = 24.dp), horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.45f))
                Text(
                    text = workout.name,
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.titleMedium,
                    color = onSurface,
                    modifier = Modifier.padding(bottom = 8.dp),
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "${workout.duration} | Intensity ",
                        modifier = Modifier,
                        textAlign = TextAlign.Justify,
                        style = MaterialTheme.typography.labelMedium,
                        color = onSurface,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = true,
                        maxLines = 4
                    )
                    repeat(workout.intensity) { Text(text = "•") }
                }
                Text(
                    text = workout.description,
                    modifier = Modifier.padding(bottom = 28.dp),
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true,
                    maxLines = 4
                )
                CustomOutLinedButtonWithLeadingIcon(
                    text = "Start",
                    icon = R.drawable.play_icon,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    onStartWorkout(workout.id)
                }
                CustomOutLinedButtonWithLeadingIcon(
                    text = "Remove from favorites",
                    icon = R.drawable.icon_remove,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    onRemoveWorkout(workout.id)
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Text(text = "Loading...", modifier = modifier, textAlign = TextAlign.Center)
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun Error(modifier: Modifier = Modifier) {
    Text(text = "Wops, something went wrong.", modifier = modifier, textAlign = TextAlign.Center)
}