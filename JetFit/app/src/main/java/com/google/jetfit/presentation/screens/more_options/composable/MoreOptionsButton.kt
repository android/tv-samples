package com.google.jetfit.presentation.screens.more_options.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.presentation.theme.JetFitTheme


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
internal fun MoreOptionsButton(
    modifier: Modifier = Modifier,
    text: String = "",
    @DrawableRes icon: Int,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier.size(height = 50.dp, width = 292.dp),
        shape = ButtonDefaults.shape(shape = RoundedCornerShape(12.dp)),
        colors = ButtonDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                0.4f
            ),
            focusedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        scale = ButtonDefaults.scale(
            scale = 1f,
            focusedScale = 1.1f,
        ),
        onClick = onClick
    ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = icon),
                contentDescription = "button icon",
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }

@Preview
@Composable
private fun MoreOptionsButtonPreview() {
    JetFitTheme {
        MoreOptionsButton(
            text = "Add to favorites",
            icon = R.drawable.ic_outline_favorite,
        )
    }
}