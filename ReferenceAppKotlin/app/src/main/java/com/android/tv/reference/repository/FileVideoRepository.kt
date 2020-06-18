package com.android.tv.reference.repository

import android.app.Application
import com.android.tv.reference.R
import com.android.tv.reference.parser.VideoParser
import com.android.tv.reference.shared.datamodel.Video

/**
 * VideoRepository implementation to read video data from a file saved on /res/raw
 */
class FileVideoRepository(override val application: Application) : VideoRepository {
    // Underscore name to allow lazy loading since "getAllVideos" matches the getter name otherwise
    private val _allVideos: List<Video> by lazy {
        val jsonString = readJsonFromFile()
        VideoParser.loadVideosFromJson(jsonString)
    }

    private fun readJsonFromFile(): String {
        val inputStream = application.resources.openRawResource(R.raw.api)
        return inputStream.bufferedReader().use {
            it.readText()
        }
    }

    override fun getAllVideos(): List<Video> {
        return _allVideos
    }

    override fun getVideoById(id: String): Video? {
        val jsonString = readJsonFromFile()
        return VideoParser.findVideoFromJson(jsonString, id)
    }
}
