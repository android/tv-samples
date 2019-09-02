/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package androidx.leanback.leanbackshowcase.app.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import androidx.leanback.media.PlaybackControlGlue;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.PlaybackControlsRow;
import androidx.leanback.widget.PlaybackControlsRowPresenter;
import android.util.Log;

/**
 * This glue extends the {@link PlaybackControlGlue} with a {@link MediaMetaData} support.
 * It supports 7 actions:
 * <ul>
 * <li>{@link androidx.leanback.widget.PlaybackControlsRow.SkipNextAction}</li>
 * <li>{@link androidx.leanback.widget.PlaybackControlsRow.SkipPreviousAction}</li>
 * <li>{@link androidx.leanback.widget.PlaybackControlsRow.PlayPauseAction}</li>
 * <li>{@link androidx.leanback.widget.PlaybackControlsRow.ShuffleAction}</li>
 * <li>{@link androidx.leanback.widget.PlaybackControlsRow.RepeatAction}</li>
 * <li>{@link androidx.leanback.widget.PlaybackControlsRow.ThumbsDownAction}</li>
 * <li>{@link androidx.leanback.widget.PlaybackControlsRow.ThumbsUpAction}</li>
 * </ul>
 * <p/>
 */
public abstract class MediaPlayerGlue extends PlaybackControlGlue {

    private static final String TAG = "MusicMediaPlayerGlue";
    private static final int REFRESH_PROGRESS = 1;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_PROGRESS:
                    updateProgress();
                    queueNextRefresh();
            }
        }
    };

    protected PlaybackControlsRow.MultiAction mShuffleAction;
    protected PlaybackControlsRow.MultiAction mRepeatAction;
    protected PlaybackControlsRow.MultiAction mThumbsUpAction;
    protected PlaybackControlsRow.MultiAction mThumbsDownAction;
    private long mLastKeyDownEvent = 0L; // timestamp when the last DPAD_CENTER KEY_DOWN occurred

    protected MediaMetaData mMediaMetaData = null;

    public MediaPlayerGlue(Context context) {
        super(context, new int[]{1});
        mShuffleAction = new PlaybackControlsRow.ShuffleAction(getContext());
        mRepeatAction = new PlaybackControlsRow.RepeatAction(getContext());
        mThumbsDownAction = new PlaybackControlsRow.ThumbsDownAction(getContext());
        mThumbsUpAction = new PlaybackControlsRow.ThumbsUpAction(getContext());
        mThumbsDownAction.setIndex(PlaybackControlsRow.ThumbsAction.OUTLINE);
        mThumbsUpAction.setIndex(PlaybackControlsRow.ThumbsAction.OUTLINE);
    }

    @Override
    protected void onCreateSecondaryActions(ArrayObjectAdapter secondaryActionsAdapter) {
        // Instantiate secondary actions
        secondaryActionsAdapter.add(mShuffleAction);
        secondaryActionsAdapter.add(mRepeatAction);
        secondaryActionsAdapter.add(mThumbsDownAction);
        secondaryActionsAdapter.add(mThumbsUpAction);
    }

    @Override
    protected void onCreateControlsRowAndPresenter() {
        super.onCreateControlsRowAndPresenter();
        PlaybackControlsRowPresenter presenter = (PlaybackControlsRowPresenter)
                getPlaybackRowPresenter();
        presenter.setProgressColor(getContext().getResources().getColor(
                R.color.player_progress_color));
        presenter.setBackgroundColor(getContext().getResources().getColor(
                R.color.player_background_color));
    }

    @Override public void enableProgressUpdating(final boolean enabled) {
        Log.d(TAG, "enableProgressUpdating: " + enabled);
        if (!enabled) {
            mHandler.removeMessages(REFRESH_PROGRESS);
            return;
        }
        queueNextRefresh();
    }

    @Override
    public int getUpdatePeriod() {
        return 16;
    }

    private void queueNextRefresh() {
        Message refreshMsg = mHandler.obtainMessage(REFRESH_PROGRESS);
        mHandler.removeMessages(REFRESH_PROGRESS);
        mHandler.sendMessageDelayed(refreshMsg, getUpdatePeriod());
    }

    @Override public void onActionClicked(Action action) {
        if (action instanceof PlaybackControlsRow.ShuffleAction
                || action instanceof PlaybackControlsRow.RepeatAction) {
            ((PlaybackControlsRow.MultiAction) action).nextIndex();
            notifySecondaryActionChanged(action);
        } else if (action == mThumbsUpAction) {
            if (mThumbsUpAction.getIndex() == PlaybackControlsRow.ThumbsAction.SOLID) {
                mThumbsUpAction.setIndex(PlaybackControlsRow.ThumbsAction.OUTLINE);
                notifySecondaryActionChanged(mThumbsUpAction);
            } else {
                mThumbsUpAction.setIndex(PlaybackControlsRow.ThumbsAction.SOLID);
                mThumbsDownAction.setIndex(PlaybackControlsRow.ThumbsAction.OUTLINE);
                notifySecondaryActionChanged(mThumbsUpAction);
                notifySecondaryActionChanged(mThumbsDownAction);
            }
        } else if (action == mThumbsDownAction) {
            if (mThumbsDownAction.getIndex() == PlaybackControlsRow.ThumbsAction.SOLID) {
                mThumbsDownAction.setIndex(PlaybackControlsRow.ThumbsAction.OUTLINE);
                notifySecondaryActionChanged(mThumbsDownAction);
            } else {
                mThumbsDownAction.setIndex(PlaybackControlsRow.ThumbsAction.SOLID);
                mThumbsUpAction.setIndex(PlaybackControlsRow.ThumbsAction.OUTLINE);
                notifySecondaryActionChanged(mThumbsUpAction);
                notifySecondaryActionChanged(mThumbsDownAction);
            }
        } else {
            super.onActionClicked(action);
        }
    }

    static void notifyItemChanged(ArrayObjectAdapter adapter, Object object) {
        int index = adapter.indexOf(object);
        if (index >= 0) {
            adapter.notifyArrayItemRangeChanged(index, 1);
        }
    }

    void notifySecondaryActionChanged(Action act) {
        notifyItemChanged((ArrayObjectAdapter) getControlsRow().getSecondaryActionsAdapter(), act);
    }

    @Override public boolean hasValidMedia() {
        return mMediaMetaData != null;
    }


    @Override public CharSequence getMediaTitle() {
        return hasValidMedia() ? mMediaMetaData.getMediaTitle() : "N/a";
    }

    @Override public CharSequence getMediaSubtitle() {
        return hasValidMedia() ? mMediaMetaData.getMediaArtistName() : "N/a";
    }

    @Override public Drawable getMediaArt() {
        return (hasValidMedia() && mMediaMetaData.getMediaAlbumArtResId() != 0) ?
                getContext().getResources().
                        getDrawable(mMediaMetaData.getMediaAlbumArtResId(), null)
                : null;
    }

    @Override public long getSupportedActions() {
        return PlaybackControlGlue.ACTION_PLAY_PAUSE
                | PlaybackControlGlue.ACTION_SKIP_TO_NEXT
                | PlaybackControlGlue.ACTION_SKIP_TO_PREVIOUS;
    }

    @Override public int getCurrentSpeedId() {
        return isMediaPlaying() ? PLAYBACK_SPEED_NORMAL : PLAYBACK_SPEED_PAUSED;
    }

    public MediaMetaData getMediaMetaData() {
        return mMediaMetaData;
    }

    public void setMediaMetaData(MediaMetaData mediaMetaData) {
        mMediaMetaData = mediaMetaData;
        onMetadataChanged();
    }

}
