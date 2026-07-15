package com.example.android.tvleanback.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.IntentCompat
import com.example.android.tvleanback.model.Video
import com.example.android.tvleanback.ui.screens.PlaybackScreen

class PlaybackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val video = IntentCompat.getParcelableExtra(intent, VideoDetailsActivity.VIDEO, Video::class.java)
        if (video == null) {
            finish()
            return
        }

        setContent {
            PlaybackScreen(
                video = video,
                onVideoChanged = { activeVideo ->
                    val resultIntent = Intent().apply {
                        putExtra(VideoDetailsActivity.VIDEO, activeVideo)
                    }
                    setResult(RESULT_OK, resultIntent)
                },
                onFinish = { finish() }
            )
        }
    }
}
