package com.example.android.tvleanback.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.IntentCompat
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.android.tvleanback.model.Video
import com.example.android.tvleanback.ui.screens.VideoDetailsScreen

class VideoDetailsActivity : ComponentActivity() {
    companion object {
        const val VIDEO = "Video"
        const val SHARED_ELEMENT_NAME = "hero"
        const val NOTIFICATION_ID = "NotificationId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initialVideo = IntentCompat.getParcelableExtra(intent, VIDEO, Video::class.java)
        if (initialVideo == null) {
            finish()
            return
        }

        var activeVideo by mutableStateOf(initialVideo)

        val playbackLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { data ->
                    val updatedVideo = IntentCompat.getParcelableExtra(data, VIDEO, Video::class.java)
                    if (updatedVideo != null) {
                        activeVideo = updatedVideo
                    }
                }
            }
        }

        setContent {
            VideoDetailsScreen(
                video = activeVideo,
                onWatchTrailerClick = { v ->
                    val intent = Intent(this, PlaybackActivity::class.java).apply {
                        putExtra(VIDEO, v)
                    }
                    playbackLauncher.launch(intent)
                },
                onRelatedVideoClick = { v ->
                    val intent = Intent(this, VideoDetailsActivity::class.java).apply {
                        putExtra(VIDEO, v)
                    }
                    startActivity(intent)
                }
            )
        }
    }
}
