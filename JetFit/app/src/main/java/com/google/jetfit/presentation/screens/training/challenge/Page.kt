package com.google.jetfit.presentation.screens.training.challenge

sealed class Page {
    data object Details : Page()
    data object Tabs : Page()
}