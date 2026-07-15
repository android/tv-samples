@file:Suppress("GlobalCoroutineDispatchers")

package com.example.android.tvleanback.data

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import com.example.android.tvleanback.model.Video
import com.example.android.tvleanback.model.VideoCursorMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object VideoRepository {
    suspend fun getVideos(context: Context): List<Video> = withContext(Dispatchers.IO) {
        val videos = queryVideos(context)
        if (videos.isEmpty()) {
            val intent = Intent(context, FetchVideoService::class.java)
            context.startService(intent)
        }
        videos
    }

    fun getVideosFlow(context: Context): Flow<List<Video>> = callbackFlow {
        val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                launch(Dispatchers.IO) {
                    trySend(queryVideos(context))
                }
            }
        }

        context.contentResolver.registerContentObserver(
            VideoContract.VideoEntry.CONTENT_URI,
            true,
            contentObserver
        )

        val initialVideos = queryVideos(context)
        if (initialVideos.isEmpty()) {
            val intent = Intent(context, FetchVideoService::class.java)
            context.startService(intent)
        }
        trySend(initialVideos)

        awaitClose {
            context.contentResolver.unregisterContentObserver(contentObserver)
        }
    }.flowOn(Dispatchers.IO)

    private fun queryVideos(context: Context): List<Video> {
        val cursor = context.contentResolver.query(
            VideoContract.VideoEntry.CONTENT_URI,
            null, null, null, null
        )
        val videos = mutableListOf<Video>()
        cursor?.use {
            val mapper = VideoCursorMapper()
            while (it.moveToNext()) {
                videos.add(mapper.convert(it) as Video)
            }
        }
        return videos
    }
}
