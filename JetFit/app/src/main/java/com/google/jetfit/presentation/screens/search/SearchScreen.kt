package com.google.jetfit.presentation.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.google.jetfit.presentation.theme.surfaceContainerHigh

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SearchScreen() {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(surfaceContainerHigh),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Search Screen", fontSize = 24.sp)
    }
}