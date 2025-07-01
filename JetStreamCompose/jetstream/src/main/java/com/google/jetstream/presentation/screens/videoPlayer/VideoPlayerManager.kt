package com.google.jetstream.presentation.screens.videoPlayer

import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.google.jetstream.data.entities.MovieDetails
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@UnstableApi
// Do not make this a singleton to prevent Released player from Being invoked for play

/**
 * A manager for the video player.
 * This class is responsible for managing the video playback using the ExoPlayer library.
 *
 * @param VideoPlayerManager  Creates an Instance of the player and it available across app for Re-use
 * @property release Releases the player to prevent memory leaks
 */
class VideoPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var _exoPlayer: ExoPlayer? = ExoPlayer.Builder(context)
        .setSeekForwardIncrementMs(10)
        .setSeekBackIncrementMs(10)
        .setMediaSourceFactory(
            ProgressiveMediaSource.Factory(DefaultDataSource.Factory(context))
        )
        .setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        .build().apply {
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_OFF
        }

    val player: ExoPlayer
        get() = _exoPlayer ?: throw IllegalStateException("Player has been released")

    fun load(movieDetails: MovieDetails) {
        player.apply {
            stop()
            clearMediaItems()
            addMediaItem(movieDetails.intoMediaItem())
            movieDetails.similarMovies.forEach { addMediaItem(it.intoMediaItem()) }
            prepare()
        }
    }

    fun release() {
        _exoPlayer?.release()
        _exoPlayer = null
    }
}

