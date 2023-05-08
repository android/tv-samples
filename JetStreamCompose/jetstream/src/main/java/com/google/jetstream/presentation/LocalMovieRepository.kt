package com.google.jetstream.presentation

import androidx.compose.runtime.compositionLocalOf
import com.google.jetstream.data.repositories.MovieRepository
import com.google.jetstream.data.util.AssetsReader

val LocalMovieRepository = compositionLocalOf<MovieRepository?> { null }