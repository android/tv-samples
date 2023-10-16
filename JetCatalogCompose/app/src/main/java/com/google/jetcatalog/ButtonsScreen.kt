package com.google.jetcatalog

import androidx.compose.runtime.Composable
import androidx.tv.material3.ExperimentalTvMaterial3Api

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ButtonsScreen() {
    val actions = listOf(
        ExampleAction(
            title = "Filled button",
            content = { }
        ),
        ExampleAction(
            title = "Outlined button",
            content = { }
        ),
        ExampleAction(
            title = "Icon button",
            content = { }
        ),
        ExampleAction(
            title = "Wide button",
            content = { }
        ),
    )

    ExamplesScreenWithDottedBackground(actions)
}