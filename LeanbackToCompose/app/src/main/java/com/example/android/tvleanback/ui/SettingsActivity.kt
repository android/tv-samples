package com.example.android.tvleanback.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.android.tvleanback.ui.screens.SettingsScreen

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingsScreen(
                onLoginClick = {
                    startActivity(Intent(this, AuthenticationActivity::class.java))
                }
            )
        }
    }
}
