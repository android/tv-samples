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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
internal fun JFFilledButton(
    modifier: Modifier = Modifier,
    buttonText: String = "",
    isIcon: Boolean = false,
    @DrawableRes icon: Int = R.drawable.ic_rounded_play,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier.height(40.dp),
        onClick = onClick,
        colors = ButtonDefaults.colors(containerColor = MaterialTheme.colorScheme.onSurface),
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
                    tint = MaterialTheme.colorScheme.inverseOnSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = buttonText,
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.W500,
                ),
                color = MaterialTheme.colorScheme.inverseOnSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun JFFilledButtonWithIconPreview() {
    JetFitTheme {
        JFFilledButton(
            isIcon = true,
            buttonText = "Start workout"
        )
    }
}

@Preview
@Composable
private fun JFFilledButtonWithoutIconPreview() {
    JetFitTheme {
        JFFilledButton(
            buttonText = "Subscribe now"
        )
    }
}