package com.android.tv.reference.repository

import android.app.Application
import android.content.res.Resources
import com.android.tv.reference.R
import com.android.tv.reference.parser.VideoParser
import com.android.tv.reference.shared.datamodel.Video

/**
 * VideoRepository implementation to read video data from a file saved on /res/raw
 */
class FileVideoRepository(override val application: Application) : VideoRepository {
    private var allVideos: List<Video>

    init {
        var jsonString = readJsonFromFile()
        allVideos = VideoParser.loadVideosFromJson(jsonString)
    }

    private fun readJsonFromFile(): String {
        val inputStream = application.resources.openRawResource(R.raw.api)
        return inputStream.bufferedReader().use {
            it.readText()
        }
    }

    override fun getAllVideos(): List<Video> {
        return allVideos
    }
}