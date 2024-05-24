package com.example.benchmark

import android.os.Build
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @RequiresApi(Build.VERSION_CODES.P)
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @RequiresApi(Build.VERSION_CODES.P)
    @Test
    fun generateBaseLineProfile() = baselineProfileRule.collect(
        packageName = "com.google.jetfit"
    ){
        startActivityAndWait()

        device.waitForIdle()

        device.run {
            // Home screen navigation
            wait(Until.findObject(By.descContains(HOME_SCREEN_DESCRIPTION).focused(true)), INITIAL_WAIT_TIMEOUT)
            exploreHomeScreen()

            // Navigate to Profile Selector screen
            navigateToProfileSelector()
            exploreProfileSelectorScreen()

            // Navigate to Settings screen
            navigateToSettingsScreen()
            exploreSettingsScreen()

            // Navigate to Subscription screen
            navigateToSubscriptionScreen()
            exploreSubscriptionScreen()
        }
    }

    private fun UiDevice.exploreHomeScreen() {
        // Interactions on the Home screen
        repeat(2) { pressDPadRight(); waitForIdle(WAIT_TIMEOUT) }
        repeat(2) { pressDPadLeft(); waitForIdle(WAIT_TIMEOUT) }
        pressDPadDown(); waitForIdle(WAIT_TIMEOUT)
        repeat(4) { pressDPadRight(); waitForIdle(WAIT_TIMEOUT) }
        repeat(4) { pressDPadLeft(); waitForIdle(WAIT_TIMEOUT) }
    }

    private fun UiDevice.navigateToProfileSelector() {
        // Navigate to Profile Selector screen
        pressDPadUp(); waitForIdle(WAIT_TIMEOUT)
        pressDPadRight(); waitForIdle(WAIT_TIMEOUT)
        wait(Until.findObject(By.descContains(PROFILE_SELECTOR_SCREEN_DESCRIPTION).focused(true)), WAIT_TIMEOUT)
    }

    private fun UiDevice.exploreProfileSelectorScreen() {
        // Interactions on the Profile Selector screen
        pressDPadRight(); waitForIdle(WAIT_TIMEOUT)
        pressDPadCenter(); waitForIdle(WAIT_TIMEOUT)
    }

    private fun UiDevice.navigateToSettingsScreen() {
        // Navigate to Settings screen
        pressDPadLeft(); waitForIdle(WAIT_TIMEOUT)
        pressDPadDown(); waitForIdle(WAIT_TIMEOUT)
        wait(Until.findObject(By.descContains(SETTINGS_SCREEN_DESCRIPTION).focused(true)), WAIT_TIMEOUT)
    }

    private fun UiDevice.exploreSettingsScreen() {
        // Interactions on the Settings screen
        pressDPadDown(); waitForIdle(WAIT_TIMEOUT)
        repeat(4) { pressDPadRight(); waitForIdle(WAIT_TIMEOUT) }
        repeat(4) { pressDPadLeft(); waitForIdle(WAIT_TIMEOUT) }
    }

    private fun UiDevice.navigateToSubscriptionScreen() {
        // Navigate to Subscription screen
        pressDPadUp(); waitForIdle(WAIT_TIMEOUT)
        pressDPadRight(); waitForIdle(WAIT_TIMEOUT)
        wait(Until.findObject(By.descContains(SUBSCRIPTION_SCREEN_DESCRIPTION).focused(true)), WAIT_TIMEOUT)
    }

    private fun UiDevice.exploreSubscriptionScreen() {
        // Interactions on the Subscription screen
        pressDPadRight(); waitForIdle(WAIT_TIMEOUT)
        pressDPadCenter(); waitForIdle(WAIT_TIMEOUT)
        repeat(4) { pressDPadDown(); waitForIdle(WAIT_TIMEOUT) }
    }
}

private const val INITIAL_WAIT_TIMEOUT = 2000L
private const val WAIT_TIMEOUT = 1000L

private const val HOME_SCREEN_DESCRIPTION = "Home Screen"
private const val PROFILE_SELECTOR_SCREEN_DESCRIPTION = "Profile Selector"
private const val SETTINGS_SCREEN_DESCRIPTION = "Settings Screen"
private const val SUBSCRIPTION_SCREEN_DESCRIPTION = "Subscription Screen"