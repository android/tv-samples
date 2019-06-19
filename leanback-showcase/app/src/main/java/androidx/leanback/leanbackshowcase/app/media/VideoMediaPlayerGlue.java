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

import android.app.Activity;
import android.os.Handler;
import androidx.leanback.media.PlaybackTransportControlGlue;
import androidx.leanback.media.PlayerAdapter;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.PlaybackControlsRow;
import android.widget.Toast;

/**
 * PlayerGlue for video playback
 * @param <T>
 */
public class VideoMediaPlayerGlue<T extends PlayerAdapter> extends PlaybackTransportControlGlue<T> {

    private PlaybackControlsRow.RepeatAction mRepeatAction;
    private PlaybackControlsRow.ThumbsUpAction mThumbsUpAction;
    private PlaybackControlsRow.ThumbsDownAction mThumbsDownAction;
    private PlaybackControlsRow.PictureInPictureAction mPipAction;
    private PlaybackControlsRow.ClosedCaptioningAction mClosedCaptioningAction;

    public VideoMediaPlayerGlue(Activity context, T impl) {
        super(context, impl);
        mClosedCaptioningAction = new PlaybackControlsRow.ClosedCaptioningAction(context);
        mThumbsUpAction = new PlaybackControlsRow.ThumbsUpAction(context);
        mThumbsUpAction.setIndex(PlaybackControlsRow.ThumbsUpAction.OUTLINE);
        mThumbsDownAction = new PlaybackControlsRow.ThumbsDownAction(context);
        mThumbsDownAction.setIndex(PlaybackControlsRow.ThumbsDownAction.OUTLINE);
        mRepeatAction = new PlaybackControlsRow.RepeatAction(context);
        mPipAction = new PlaybackControlsRow.PictureInPictureAction(context);
    }

    @Override
    protected void onCreateSecondaryActions(ArrayObjectAdapter adapter) {
        adapter.add(mThumbsUpAction);
        adapter.add(mThumbsDownAction);
        if (android.os.Build.VERSION.SDK_INT > 23) {
            adapter.add(mPipAction);
        }
    }

    @Override
    protected void onCreatePrimaryActions(ArrayObjectAdapter adapter) {
        super.onCreatePrimaryActions(adapter);
        adapter.add(mRepeatAction);
        adapter.add(mClosedCaptioningAction);
    }

    @Override
    public void onActionClicked(Action action) {
        if (shouldDispatchAction(action)) {
            dispatchAction(action);
            return;
        }
        super.onActionClicked(action);
    }

    private boolean shouldDispatchAction(Action action) {
        return action == mRepeatAction || action == mThumbsUpAction || action == mThumbsDownAction
                || action == mPipAction || action == mClosedCaptioningAction;
    }

    private void dispatchAction(Action action) {
        if (action == mPipAction) {
            ((Activity) getContext()).enterPictureInPictureMode();
        } else {
            Toast.makeText(getContext(), action.toString(), Toast.LENGTH_SHORT).show();
            PlaybackControlsRow.MultiAction multiAction = (PlaybackControlsRow.MultiAction) action;
            multiAction.nextIndex();
            notifyActionChanged(multiAction);
        }
    }

    private void notifyActionChanged(PlaybackControlsRow.MultiAction action) {
        int index = -1;
        if (getPrimaryActionsAdapter() != null) {
            index = getPrimaryActionsAdapter().indexOf(action);
        }
        if (index >= 0) {
            getPrimaryActionsAdapter().notifyArrayItemRangeChanged(index, 1);
        } else {
            if (getSecondaryActionsAdapter() != null) {
                index = getSecondaryActionsAdapter().indexOf(action);
                if (index >= 0) {
                    getSecondaryActionsAdapter().notifyArrayItemRangeChanged(index, 1);
                }
            }
        }
    }

    private ArrayObjectAdapter getPrimaryActionsAdapter() {
        if (getControlsRow() == null) {
            return null;
        }
        return (ArrayObjectAdapter) getControlsRow().getPrimaryActionsAdapter();
    }

    private ArrayObjectAdapter getSecondaryActionsAdapter() {
        if (getControlsRow() == null) {
            return null;
        }
        return (ArrayObjectAdapter) getControlsRow().getSecondaryActionsAdapter();
    }

    Handler mHandler = new Handler();

    @Override
    protected void onPlayCompleted() {
        super.onPlayCompleted();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mRepeatAction.getIndex() != PlaybackControlsRow.RepeatAction.NONE) {
                    play();
                }
            }
        });
    }

    public void setMode(int mode) {
        mRepeatAction.setIndex(mode);
        if (getPrimaryActionsAdapter() == null) {
            return;
        }
        notifyActionChanged(mRepeatAction);
    }
}