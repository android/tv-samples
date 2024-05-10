package com.google.jetfit.presentation.screens.training.training_entities.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.width
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ChallengeTabRowIndicator(
    currentTabPosition: DpRect,
    doesTabRowHaveFocus: Boolean,
    modifier: Modifier = Modifier,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.secondary,
) {
    val unfocusedUnderlineWidth = 8.dp
    val indicatorHeight = 2.dp
    val width by
    animateDpAsState(
        targetValue =
        if (doesTabRowHaveFocus)
            currentTabPosition.width
        else
            unfocusedUnderlineWidth,
        label = "UnderlinedIndicator.width",
    )
    val leftOffset by
    animateDpAsState(
        targetValue =
        if (doesTabRowHaveFocus) {
            currentTabPosition.left
        } else {
            val tabCenter = currentTabPosition.left + currentTabPosition.width / 2
            tabCenter - unfocusedUnderlineWidth / 2
        },
        label = "UnderlinedIndicator.leftOffset",
    )

    val underlineColor by
    animateColorAsState(
        targetValue = if (doesTabRowHaveFocus) activeColor else inactiveColor,
        label = "UnderlinedIndicator.underlineColor",
    )

    Box(
        modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = leftOffset)
            .width(width)
            .height(indicatorHeight)
            .background(color = underlineColor, shape = RoundedCornerShape(indicatorHeight))
    )
}
