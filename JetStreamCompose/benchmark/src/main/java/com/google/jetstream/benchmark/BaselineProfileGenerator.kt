/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test

class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startup() = baselineProfileRule.collect(
        packageName = JETSTREAM_PACKAGE_NAME
    ) {
        startActivityAndWait()
        device.waitForIdle()

        device.run {
            // Wait until screen loads and Carousel gains focus
            wait(
                Until.findObject(By.descContains(CAROUSEL_DESCRIPTION).focused(true)),
                INITIAL_WAIT_TIMEOUT
            )

            // Explore the Home tab
            repeat(2) { pressDPadRight(); waitForIdle(WAIT_TIMEOUT) }
            repeat(2) { pressDPadLeft(); waitForIdle(WAIT_TIMEOUT) }

            repeat(3) {
                pressDPadDown()
                waitForIdle()
                repeat(4) { pressDPadRight(); waitForIdle() }
                repeat(4) { pressDPadLeft(); waitForIdle() }
                waitForIdle()
            }

            // Navigate to Categories tab
            repeat(2) { pressDPadUp(); waitForIdle() }
            pressDPadDown()
            waitForIdle()
            repeat(2) { pressDPadUp(); waitForIdle() }

            pressDPadRight()
            wait(
                Until.findObject(By.text(CATEGORIES_TAB_LABEL).focused(true)),
                WAIT_TIMEOUT
            )

            // Explore the Categories tab
            pressDPadDown()
            waitForIdle()
            repeat(2) { pressDPadRight(); waitForIdle() }

            // Navigate to Movies tab
            pressDPadUp()
            waitForIdle()
            pressDPadRight()
            waitForIdle()
            wait(
                Until.findObject(By.text(MOVIES_TAB_LABEL).focused(true)),
                WAIT_TIMEOUT
            )

            // Explore the Movies tab
            repeat(2) {
                pressDPadDown()
                waitForIdle()
                repeat(4) { pressDPadRight(); waitForIdle() }
                repeat(4) { pressDPadLeft(); waitForIdle() }
                waitForIdle()
            }

            // Navigate to Shows tab
            repeat(2) { pressDPadUp(); pressDPadRight(); waitForIdle() }
            wait(
                Until.findObject(By.text(SHOWS_TAB_LABEL).focused(true)),
                WAIT_TIMEOUT
            )

            // Explore the Shows tab
            repeat(2) {
                pressDPadDown()
                waitForIdle()
                repeat(4) { pressDPadRight(); waitForIdle() }
                repeat(4) { pressDPadLeft(); waitForIdle() }
                waitForIdle()
            }

            // Navigate to Favourites tab
            repeat(2) { pressDPadUp(); waitForIdle() }
            pressDPadRight()
            wait(
                Until.findObject(By.text(FAVOURITES_TAB_LABEL).focused(true)),
                WAIT_TIMEOUT
            )

            // Explore the favourites tab
            pressDPadDown()
            waitForIdle()
            pressDPadRight()
            wait(
                Until.findObject(By.text(TV_SHOWS_CHIP_LABEL).focused(true)),
                WAIT_TIMEOUT
            )
            pressDPadCenter()
            waitForIdle()
            repeat(2) { pressDPadDown(); waitForIdle() }
            pressDPadRight()
            waitForIdle()

            // Navigate to Search tab
            repeat(3) { pressDPadUp(); waitForIdle() }
            pressDPadRight()
            wait(
                Until.findObject(
                    By.descContains(SEARCH_TAB_DESCRIPTION).focused(true)
                ),
                WAIT_TIMEOUT
            )

            // Explore the Search tab
            repeat(2) { pressDPadDown(); waitForIdle() }
            repeat(10) { pressDPadRight(); waitForIdle() }
            repeat(10) { pressDPadLeft(); waitForIdle() }

            // Navigate to Account screen
            repeat(2) { pressDPadUp(); waitForIdle() }
            repeat(6) { pressDPadLeft(); waitForIdle() }
            wait(
                Until.findObject(By.descContains(PROFILE_BUTTON_DESCRIPTION).focused(true)),
                WAIT_TIMEOUT
            )
            pressDPadCenter()
            waitForIdle()

            // Explore the Account screen
            repeat(5) { pressDPadDown(); waitForIdle() }

            // Navigate to Category details screen
            repeat(6) { pressDPadUp(); waitForIdle() }
            wait(
                Until.findObject(By.descContains(PROFILE_BUTTON_DESCRIPTION).focused(true)),
                WAIT_TIMEOUT
            )
            repeat(2) { pressDPadRight(); waitForIdle() }
            wait(
                Until.findObject(By.text(CATEGORIES_TAB_LABEL).focused(true)),
                WAIT_TIMEOUT
            )
            pressDPadDown()
            waitForIdle()
            pressDPadCenter()
            waitForIdle()

            // Explore Category details screen
            repeat(4) { pressDPadDown(); waitForIdle() }
            repeat(4) { pressDPadUp(); waitForIdle() }
            waitForIdle()

            // Navigate to Movie details screen
            pressDPadCenter()
            waitForIdle()

            // Explore Movie details screen
            repeat(4) { pressDPadDown(); waitForIdle() }
            repeat(4) { pressDPadUp(); waitForIdle() }
        }
    }
}

private const val JETSTREAM_PACKAGE_NAME = "com.google.jetstream"

private const val INITIAL_WAIT_TIMEOUT = 2000L
private const val WAIT_TIMEOUT = 1000L

private const val PROFILE_BUTTON_DESCRIPTION = "User Profile"
private const val HOME_TAB_LABEL = "Home"
private const val CATEGORIES_TAB_LABEL = "Categories"
private const val MOVIES_TAB_LABEL = "Movies"
private const val SHOWS_TAB_LABEL = "Shows"
private const val FAVOURITES_TAB_LABEL = "Favourites"
private const val SEARCH_TAB_DESCRIPTION = "Dashboard Search"
private const val TV_SHOWS_CHIP_LABEL = "TV Shows"
private const val CAROUSEL_DESCRIPTION = "Carousel"
