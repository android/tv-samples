package com.google.jetfit.presentation.screens.trainingentities.challenge

sealed class Page {
    data object Details : Page()
    data object Tabs : Page()
}