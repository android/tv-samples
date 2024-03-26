package com.google.jetfit.presentation.screens.more_options.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.presentation.theme.JetFitTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
internal fun BackRowSchema(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.press),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        IconButton(
            modifier = Modifier.size(20.dp),
            colors = ButtonDefaults.colors(containerColor = MaterialTheme.colorScheme.onSurfaceVariant),
            onClick = onClickBack,
            scale = ButtonDefaults.scale(
                scale = 1f,
                focusedScale = 1.2f,
            )
        ) {
            Icon(
                modifier = Modifier.size(12.dp),
                painter = painterResource(R.drawable.ic_back_arrow), contentDescription = "back icon",
                tint = MaterialTheme.colorScheme.background
            )
        }
        Text(
            text = stringResource(R.string.to_go_back),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
private fun BackRowSchemaPreview() {
    JetFitTheme {
        BackRowSchema()
    }
}