package com.google.jetfit.presentation.screens.subscription

import android.content.Context
import com.google.jetfit.R
import com.google.jetfit.data.entities.Subscription

sealed interface SubscriptionUiState {
    data object Loading : SubscriptionUiState
    data object Error : SubscriptionUiState
    data class Ready(
        val subscriptionOptions: List<Subscription>
    ) : SubscriptionUiState {
        fun formatPeriodTimeAndPrice(periodTime: String, price: String, context: Context): String {
            return "${context.getString(R.string.free_trail)} $price / ${
                if (periodTime == "1")
                    "${context.getString(R.string.month)}.\n"
                else
                    "$periodTime \n${context.getString(R.string.months)}."
            }${context.getString(R.string.subscription_cancelled)}"
        }

        fun formatPeriodTime(periodTime: String, context: Context): String {
            return "$periodTime ${context.getString(R.string.month_subscription)}"
        }
    }
}