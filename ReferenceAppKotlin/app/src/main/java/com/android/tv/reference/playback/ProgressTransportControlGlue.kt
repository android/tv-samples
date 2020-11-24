package com.android.tv.reference.playback

import android.content.Context
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.media.PlayerAdapter
import androidx.leanback.media.MediaPlayerAdapter
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter

/**
 * Custom [PlaybackTransportControlGlue] that exposes a callback when the progress is updated.
 *
 * The callback is triggered based on a progress interval defined in several ways depending on the
 * [PlayerAdapter].
 *
 * [LeanbackPlayerAdapter] example:
 * ```
 *     private val updateMillis = 16
 *     LeanbackPlayerAdapter(context, exoplayer, updateMillis)
 * ```
 *
 * [MediaPlayerAdapter] example:
 * ```
 *     object : MediaPlayerAdapter(context) {
 *         private val updateMillis = 16
 *         override fun getProgressUpdatingInterval(): Int {
 *             return updateMillis
 *         }
 *     }
 * ```
 */
class ProgressTransportControlGlue<T : PlayerAdapter>(
    context: Context,
    impl: T,
    private val updateProgress: () -> Unit
) : PlaybackTransportControlGlue<T>(context, impl) {

    override fun onUpdateProgress() {
        super.onUpdateProgress()
        updateProgress()
    }
}