package com.google.jetfit.data.repository.Series

import com.google.jetfit.data.entities.Series
import com.google.jetfit.data.entities.Song

interface SeriesRepository {
    fun getSeries(): List<Series>
    fun getSeriesById(id: String): Series
    fun getSongById(id: String): Song
}