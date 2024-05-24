package com.google.jetfit.presentation.screens.subscription.composable

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.data.entities.Subscription

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
internal fun SubscriptionOptions(
    modifier: Modifier = Modifier,
    subscriptionOptions: List<Subscription> = emptyList(),
    formatPeriodTime: (String, Context) -> String,
    formatPeriodTimeAndPrice: (String, String, Context) -> String,
    selectedSubscription: Subscription,
    updateSelectedSubscriptionOption: (Subscription) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.width(412.dp),
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(R.string.our_plans),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        subscriptionOptions.forEach {
            SubscriptionOption(
                title = formatPeriodTime(it.periodTime, context),
                description = formatPeriodTimeAndPrice(it.periodTime, it.price, context),
                price = it.price,
                isSelected = selectedSubscription.price == it.price,
                onClick = { updateSelectedSubscriptionOption(it) }
            )
        }
    }
}