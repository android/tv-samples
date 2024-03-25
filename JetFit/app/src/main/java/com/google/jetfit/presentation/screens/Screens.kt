package com.google.jetfit.presentation.screens

import com.google.jetfit.R

enum class Screens(
    private val args: List<String>? = null,
    var routePath: String? = null,
    var clearBackStack: Boolean = false,
    val isNavigationDrawerItem: Boolean = false,
    val navigationDrawerIcon: Int? = null
) {
    MoreOptions,
    VideoPlayer,
    AudioPlayer,
    Dashboard,
    ProfileSelector,
    Search( isNavigationDrawerItem =true, navigationDrawerIcon = R.drawable.search_),
    Home( isNavigationDrawerItem = true, navigationDrawerIcon = R.drawable.home),
    Training( isNavigationDrawerItem = true, navigationDrawerIcon = R.drawable.fitness_center),
    Favorite( isNavigationDrawerItem = true, navigationDrawerIcon = R.drawable.favorite),
    Settings(isNavigationDrawerItem = true, navigationDrawerIcon = R.drawable.settings);

    operator fun invoke(): String {
        val argList = StringBuilder()
        args?.let { nnArgs ->
            nnArgs.forEach { arg -> argList.append("/{$arg}") }
        }
        return name + argList
    }

    fun withArgs(vararg args: Any): String {
        val destination = StringBuilder()
        args.forEach { arg -> destination.append("/$arg") }
        return name + destination
    }
}