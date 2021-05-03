/*
 * Copyright 2020 Google LLC
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
 *
 */

package com.android.tv.reference.playback

import androidx.leanback.media.PlayerAdapter
import androidx.leanback.widget.PlaybackControlsRow.PlayPauseAction
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProgressTransportControlGlueTest {

  private lateinit var progressTransportControlGlue:
    ProgressTransportControlGlue<PlayerAdapter>

  @Before
  fun setUp() {
    val fakePlayerAdapter: PlayerAdapter = object : PlayerAdapter() {
      var _currentPosition = 0L

      override fun play() {}
      override fun pause() {}

      override fun getCurrentPosition() = _currentPosition

      // Sample duration for a Video object used in the tests set to 100 seconds. This duration can
      // be tweaked and only needs to be longer than the playback position the tests seek to.
      override fun getDuration() = 100_000L

      override fun seekTo(positionInMs: Long) {
        _currentPosition = positionInMs
      }
    }

    progressTransportControlGlue = ProgressTransportControlGlue(
      ApplicationProvider.getApplicationContext(),
      fakePlayerAdapter,
      /* updateProgress= */ { }
    )
  }

  @Test
  fun onActionClicked_skipBackwardAction_skipToNewPosition() {
    // Seek to position of 50 seconds in the video.
    progressTransportControlGlue.seekTo(50_000L)

    val oldPosition = progressTransportControlGlue.currentPosition

    progressTransportControlGlue
      .onActionClicked(progressTransportControlGlue.skipBackwardAction)

    val newPosition = progressTransportControlGlue.currentPosition

    // The expected value of the position is 20 seconds after skipping backwards 30 seconds.
    assertThat(newPosition).isEqualTo(20_000L)
  }

  @Test
  fun onActionClicked_skipBackwardAction_skipToStart() {
    // Seek to position of 20 seconds in the video. Since skipping backwards moves the position back
    // by 30 seconds, the new position should be the start of the video.
    progressTransportControlGlue.seekTo(20_000L)

    val oldPosition = progressTransportControlGlue.currentPosition

    progressTransportControlGlue
      .onActionClicked(progressTransportControlGlue.skipBackwardAction)

    val newPosition = progressTransportControlGlue.currentPosition

    // The expected value of the position is the start of the video after skipping backwards.
    assertThat(newPosition).isEqualTo(0L)
  }

  @Test
  fun onActionClicked_skipForwardAction_skipToNewPosition() {
    // Seek to position of 50 seconds in the video.
    progressTransportControlGlue.seekTo(50_000L)

    val oldPosition = progressTransportControlGlue.currentPosition

    progressTransportControlGlue
      .onActionClicked(progressTransportControlGlue.skipForwardAction)

    val newPosition = progressTransportControlGlue.currentPosition

    // The expected value of the position is 80 seconds after skipping forwards 30 seconds.
    assertThat(newPosition).isEqualTo(80_000L)
  }

  @Test
  fun onActionClicked_skipForwardAction_skipToEnd() {
    // Seek to position of 90 seconds in the video. Since skipping forwards moves the position
    // forward by 30 seconds, the new position should be the end of the video.
    progressTransportControlGlue.seekTo(90_000L)

    val oldPosition = progressTransportControlGlue.currentPosition

    progressTransportControlGlue
      .onActionClicked(progressTransportControlGlue.skipForwardAction)

    val newPosition = progressTransportControlGlue.currentPosition

    // The expected value of the position is the end of the video after skipping forwards.
    assertThat(newPosition).isEqualTo(100_000L)
  }

  @Test
  fun onActionClicked_playPauseAction() {
    // Seek to position of 50 seconds in the video.
    progressTransportControlGlue.seekTo(50000L)

    val oldPosition = progressTransportControlGlue.currentPosition

    progressTransportControlGlue
      .onActionClicked(PlayPauseAction(ApplicationProvider.getApplicationContext()))

    val newPosition = progressTransportControlGlue.currentPosition

    assertThat(newPosition).isEqualTo(oldPosition)
  }
}