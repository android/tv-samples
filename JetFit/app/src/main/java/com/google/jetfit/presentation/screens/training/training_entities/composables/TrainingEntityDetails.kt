package com.google.jetfit.presentation.screens.training.training_entities.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.presentation.screens.training.training_entities.TrainingEntityUiState
import com.google.jetfit.presentation.screens.training.training_entities.TrainingEntityUiState.ContentType.*
import com.google.jetfit.presentation.screens.training.training_entities.getSecondaryButtonID
import com.google.jetfit.presentation.screens.training.training_entities.getSecondaryButtonIcon
import com.google.jetfit.presentation.screens.training.training_entities.getStartButtonID
import com.google.jetfit.presentation.screens.training.training_entities.isSecondaryButtonVisible

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TrainingEntityDetails(
    state: TrainingEntityUiState,
    onClickStart: () -> Unit,
    onClickSecondaryButton: () -> Unit,
    onClickRoutineFavourite: () -> Unit,
    onClickChallengesPlan: () -> Unit
) {
    val descriptionWidth = (LocalConfiguration.current.screenWidthDp / 2).dp
    val isChallenges = state.contentType == CHALLENGES
    val isRoutine = state.contentType == ROUTINE
    val paddingBottom = when (state.contentType) {
        CHALLENGES -> 24.dp
        else -> 80.dp
    }
    Column(
        modifier = Modifier.padding(start = 48.dp, bottom = paddingBottom),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = state.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = state.title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = state.description,
            modifier = Modifier.width(descriptionWidth),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(48.dp)) {
            state.itemsInfo.forEach { item ->
                TrainingInfo(
                    info = item.info,
                    label = item.label
                )
            }
        }
        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TrainingDetailsButton(
                iconId = R.drawable.play_icon,
                textId = state.contentType.getStartButtonID(),
                onClick = onClickStart
            )
            if (state.contentType.isSecondaryButtonVisible())
                TrainingDetailsButton(
                    iconId = state.contentType.getSecondaryButtonIcon(),
                    textId = state.contentType.getSecondaryButtonID(),
                    onClick = onClickSecondaryButton
                )
            if (isRoutine)
                RoutineFavouriteButton(
                    isFavorite = state.isFavorite,
                    onClick = onClickRoutineFavourite
                )
        }
        if (isChallenges)
            ChallengesPlanButton(
                modifier = Modifier.padding(top = 16.dp),
                subtitle = stringResource(R.string.weekly_plan),
                iconId = R.drawable.down_arrow_head_icon,
                onClick = onClickChallengesPlan
            )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TrainingDetailsButton(
    iconId: Int,
    textId: Int,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = null,
            )
            Text(
                text = stringResource(textId)
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TrainingInfo(
    info: String,
    label: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = info,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RoutineFavouriteButton(isFavorite: Boolean, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        colors = ButtonDefaults.colors(containerColor = Color.Transparent),
        border = ButtonDefaults.border(
            border = Border(
                BorderStroke(
                    2.dp,
                    MaterialTheme.colorScheme.border
                )
            )
        )
    ) {
        Icon(
            painter = painterResource(
                if (isFavorite)
                    R.drawable.favorite
                else
                    R.drawable.fav_icon
            ), contentDescription = null
        )
    }
}