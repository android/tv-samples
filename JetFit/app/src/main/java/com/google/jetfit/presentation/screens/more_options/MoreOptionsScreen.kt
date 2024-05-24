package com.google.jetfit.presentation.screens.more_options

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.jetfit.R
import com.google.jetfit.presentation.screens.more_options.composable.BackRowSchema
import com.google.jetfit.presentation.screens.more_options.composable.MoreOptionsButton
import com.google.jetfit.presentation.screens.more_options.composable.TrainingDetails


@Composable
fun MoreOptionsScreen(
    viewModel: MoreOptionsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onStartClick: () -> Unit,
    onFavouriteClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    MoreOptionsContent(
        state = state,
        onBackPressed = onBackPressed,
        onStartClick = onStartClick,
        onFavouriteClick = onFavouriteClick
    )
}

@Composable
private fun MoreOptionsContent(
    state: MoreOptionsUiState,
    onBackPressed: () -> Unit,
    onStartClick: () -> Unit,
    onFavouriteClick: () -> Unit,
) {
    when (state) {
        MoreOptionsUiState.Error -> Log.d("Tarek", "Error")
        MoreOptionsUiState.Loading -> Log.d("Tarek", "Loading")
        is MoreOptionsUiState.Ready -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                ConstraintLayout {
                    val (
                        trainingDetails,
                        backRowSchema,
                        startButton,
                        favoritesButton,
                        moreInfoButton,
                        viewInstructorButton,
                        shareButton
                    ) = createRefs()

                    TrainingDetails(
                        modifier = Modifier.constrainAs(trainingDetails) {},
                        title = state.trainingDetails.title,
                        time = state.formatTimeAndTypeTraining(),
                        description = state.trainingDetails.description
                    )
                    BackRowSchema(
                        modifier = Modifier.constrainAs(backRowSchema) {
                            top.linkTo(trainingDetails.bottom, margin = 50.dp)
                        },
                        onClickBack = onBackPressed
                    )


                    MoreOptionsButton(
                        modifier = Modifier.constrainAs(startButton) {
                            top.linkTo(trainingDetails.top)
                            start.linkTo(trainingDetails.end, margin = 164.dp)
                        },
                        text = stringResource(R.string.start_workout),
                        icon = R.drawable.ic_rounded_play,
                        onClick = onStartClick
                    )
                    MoreOptionsButton(
                        modifier = Modifier.constrainAs(favoritesButton) {
                            top.linkTo(startButton.bottom, margin = 12.dp)
                            start.linkTo(startButton.start)
                        },
                        text = stringResource(R.string.add_to_favorites),
                        icon = R.drawable.ic_outline_favorite,
                        onClick = onFavouriteClick
                    )
                    MoreOptionsButton(
                        modifier = Modifier.constrainAs(moreInfoButton) {
                            top.linkTo(favoritesButton.bottom, margin = 12.dp)
                            start.linkTo(startButton.start)
                        },
                        text = stringResource(R.string.more_info),
                        icon = R.drawable.ic_info
                    )
                    MoreOptionsButton(
                        modifier = Modifier.constrainAs(viewInstructorButton) {
                            top.linkTo(moreInfoButton.bottom, margin = 12.dp)
                            start.linkTo(startButton.start)
                        },
                        text = stringResource(R.string.view_instructor),
                        icon = R.drawable.ic_instructor
                    )
                    MoreOptionsButton(
                        modifier = Modifier.constrainAs(shareButton) {
                            top.linkTo(viewInstructorButton.bottom, margin = 12.dp)
                            start.linkTo(startButton.start)
                        },
                        text = stringResource(R.string.share),
                        icon = R.drawable.ic_share
                    )
                }
            }
        }
    }
}

