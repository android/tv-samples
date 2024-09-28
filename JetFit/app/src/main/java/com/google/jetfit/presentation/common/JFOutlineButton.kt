package com.google.jetfit.presentation.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.OutlinedButtonDefaults
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.presentation.theme.JetFitTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
internal fun JFOutlineButton(
    modifier: Modifier = Modifier,
    buttonText: String = "",
    isIcon: Boolean = false,
    @DrawableRes icon: Int = R.drawable.ic_rounded_play,
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        modifier = modifier.height(40.dp),
        onClick = onClick,
        colors = OutlinedButtonDefaults.colors(
            containerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedContentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = ButtonDefaults.shape(shape = RoundedCornerShape(40.dp)),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isIcon) Arrangement.Start else Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isIcon) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "button icon",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = buttonText,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun JFOutlineButtonWithIconPreview() {
    JetFitTheme {
        JFOutlineButton(
            isIcon = true,
            buttonText = "Start workout"
        )
    }
}

@Preview
@Composable
private fun JFOutlineButtonWithoutIconPreview() {
    JetFitTheme {
        JFOutlineButton(
            buttonText = "Subscribe now"
        )
    }
}