/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tv.classics.fragments

import android.animation.ArgbEvaluator
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.ObjectAdapter
import androidx.navigation.Navigation
import com.android.tv.classics.R
import androidx.lifecycle.lifecycleScope
import com.android.tv.classics.models.TvMediaDatabase
import com.android.tv.classics.models.TvMediaMetadata
import com.android.tv.classics.workers.TvMediaSynchronizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import androidx.leanback.widget.DiffCallback
import androidx.palette.graphics.Palette
import coil.Coil
import coil.api.get
import coil.api.getAny
import coil.bitmappool.BitmapPool
import coil.transform.Transformation
import com.android.tv.classics.presenters.TvMediaMetadataPresenter
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * A fragment that lets user browse our collection of metadata.
 */
class MediaBrowserFragment : BrowseSupportFragment() {

    private lateinit var database: TvMediaDatabase

    /** Tint applied to the background, default to dark grey */
    private var currentTintColor: Int = ColorUtils.setAlphaComponent(
            Color.parseColor("#000000"), BACKGROUND_TINT_ALPHA)

    /** Animation task used to update the background tint */
    private var backgroundAnimation: Runnable? = null

    /** Background for our fragment, selected randomly at runtime */
    private lateinit var backgroundDrawable: Deferred<Drawable>

    /** Job used to synchronize our media database */
    private lateinit var synchronizeJob: Job

    /** List row used exclusively to display "credits" and no media content */
    private lateinit var creditsRow: ListRow

    /** Used to efficiently add items to our array adapter for display */
    private val listRowDiffCallback = object : DiffCallback<ListRow>() {
        override fun areContentsTheSame(
                oldItem: ListRow, newItem: ListRow) = oldItem.hashCode() == newItem.hashCode()
        override fun areItemsTheSame(
                oldItem: ListRow, newItem: ListRow): Boolean  =
                oldItem.headerItem == newItem.headerItem &&
                        oldItem.adapter.size() == newItem.adapter.size() &&
                        (0 until oldItem.adapter.size()).all { idx ->
                            oldItem.adapter.get(idx) == oldItem.adapter.get(idx)
                        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.app_name)
        headersState = BrowseSupportFragment.HEADERS_DISABLED
        isHeadersTransitionOnBackEnabled = true

        // Initialize database connection
        database = TvMediaDatabase.getInstance(requireContext())

        // Setup this browser fragment's adapter
        adapter = ArrayObjectAdapter(ListRowPresenter())

        // Each time an item is selected, change the background
        setOnItemViewSelectedListener { _, item, _, row ->
            if (item == null) return@setOnItemViewSelectedListener
            val metadata = item as TvMediaMetadata
            Log.d(TAG, "Row selected: ${row.id}. Item selected: ${metadata.id}")

            // Launch the background tint update task in a coroutine
            lifecycleScope.launch(Dispatchers.IO) {
                metadata.artUri?.let { artUri ->
                    Coil.get(artUri) { allowHardware(false) }.toBitmap()
                }?.let {  artBitmap ->
                    Palette.Builder(artBitmap).generate {

                        // Extract dominant color from the generated palette
                        val dominantColor =
                                it?.getDominantColor(currentTintColor) ?: currentTintColor

                        // Modify color's alpha channel to make it partly transparent
                        val backgroundColor =
                                ColorUtils.setAlphaComponent(dominantColor, BACKGROUND_TINT_ALPHA)

                        // Set the partly transparent dominant color as the background tint
                        Log.d(TAG, "Using dominant color for background tint: $backgroundColor")
                        updateBackgroundTint(backgroundColor)
                    }
                }
            }
        }

        // When user clicks on an item, navigate to the now playing screen
        setOnItemViewClickedListener { _, item, _, _ ->
            val metadata = item as TvMediaMetadata
            Navigation.findNavController(
                    requireActivity(), R.id.fragment_container).navigate(
                    MediaBrowserFragmentDirections.actionToNowPlaying(metadata))
        }

        // Instantiate the credits row, which will be added to the adapter inside [populateAdapter]
        creditsRow = ListRow(HeaderItem(getString(R.string.credits)), object : ObjectAdapter() {
            override fun size(): Int = 0
            override fun get(position: Int): Any? = null
        })

        // Keep track of the synchronization work so we can join it later
        synchronizeJob = lifecycleScope.launch(Dispatchers.IO) {

            // Now that the fragment has been created, we can populate our adapter
            populateAdapter(adapter as ArrayObjectAdapter)

            // Start a one-off synchronization job when fragment is shown, bypassing work manager
            TvMediaSynchronizer.synchronize(requireContext())
        }

        // Pick a random background for our fragment
        backgroundDrawable = lifecycleScope.async(Dispatchers.IO) {
            val backgroundUrl = database.backgrounds().findAll().shuffled().firstOrNull()?.uri
            Coil.getAny(backgroundUrl ?: R.mipmap.bg_browse_fallback)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Because we will be handling the database, we must do it from a different context
        // But we also need the view to be completely inflated to know its size, which is why we
        // use `view.post` here.
        view.post { lifecycleScope.launch(Dispatchers.IO) {

            // With the view inflated, we know the final size of our background
            val sizedBackground = Coil.get(backgroundDrawable.await()) {
                transformations(object : Transformation {
                    override fun key(): String = "CenterCropTransform"
                    override suspend fun transform(pool: BitmapPool, input: Bitmap): Bitmap {
                        return ThumbnailUtils.extractThumbnail(input, view.width, view.height)
                    }
                })
            }

            // Set background in main thread
            withContext(Dispatchers.Main) {
                view.background = sizedBackground
                view.backgroundTintMode = PorterDuff.Mode.OVERLAY
                view.backgroundTintList = ColorStateList.valueOf(currentTintColor)
            }

            // Wait for the synchronization to end, then we can see if we need to update the UI
            synchronizeJob.join()

            // Handle special case where the app had no data available e.g. after clearing app data
            val collectionAdapter = adapter as ArrayObjectAdapter
            if (collectionAdapter.size() <= 1) populateAdapter(collectionAdapter)
        } }
    }

    /** Convenience function used to update the background tint to match the selected item */
    private fun updateBackgroundTint(targetColor: Int) = view?.let { view ->

        // We update the background tint using a smooth animation
        ValueAnimator.ofObject(ArgbEvaluator(), currentTintColor, targetColor).apply {
            duration = BACKGROUND_ANIMATION_MILLIS

            // As the animation progresses, we update the background tint
            addUpdateListener {
                view.backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
            }

            // Remote previously scheduled animation, if any
            backgroundAnimation?.let { view.removeCallbacks(it) }

            // Schedule a new animation, to let the user settle on an item
            backgroundAnimation = Runnable {
                start()
                currentTintColor = targetColor
                // Use the same time as animation to prevent overlap, since we only stop scheduled
                // animations but we don't stop ongoing animations
            }.also { view.postDelayed(it, BACKGROUND_ANIMATION_MILLIS) }
        }
    }

    /**
     * Convenience function used to populate the main screen's adapter with all media collections.
     * Since this function makes use of the database, it cannot be run from the main thread.
     */
    private fun populateAdapter(adapter: ArrayObjectAdapter) {
        val rowCount = adapter.size()

        val collections = database.collections().findAll()
        val collectionRows = collections.mapIndexed { idx, collection ->

            // Create header for each album
            val header = HeaderItem(idx.toLong(), collection.title)

            // Create corresponding row adapter for the album's songs
            val listRowAdapter = ArrayObjectAdapter(TvMediaMetadataPresenter()).apply {

                // Add all the collection's metadata to the row's adapter
                setItems(database.metadata().findByCollection(collection.id), null)
            }

            // Add a list row for the <header, row adapter> pair
            ListRow(header, listRowAdapter)
        }

        // Add all new rows at once using our diff callback for a smooth animation
        adapter.setItems(collectionRows + creditsRow, listRowDiffCallback)

        // If we are being requested to scroll to a specific channel, find its index now
        // NOTE: We can't use args by navArgs() because this fragment is startDestination
        val scrollPosition = arguments?.let {
            MediaBrowserFragmentArgs.fromBundle(it)
        }?.channelId?.let { channelId ->
            collections.indexOfFirst { it.id == channelId }.coerceAtLeast(0)
        } ?: 0

        // If user requested to see a specific channel or collections changed, scroll automatically
        if (scrollPosition != selectedPosition || rowCount != adapter.size()) {
            view?.postDelayed({
                setSelectedPosition(scrollPosition, true)
                Log.d(TAG, "Requesting scrolling to $scrollPosition")
            }, BACKGROUND_ANIMATION_MILLIS)
        }
    }

    companion object {
        private val TAG = MediaBrowserFragment::class.java.simpleName

        /** Animation time in milliseconds for background changes */
        private val BACKGROUND_ANIMATION_MILLIS: Long = TimeUnit.SECONDS.toMillis(1)

        /** Alpha component (0-255) of the background color tint */
        private const val BACKGROUND_TINT_ALPHA: Int = 150
    }
}

