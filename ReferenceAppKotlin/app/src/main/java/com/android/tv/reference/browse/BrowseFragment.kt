/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.tv.reference.browse

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.tv.reference.R
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.image.BlurImageTransformation
import com.android.tv.reference.shared.image.OverlayImageTransformation
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import timber.log.Timber

/**
 * Fragment displaying the main content browsing UI
 *
 * This shows a menu of categories and a row for each category. Users can pick which content to
 * play by navigating and pressing the main select button.
 */
class BrowseFragment : BrowseSupportFragment(), Target {

    companion object {
        private const val BACKGROUND_UPDATE_DELAY_MILLIS = 500L
        private const val BACKGROUND_RESOURCE_ID = R.drawable.image_placeholder
    }

    private lateinit var viewModel: BrowseViewModel
    private lateinit var backgroundManager: BackgroundManager
    private lateinit var handler: Handler

    private val overlayImageTransformation =
        OverlayImageTransformation()
    private lateinit var blurImageTransformation: BlurImageTransformation

    // The DisplayMetrics instance is used to get the screen dimensions
    private val displayMetrics = DisplayMetrics()

    // The URI of the background we are currently displaying to avoid reloading the same one
    private var backgroundUri = ""

    private val backgroundRunnable: Runnable = Runnable {
        updateBackgroundImmediate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        blurImageTransformation = BlurImageTransformation(requireContext())

        handler = Handler()
        backgroundManager = BackgroundManager.getInstance(requireActivity()).apply {
            attach(requireActivity().window)
            setThemeDrawableResourceId(BACKGROUND_RESOURCE_ID)
        }

        val menuMore = BrowseCustomMenu(
            getString(R.string.menu_more),
            mapOf(
                // temporary - sign in will be triggered automatically in the future
                BrowseCustomMenu.MenuItem(getString(R.string.sign_in)) to {
                    findNavController().navigate(R.id.action_global_signInFragment)
                },
                BrowseCustomMenu.MenuItem(getString(R.string.profile)) to {
                    findNavController()
                        .navigate(BrowseFragmentDirections.actionBrowseFragmentToProfileFragment())
                }
            )
        )

        viewModel = ViewModelProvider(this).get(BrowseViewModel::class.java)
        viewModel.browseContent.observe(
            this,
            Observer {
                adapter = BrowseAdapter(it, listOf(menuMore))
            }
        )

        setOnItemViewClickedListener { _, item, _, _ ->
            when (item) {
                is Video ->
                    findNavController().navigate(
                        BrowseFragmentDirections.actionBrowseFragmentToPlaybackFragment(item)
                    )
                is BrowseCustomMenu.MenuItem -> menuMore.navigate(item)
            }
        }

        setOnItemViewSelectedListener { _, item, _, _ ->
            if (item is Video) {
                updateBackgroundDelayed(item)
            }
        }
    }

    /**
     * Updates the main fragment background after a delay
     *
     * This delay allows the user to quickly scroll through content without flashing a changing
     * background with every item that is passed.
     */
    private fun updateBackgroundDelayed(video: Video) {
        if (backgroundUri != video.backgroundImageUri) {
            handler.removeCallbacks(backgroundRunnable)
            backgroundUri = video.backgroundImageUri

            if (backgroundUri.isEmpty()) {
                backgroundManager.setThemeDrawableResourceId(BACKGROUND_RESOURCE_ID)
            } else {
                handler.postDelayed(backgroundRunnable, BACKGROUND_UPDATE_DELAY_MILLIS)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Picasso.get().cancelRequest(this)
    }

    private fun updateBackgroundImmediate() {
        if (activity == null) {
            // Triggered after fragment detached from activity, ignore
            return
        }

        Picasso.get()
            .load(backgroundUri)
            .centerCrop()
            .resize(displayMetrics.widthPixels, displayMetrics.heightPixels)
            .onlyScaleDown()
            .transform(overlayImageTransformation).transform(blurImageTransformation)
            .into(this)
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        // Keep displaying the previous background
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        Timber.w(e, "Failed to load background")
        showDefaultBackground()
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        if (bitmap == null) {
            Timber.w("Background loaded but was null")
            showDefaultBackground()
        } else {
            backgroundManager.setBitmap(bitmap)
        }
    }

    private fun showDefaultBackground() {
        backgroundUri = ""
        backgroundManager.setThemeDrawableResourceId(BACKGROUND_RESOURCE_ID)
    }
}
