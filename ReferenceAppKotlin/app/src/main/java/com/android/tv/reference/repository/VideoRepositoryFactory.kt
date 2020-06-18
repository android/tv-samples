package com.android.tv.reference.repository

import android.app.Application

/**
 * Class to create a VideoRepository implementation according to specific requirements.
 */
class VideoRepositoryFactory private constructor() {

    companion object {
        fun getVideoRepository(application: Application): VideoRepository {
            /**
             * Return a specific VideoRepository implementation following some rules.
             * Currently, only the FileVideoService implementation is available.
             */
           return FileVideoRepository(application)
        }
    }
}
