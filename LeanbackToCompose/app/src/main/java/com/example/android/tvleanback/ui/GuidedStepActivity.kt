package com.example.android.tvleanback.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.android.tvleanback.ui.screens.GuidedStepScreen

class GuidedStepActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuidedStepScreen(onFinish = { finish() })
        }
    }
}
