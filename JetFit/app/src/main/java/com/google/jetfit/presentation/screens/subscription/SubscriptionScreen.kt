package com.google.jetfit.presentation.screens.subscription

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.jetfit.R
import com.google.jetfit.data.entities.Subscription
import com.google.jetfit.presentation.common.JFFilledButton
import com.google.jetfit.presentation.common.JFOutlineButton
import com.google.jetfit.presentation.screens.subscription.composable.SubscriptionHeadline
import com.google.jetfit.presentation.screens.subscription.composable.SubscriptionOptions

@Composable
fun SubscriptionScreen(
    viewModel: SubscriptionViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val instructorImage by viewModel.instructorImageState.collectAsState()
    val selectedSubscription by viewModel.selectedSubscriptionOption.collectAsState()

    SubscriptionContent(
        state = state,
        instructorImage = instructorImage,
        selectedSubscription = selectedSubscription,
        onBackPressed = onBackPressed,
        updateSubscriptionOption = viewModel::updateSelectedSubscriptionOption
    )
}

@Composable
private fun SubscriptionContent(
    state: SubscriptionUiState,
    instructorImage: String,
    selectedSubscription: Subscription,
    updateSubscriptionOption: (Subscription) -> Unit,
    onBackPressed: () -> Unit
) {
    BackHandler(onBack = onBackPressed)

    when (state) {
        SubscriptionUiState.Error -> Log.d("Tarek", "Error")
        SubscriptionUiState.Loading -> Log.d("Tarek", "Loading")
        is SubscriptionUiState.Ready -> {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    modifier = Modifier
                        .size(width = 324.dp, height = 452.dp)
                        .border(
                            border = BorderStroke(1.dp, Color.White.copy(0.07f)),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp)),
                    painter = rememberAsyncImagePainter(model = instructorImage),
                    contentDescription = "training photo",
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(28.dp))
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    SubscriptionHeadline()
                    SubscriptionOptions(
                        subscriptionOptions = state.subscriptionOptions,
                        formatPeriodTime = state::formatPeriodTime,
                        formatPeriodTimeAndPrice = state::formatPeriodTimeAndPrice,
                        selectedSubscription = selectedSubscription,
                        updateSelectedSubscriptionOption = updateSubscriptionOption
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        JFFilledButton(
                            modifier = Modifier.width(184.dp),
                            buttonText = stringResource(R.string.subscribe_now),
                        )
                        JFOutlineButton(
                            modifier = Modifier.width(184.dp),
                            buttonText = stringResource(R.string.restore_purchases)
                        )
                    }
                }
            }
        }
    }
}
