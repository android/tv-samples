package com.example.android.tvleanback.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.android.tvleanback.model.Video
import com.example.android.tvleanback.ui.screens.VerticalGridScreen

class VerticalGridActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VerticalGridScreen(
                onVideoClick = { video ->
                    val intent = Intent(this, VideoDetailsActivity::class.java).apply {
                        putExtra(VideoDetailsActivity.VIDEO, video)
                    }
                    startActivity(intent)
                },
                onSearchClick = {
                    startActivity(Intent(this, SearchActivity::class.java))
                }
            )
        }
    }
}
