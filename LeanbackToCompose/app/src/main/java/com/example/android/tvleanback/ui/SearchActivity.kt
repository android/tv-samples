package com.example.android.tvleanback.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.android.tvleanback.ui.screens.SearchScreen

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchScreen(
                onVideoClick = { video ->
                    val intent = Intent(this, VideoDetailsActivity::class.java).apply {
                        putExtra(VideoDetailsActivity.VIDEO, video)
                    }
                    startActivity(intent)
                }
            )
        }
    }
}
