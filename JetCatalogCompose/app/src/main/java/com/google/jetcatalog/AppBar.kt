package com.google.jetcatalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

@Composable
fun AppBar(
    title: String,
    onThemeColorModeClick: () -> Unit,
    onFontScaleClick: () -> Unit,
    description: String? =
        if (title == stringResource(id = R.string.tv_compose)) "Component Catalog" else null,
    isMainIconMagnified: Boolean = description != null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 54.dp, top = 40.dp, end = 38.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HeadlineContent(
            title = title,
            description = description,
            isMainIconMagnified = isMainIconMagnified
        )
        Actions(
            onThemeColorModeClick = onThemeColorModeClick,
            onFontScaleClick = onFontScaleClick,
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun HeadlineContent(
    title: String,
    description: String? = null,
    isMainIconMagnified: Boolean,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(if (isMainIconMagnified) 64.dp else 40.dp)
                .background(
                    MaterialTheme.colorScheme.secondaryContainer.copy(0.4f),
                    shape = CircleShape
                ), contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .padding(if (isMainIconMagnified) 12.dp else 8.dp)
                    .size(if (isMainIconMagnified) 38.dp else 22.dp),
                painter = painterResource(id = R.drawable.ic_tv),
                contentDescription = null
            )
        }

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = title,
                maxLines = 1,
                style = MaterialTheme.typography.headlineMedium,
                overflow = TextOverflow.Ellipsis,
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun Actions(
    onThemeColorModeClick: () -> Unit,
    onFontScaleClick: () -> Unit,
) {
    val actions = listOf(
        Action(
            iconPainter = painterResource(id = R.drawable.ic_palette),
            text = "Theme & color mode",
            onClick = onThemeColorModeClick
        ),
        Action(
            iconPainter = painterResource(id = R.drawable.ic_font),
            text = "Font scale",
            onClick = onFontScaleClick
        )
    )

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        actions.forEach {
            Button(onClick = it.onClick) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = it.iconPainter,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = it.text,
                    style = MaterialTheme.typography.labelLarge.copy(textMotion = TextMotion.Animated)
                )
            }
        }
    }
}

private data class Action(
    val iconPainter: Painter,
    val text: String,
    val onClick: () -> Unit,
)
