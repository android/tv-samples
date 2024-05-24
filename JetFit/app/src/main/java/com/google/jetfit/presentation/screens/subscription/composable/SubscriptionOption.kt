package com.google.jetfit.presentation.screens.subscription.composable


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.RadioButton
import androidx.tv.material3.RadioButtonDefaults
import androidx.tv.material3.Text
import com.google.jetfit.presentation.theme.JetFitTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
internal fun SubscriptionOption(
    modifier: Modifier = Modifier,
    title: String = "",
    description: String = "",
    price: String = "",
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Text(
                text = price,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            RadioButton(
                modifier = Modifier.size(24.dp),
                selected = isSelected,
                onClick = { onClick() },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.secondary,
                    unselectedColor = MaterialTheme.colorScheme.border
                )
            )
        }
    }
}

@Preview
@Composable
private fun SelectedSubscriptionOptionPreview() {
    JetFitTheme {
        SubscriptionOption(
            title = "1 Month Subscription",
            description = "Start your 7-day free trial then \$7.99 / month.\nSubscription continues until cancelled",
            price = "$7.99",
            isSelected = true,
        )
    }
}

@Preview
@Composable
private fun UnSelectedSubscriptionOptionPreview() {
    JetFitTheme {
        SubscriptionOption(
            title = "1 Month Subscription",
            description = "Start your 7-day free trial then \$7.99 / month.\nSubscription continues until cancelled",
            price = "$7.99",
            isSelected = false,
        )
    }
}