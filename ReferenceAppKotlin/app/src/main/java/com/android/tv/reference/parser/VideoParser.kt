package com.android.tv.reference.parser

import com.android.tv.reference.shared.datamodel.Video
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject

object VideoParser {
    fun loadVideosFromJson(jsonString: String): List<Video> {
        try {
            val contentJson = JSONObject(jsonString)
            return Gson().fromJson(
                contentJson.getJSONArray("content").toString(),
                object : TypeToken<List<Video>>() {}.type
            )
        } catch (e: JSONException) {
            throw IllegalArgumentException("Invalid JSON")
        }
    }

    fun findVideoFromJson(jsonString: String, videoId: String): Video? {
        val videosList = loadVideosFromJson(jsonString)
        for (video in videosList) {
            if (video.name == videoId) {
                return video
            }
        }
        return null
    }
}