package com.android.tv.reference.repository

import android.app.Application
import com.android.tv.reference.shared.datamodel.Video

/**
 * Interface to define methods to interact with different data sources.
 */
interface VideoRepository {
    abstract val application: Application

    /**
     * Return all videos available from a specific source.
     * @return List<Video>
     */
    fun getAllVideos(): List<Video>
}