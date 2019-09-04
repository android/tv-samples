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
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;

import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.PlaybackControlsRow;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**<p>>
 * This glue extends the {@link MediaPlayerGlue} and handles all the heavy-lifting of the
 * interactions between the fragment, playback controls, and the music service. It starts and
 * connects to a music service which will be running in the background. The music service notifies
 * the listeners set in this glue upon any playback status changes, and this glue will in turn
 * notify listeners passed from the fragment.
 * <p/>
 */
public class MusicMediaPlayerGlue extends MediaPlayerGlue implements
        MusicPlaybackService.ServiceCallback {

    private static final String TAG = "MusicMediaPlayerGlue";
    private final Context mContext;
    private List<MediaMetaData> mMediaMetaDataList = new ArrayList<>();
    boolean mPendingServiceListUpdate = false; // flag indicating that mMediaMetaDataList is changed and
                                            // the media item list in the service needs to be updated
                                            // next time one of its APIs is used
    private MusicPlaybackService mPlaybackService;
    private boolean mServiceCallbackRegistered = false;
    private boolean mOnBindServiceHasBeenCalled = false;
    private boolean mStartPlayingAfterConnect = true;

    private ServiceConnection mPlaybackServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicPlaybackService.LocalBinder binder = (MusicPlaybackService.LocalBinder) iBinder;
            mPlaybackService = binder.getService();

            if (mPlaybackService.getCurrentMediaItem() == null) {
                if (mStartPlayingAfterConnect && mMediaMetaData != null) {
                    prepareAndPlay(mMediaMetaData);
                }
            }

            mPlaybackService.registerServiceCallback(MusicMediaPlayerGlue.this);
            mServiceCallbackRegistered = true;

            Log.d("MusicPlaybackService", "mPlaybackServiceConnection connected!");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("MusicPlaybackService", "mPlaybackServiceConnection disconnected!");
            mOnBindServiceHasBeenCalled = false;
            mPlaybackService = null;
            // update UI before the service disconnects. This should presumably happen after the
            // activity has called onStop. If the playback service finishes and stops, and when the user
            // returns to the playback from home screen, the play status and progress bar UIs could
            // be outdated and the activity may not connect to the service for a while. So we update
            // UI here for the playback state to be up-to-date once the user returns to the activity.
            onMediaStateChanged(-1);
        }
    };

    public MusicMediaPlayerGlue(Context context) {
        super(context);
        mContext = context;

        startAndBindToServiceIfNeeded();
    }

    public boolean isPlaybackServiceConnected() {
        return mPlaybackService != null;
    }

    private void startAndBindToServiceIfNeeded() {
        if (mOnBindServiceHasBeenCalled) {
            return;
        }
        // setting this flag to true so that media item list is repopulated once this activity
        // connects to a fresh copy of the playback service
        mPendingServiceListUpdate = true;
        // Bind to MusicPlaybackService
        Intent serviceIntent = new Intent(mContext, MusicPlaybackService.class);
        mContext.startService(serviceIntent);
        mContext.bindService(serviceIntent, mPlaybackServiceConnection, 0);
        mOnBindServiceHasBeenCalled = true;
    }

    @Override
    public void onCurrentItemChanged(MediaMetaData currentMediaItem) {
        if (mPlaybackService == null) {
            // onMetadataChanged updates both the metadata info on the player as well as the progress bar
            onMetadataChanged();
            return;
        }
        setMediaMetaData(currentMediaItem);
        if (mPlaybackService != null) {
            int repeatState = mPlaybackService.getRepeatState();
            int mappedActionIndex = mapServiceRepeatStateToActionIndex(repeatState);
            // if the activity's current repeat state differs from the service's, update it with the
            // repeatState of the service
            if (mRepeatAction.getIndex() != mappedActionIndex) {
                mRepeatAction.setIndex(mappedActionIndex);
                notifyItemChanged((ArrayObjectAdapter)
                        getControlsRow().getSecondaryActionsAdapter(), mRepeatAction);
            }
        }
    }

    @Override
    public void onMediaStateChanged(int currentMediaState) {
        onStateChanged();
    }

    public void openServiceCallback() {
        if (mPlaybackService != null && !mServiceCallbackRegistered) {
            mPlaybackService.registerServiceCallback(this);
            mServiceCallbackRegistered = true;
        }
    }

    public void releaseServiceCallback() {
        if (mPlaybackService != null && mServiceCallbackRegistered) {
            mPlaybackService.unregisterAll();
            mServiceCallbackRegistered = false;
        }
    }
    /**
     * Unbinds glue from the playback service. Called when the fragment is destroyed (pressing back)
     */
    public void close() {
        Log.d("MusicPlaybackService", "MusicMediaPlayerGlue closed!");
        mContext.unbindService(mPlaybackServiceConnection);
    }

    @Override public void onActionClicked(Action action) {
        // If either 'Shuffle' or 'Repeat' has been clicked we need to make sure the acitons index
        // is incremented and the UI updated such that we can display the new state.
        super.onActionClicked(action);
        if (action instanceof PlaybackControlsRow.RepeatAction) {
            int index = ((PlaybackControlsRow.RepeatAction) action).getIndex();
            if (mPlaybackService != null) {
                mPlaybackService.setRepeatState(mapActionIndexToServiceRepeatState(index));
            }
        }
    }

    @Override public boolean isMediaPlaying() {
        return (mPlaybackService != null) && mPlaybackService.isPlaying();
    }

    @Override public int getMediaDuration() {
        return (mPlaybackService != null) ? mPlaybackService.getDuration() : 0;
    }

    @Override public int getCurrentPosition() {
        return (mPlaybackService != null) ? mPlaybackService.getCurrentPosition() : 0;
    }

    @Override public void play(int speed) {
        prepareAndPlay(mMediaMetaData);
    }

    @Override public void pause() {
        if (mPlaybackService != null) {
            mPlaybackService.pause();
        }
    }

    public static int mapActionIndexToServiceRepeatState(int index) {
        if (index == PlaybackControlsRow.RepeatAction.ONE) {
            return MusicPlaybackService.MEDIA_ACTION_REPEAT_ONE;
        } else if (index == PlaybackControlsRow.RepeatAction.ALL) {
            return MusicPlaybackService.MEDIA_ACTION_REPEAT_ALL;
        } else {
            return MusicPlaybackService.MEDIA_ACTION_NO_REPEAT;
        }
    }

    public static int mapServiceRepeatStateToActionIndex(int serviceRepeatState) {
        if (serviceRepeatState == MusicPlaybackService.MEDIA_ACTION_REPEAT_ONE) {
            return PlaybackControlsRow.RepeatAction.ONE;
        } else if (serviceRepeatState == MusicPlaybackService.MEDIA_ACTION_REPEAT_ALL) {
            return PlaybackControlsRow.RepeatAction.ALL;
        } else {
            return PlaybackControlsRow.RepeatAction.NONE;
        }
    }

    @Override
    public void next() {
        if (mPlaybackService != null) {
            mPlaybackService.next();
        }
    }

    @Override
    public void previous() {
        if (mPlaybackService != null) {
            mPlaybackService.previous();
        }
    }

    public void setMediaMetaDataList(List<MediaMetaData> mediaMetaDataList) {
        mMediaMetaDataList.clear();
        mMediaMetaDataList.addAll(mediaMetaDataList);
        mPendingServiceListUpdate = true;
        if (mPlaybackService != null) {
            mPlaybackService.setMediaItemList(mMediaMetaDataList, false);
            mPendingServiceListUpdate = false;
        }
    }

    public void prepareAndPlay(Uri uri) {
        for (int i = 0; i < mMediaMetaDataList.size(); i++) {
            MediaMetaData mediaData = mMediaMetaDataList.get(i);
            if (mediaData.getMediaSourceUri().equals(uri)) {
                prepareAndPlay(mediaData);
                return;
            }
        }
    }

    public void prepareAndPlay(MediaMetaData mediaMetaData) {
        if (mediaMetaData == null) {
            throw new RuntimeException("Provided uri is null!");
        }
        startAndBindToServiceIfNeeded();
        mMediaMetaData = mediaMetaData;
        if (mPlaybackService == null) {
            // This media item is saved (mMediaMetaData) and later played when the
            // connection channel is established.
            mStartPlayingAfterConnect = true;
            return;
        }
        if (mPendingServiceListUpdate) {
            mPlaybackService.setMediaItemList(mMediaMetaDataList, false);
            mPendingServiceListUpdate = false;
        }
        mPlaybackService.playMediaItem(mediaMetaData);
    }

}
