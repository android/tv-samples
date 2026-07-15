package com.example.android.tvleanback.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.android.tvleanback.ui.screens.OnboardingScreen

class OnboardingActivity : ComponentActivity() {
    companion object {
        @JvmField
        val COMPLETED_ONBOARDING = "completed_onboarding"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnboardingScreen(
                onFinish = {
                    startActivity(android.content.Intent(this@OnboardingActivity, MainActivity::class.java))
                    finish()
                }
            )
        }
    }
}
