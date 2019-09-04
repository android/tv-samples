/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package androidx.leanback.leanbackshowcase.app.media;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import androidx.leanback.media.PlaybackGlueHost;
import androidx.leanback.media.PlayerAdapter;
import androidx.leanback.media.SurfaceHolderGlueHost;
import androidx.leanback.leanbackshowcase.R;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * This implementation extends the {@link PlayerAdapter} with a {@link SimpleExoPlayer}.
 */
public class ExoPlayerAdapter extends PlayerAdapter implements ExoPlayer.EventListener{

    Context mContext;
    final SimpleExoPlayer mPlayer;
    SurfaceHolderGlueHost mSurfaceHolderGlueHost;
    final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getCallback().onCurrentPositionChanged(ExoPlayerAdapter.this);
            getCallback().onBufferedPositionChanged(ExoPlayerAdapter.this);
            mHandler.postDelayed(this, getUpdatePeriod());
        }
    };
    final Handler mHandler = new Handler();
    boolean mInitialized = false;
    Uri mMediaSourceUri = null;
    boolean mHasDisplay;
    boolean mBufferingStart;
    @C.StreamType int mAudioStreamType;

    /**
     * Constructor.
     */
    public ExoPlayerAdapter(Context context) {
        mContext = context;
        mPlayer = ExoPlayerFactory.newSimpleInstance(mContext,
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        mPlayer.addListener(this);
    }

    @Override
    public void onAttachedToHost(PlaybackGlueHost host) {
        if (host instanceof SurfaceHolderGlueHost) {
            mSurfaceHolderGlueHost = ((SurfaceHolderGlueHost) host);
            mSurfaceHolderGlueHost.setSurfaceHolderCallback(new VideoPlayerSurfaceHolderCallback());
        }
    }

    /**
     * Will reset the {@link ExoPlayer} and the glue such that a new file can be played. You are
     * not required to call this method before playing the first file. However you have to call it
     * before playing a second one.
     */
    public void reset() {
        changeToUninitialized();
        mPlayer.stop();
    }

    void changeToUninitialized() {
        if (mInitialized) {
            mInitialized = false;
            notifyBufferingStartEnd();
            if (mHasDisplay) {
                getCallback().onPreparedStateChanged(ExoPlayerAdapter.this);
            }
        }
    }

    /**
     * Notify the state of buffering. For example, an app may enable/disable a loading figure
     * according to the state of buffering.
     */
    void notifyBufferingStartEnd() {
        getCallback().onBufferingStateChanged(ExoPlayerAdapter.this,
                mBufferingStart || !mInitialized);
    }

    /**
     * Release internal {@link SimpleExoPlayer}. Should not use the object after call release().
     */
    public void release() {
        changeToUninitialized();
        mHasDisplay = false;
        mPlayer.release();
    }

    @Override
    public void onDetachedFromHost() {
        if (mSurfaceHolderGlueHost != null) {
            mSurfaceHolderGlueHost.setSurfaceHolderCallback(null);
            mSurfaceHolderGlueHost = null;
        }
        reset();
        release();
    }

    /**
     * @see SimpleExoPlayer#setVideoSurfaceHolder(SurfaceHolder)
     */
    void setDisplay(SurfaceHolder surfaceHolder) {
        boolean hadDisplay = mHasDisplay;
        mHasDisplay = surfaceHolder != null;
        if (hadDisplay == mHasDisplay) {
            return;
        }

        mPlayer.setVideoSurfaceHolder(surfaceHolder);
        if (mHasDisplay) {
            if (mInitialized) {
                getCallback().onPreparedStateChanged(ExoPlayerAdapter.this);
            }
        } else {
            if (mInitialized) {
                getCallback().onPreparedStateChanged(ExoPlayerAdapter.this);
            }
        }
    }

    @Override
    public void setProgressUpdatingEnabled(final boolean enabled) {
        mHandler.removeCallbacks(mRunnable);
        if (!enabled) {
            return;
        }
        mHandler.postDelayed(mRunnable, getUpdatePeriod());
    }

    int getUpdatePeriod() {
        return 16;
    }

    @Override
    public boolean isPlaying() {
        boolean exoPlayerIsPlaying = mPlayer.getPlaybackState() == ExoPlayer.STATE_READY
                && mPlayer.getPlayWhenReady();
        return mInitialized && exoPlayerIsPlaying;
    }

    @Override
    public long getDuration() {
        return mInitialized ? mPlayer.getDuration() : -1;
    }

    @Override
    public long getCurrentPosition() {
        return mInitialized ? mPlayer.getCurrentPosition() : -1;
    }


    @Override
    public void play() {
        if (!mInitialized || isPlaying()) {
            return;
        }

        mPlayer.setPlayWhenReady(true);
        getCallback().onPlayStateChanged(ExoPlayerAdapter.this);
        getCallback().onCurrentPositionChanged(ExoPlayerAdapter.this);
    }

    @Override
    public void pause() {
        if (isPlaying()) {
            mPlayer.setPlayWhenReady(false);
            getCallback().onPlayStateChanged(ExoPlayerAdapter.this);
        }
    }

    @Override
    public void seekTo(long newPosition) {
        if (!mInitialized) {
            return;
        }
        mPlayer.seekTo(newPosition);
    }

    @Override
    public long getBufferedPosition() {
        return mPlayer.getBufferedPosition();
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * Sets the media source of the player with a given URI.
     *
     * @return Returns <code>true</code> if uri represents a new media; <code>false</code>
     * otherwise.
     * @see ExoPlayer#prepare(MediaSource)
     */
    public boolean setDataSource(Uri uri) {
        if (mMediaSourceUri != null ? mMediaSourceUri.equals(uri) : uri == null) {
            return false;
        }
        mMediaSourceUri = uri;
        prepareMediaForPlaying();
        return true;
    }

    public int getAudioStreamType() {
        return mAudioStreamType;
    }

    public void setAudioStreamType(@C.StreamType int audioStreamType) {
        mAudioStreamType = audioStreamType;
    }

    /**
     * Set {@link MediaSource} for {@link SimpleExoPlayer}. An app may override this method in order
     * to use different {@link MediaSource}.
     * @param uri The url of media source
     * @return MediaSource for the player
     */
    public MediaSource onCreateMediaSource(Uri uri) {
        String userAgent = Util.getUserAgent(mContext, "ExoPlayerAdapter");
        return new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(mContext, userAgent),
                new DefaultExtractorsFactory(),
                null,
                null);
    }

    private void prepareMediaForPlaying() {
        reset();
        if (mMediaSourceUri != null) {
            MediaSource mediaSource = onCreateMediaSource(mMediaSourceUri);
            mPlayer.prepare(mediaSource);
        } else {
            return;
        }

        mPlayer.setAudioStreamType(mAudioStreamType);
        mPlayer.setVideoListener(new SimpleExoPlayer.VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                    float pixelWidthHeightRatio) {
                getCallback().onVideoSizeChanged(ExoPlayerAdapter.this, width, height);
            }

            @Override
            public void onRenderedFirstFrame() {
            }
        });
        notifyBufferingStartEnd();
        getCallback().onPlayStateChanged(ExoPlayerAdapter.this);
    }

    /**
     * @return True if ExoPlayer is ready and got a SurfaceHolder if
     * {@link PlaybackGlueHost} provides SurfaceHolder.
     */
    @Override
    public boolean isPrepared() {
        return mInitialized && (mSurfaceHolderGlueHost == null || mHasDisplay);
    }

    /**
     * Implements {@link SurfaceHolder.Callback} that can then be set on the
     * {@link PlaybackGlueHost}.
     */
    class VideoPlayerSurfaceHolderCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            setDisplay(surfaceHolder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            setDisplay(null);
        }
    }

    // ExoPlayer Event Listeners

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        mBufferingStart = false;
        if (playbackState == ExoPlayer.STATE_READY && !mInitialized) {
            mInitialized = true;
            if (mSurfaceHolderGlueHost == null || mHasDisplay) {
                getCallback().onPreparedStateChanged(ExoPlayerAdapter.this);
            }
        } else if (playbackState == ExoPlayer.STATE_BUFFERING) {
            mBufferingStart = true;
        } else if (playbackState == ExoPlayer.STATE_ENDED) {
            getCallback().onPlayStateChanged(ExoPlayerAdapter.this);
            getCallback().onPlayCompleted(ExoPlayerAdapter.this);
        }
        notifyBufferingStartEnd();
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        getCallback().onError(ExoPlayerAdapter.this, error.type,
                mContext.getString(R.string.lb_media_player_error,
                        error.type,
                        error.rendererIndex));
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }
}
