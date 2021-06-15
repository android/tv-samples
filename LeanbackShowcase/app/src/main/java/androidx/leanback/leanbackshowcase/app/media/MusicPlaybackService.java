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

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.leanback.leanbackshowcase.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Music service that handles all the interactions between the app and the media player. It receives
 * media list from an app and starts playing the media items one after another. Apps can also directly
 * interact with the service for specific operations such as playing or pausing or getting different
 * info about the current media item.
 */
public class MusicPlaybackService extends Service {

    // The ID used for for the notification. This is purely for making the service run as a
    // foreground service
    final int NOTIFICATION_ID = 1;
    final String NOW_PLAYING_CHANNEL = "androidx.leanback.leanbackshowcase.app.media.NOW_PLAYING";
    NotificationCompat.Builder mNotificationBuilder = null;

    public static final int MEDIA_ACTION_NO_REPEAT = 0;
    public static final int MEDIA_ACTION_REPEAT_ONE = 1;
    public static final int MEDIA_ACTION_REPEAT_ALL = 2;

    private AudioManager mAudioManager;
    private int mRepeatState = MEDIA_ACTION_NO_REPEAT;

    private MediaPlayer mPlayer;
    // MediaSession created for communication between NowPlayingCard in the launcher and the current MediaPlayer state
    private MediaSessionCompat mMediaSession;

    private static final String TAG = "MusicPlaybackService";
    int mCurrentMediaPosition = -1;
    int mCurrentMediaState = -1;
    MediaMetaData mCurrentMediaItem;
    List<MediaMetaData> mMediaItemList = new ArrayList<>();
    private boolean mInitialized = false; // true when the MediaPlayer is prepared/initialized

    private static final int FOCUS_CHANGE = 2;
    private NotificationManager mNotificationManager;


    private Handler mMediaPlayerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FOCUS_CHANGE:
                    switch (msg.arg1)
                    {
                        case AudioManager.AUDIOFOCUS_LOSS:
                            if (mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener) !=
                                    AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                                Log.w(TAG, "abandonAudioFocus after AudioFocus_LOSS failed!");
                            }
                            pause();
                            Log.d(TAG, "AudioFocus: Received AUDIOFOCUS_LOSS.");
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            Log.d(TAG, "AudioFocus: Received AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK.");
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            Log.d(TAG, "AudioFocus: Received AUDIOFOCUS_LOSS_TRANSIENT.");
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN:
                            Log.d(TAG, "AudioFocus: Received AUDIOFOCUS_GAIN.");
                            break;
                    }
                    break;
            }
        }
    };

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new
            AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            mMediaPlayerHandler.obtainMessage(FOCUS_CHANGE, focusChange, 0).sendToTarget();
        }
    };

    public interface ServiceCallback {
        void onMediaStateChanged(int currentMediaState);
        void onCurrentItemChanged(MediaMetaData currentMediaItem);
    }

    private List<ServiceCallback> mServiceCallbacks = new ArrayList<>();

    public void registerServiceCallback(ServiceCallback serviceCallback) {
        if (serviceCallback == null) {
            throw new IllegalArgumentException("The provided service callback is null.");
        }
        mServiceCallbacks.add(serviceCallback);
        if (mCurrentMediaItem != null) {
            // Calling onMediaStateChangedByPlaybackService on the callback to update UI on the
            // activity if they are out of sync with the playback service state.
            serviceCallback.onCurrentItemChanged(mCurrentMediaItem);
            serviceCallback.onMediaStateChanged(mCurrentMediaState);
        }
    }

    public void unregisterServiceCallback(ServiceCallback serviceCallback) {
        if (serviceCallback == null) {
            throw new IllegalArgumentException("The provided service callback is null.");
        }
        mServiceCallbacks.remove(serviceCallback);
        stopServiceIfNeeded();
    }

    public void unregisterAll() {
        mServiceCallbacks.clear();
        stopServiceIfNeeded();
    }

    private void stopServiceIfNeeded() {
        if (mServiceCallbacks.size() == 0
                && mCurrentMediaState == MediaUtils.MEDIA_STATE_MEDIALIST_COMPLETED) {
            Log.d(TAG, "stop " + MusicPlaybackService.this);
            stopSelf();
        }
    }

    private void notifyMediaStateChanged(int currentMediaState) {
        mCurrentMediaState = currentMediaState;
        for(int i = mServiceCallbacks.size() - 1; i >= 0; i--) {
            mServiceCallbacks.get(i).onMediaStateChanged(currentMediaState);
        }
    }

    private void notifyMediaItemChanged(MediaMetaData currentMediaItem) {
        mCurrentMediaItem = currentMediaItem;
        for(int i = mServiceCallbacks.size() - 1; i >= 0; i--) {
            mServiceCallbacks.get(i).onCurrentItemChanged(mCurrentMediaItem);
        }
    }

    // Binder given to clients of this service
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        MusicPlaybackService getService() {
            return MusicPlaybackService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        if (mMediaSession == null) {
            mMediaSession = new MediaSessionCompat(this, "MusicPlayer Session");
            mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mMediaSession.setCallback(new MediaSessionCallback());
            updateMediaSessionIntent();
        }
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setUpAsForeground("This Awesome Music Service :)");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public void setMediaItemList(List<MediaMetaData> mediaItemList, boolean isQueue) {
        if (!isQueue) {
            mMediaItemList.clear();
        }
        mMediaItemList.addAll(mediaItemList);

    }

    public void setRepeatState(int repeatState) {
        mRepeatState = repeatState;
    }

    public int getRepeatState() {
        return mRepeatState;
    }

    public void playMediaItem(MediaMetaData mediaItemToPlay) {
        if (mediaItemToPlay == null) {
            throw new IllegalArgumentException("mediaItemToPlay is null!");
        }
        int mediaItemPos = findMediaItemPosition(mediaItemToPlay);
        if (mediaItemPos == -1) {
            throw new IllegalArgumentException("mediaItemToPlay not found in the media item list!");
        }

        if (mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN) != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.e(TAG, "playMediaItem cannot obtain audio focus!");
            return;
        }

        if (!mMediaSession.isActive()) {
            mMediaSession.setActive(true);
        }

        if (mCurrentMediaItem != null && mInitialized &&
                mCurrentMediaItem.getMediaSourceUri().equals(mediaItemToPlay.getMediaSourceUri())) {
            if (!isPlaying()) {
                // This media item had been already playing but is being paused. Will resume the player.
                // No need to reset the player
                play();
            }
        } else {
            mCurrentMediaPosition = mediaItemPos;
            notifyMediaItemChanged(mediaItemToPlay);
            prepareNewMedia();
        }
    }

    int findMediaItemPosition(MediaMetaData mediaItem) {
        for(int i = 0; i < mMediaItemList.size(); i++) {
            if (mMediaItemList.get(i).getMediaSourceUri().equals(mediaItem.getMediaSourceUri())) {
                return i;
            }
        }
        return -1;
    }

    private void prepareNewMedia() {
        reset();
        try {
            mPlayer.setDataSource(getApplicationContext(),
                    mCurrentMediaItem.getMediaSourceUri());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override public void onPrepared(MediaPlayer mp) {
                updateMediaSessionMetaData();
                mInitialized = true;
                play();
            }
        });
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                Log.e(TAG, "Error: what=" + what + ", extra=" + extra);
                return true;
            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override public void onCompletion(MediaPlayer mp) {
                updateMediaSessionPlayState();
                if (mRepeatState == MEDIA_ACTION_REPEAT_ALL &&
                        mCurrentMediaPosition == mMediaItemList.size() - 1) {
                    // The last media item is played and repeatAll action is enabled;
                    // start over the media list from the beginning
                    mCurrentMediaPosition = 0;
                    notifyMediaItemChanged(mMediaItemList.get(mCurrentMediaPosition));
                    prepareNewMedia();
                } else if (mRepeatState == MEDIA_ACTION_REPEAT_ONE) {
                    // repeat playing the same media item
                    prepareNewMedia();
                } else if (mCurrentMediaPosition < mMediaItemList.size() - 1) {
                    // Move on to playing the next media item in the list
                    mCurrentMediaPosition++;
                    notifyMediaItemChanged(mMediaItemList.get(mCurrentMediaPosition));
                    prepareNewMedia();
                } else {
                    // Last media item is reached, and the service is no longer necessary;
                    // Stop the service after some delay, since the service might need to stay alive
                    // for some time for the cleanup (such as updating the progress bar during the
                    // final seconds).
                    notifyMediaStateChanged(MediaUtils.MEDIA_STATE_MEDIALIST_COMPLETED);
                    stopServiceIfNeeded();

                }
            }
        });
        mPlayer.prepareAsync();
        notifyMediaStateChanged(MediaUtils.MEDIA_STATE_PREPARING);
    }

    private void updateMediaSessionMetaData() {
        if (mCurrentMediaItem == null) {
            throw new IllegalArgumentException(
                    "mCurrentMediaItem is null in updateMediaSessionMetaData!");
        }
        MediaMetadataCompat.Builder metaDataBuilder = new MediaMetadataCompat.Builder();
        if (mCurrentMediaItem.getMediaTitle() != null) {
            metaDataBuilder.putString(MediaMetadata.METADATA_KEY_TITLE,
                    mCurrentMediaItem.getMediaTitle());
        }
        if (mCurrentMediaItem.getMediaAlbumName() != null) {
            metaDataBuilder.putString(MediaMetadata.METADATA_KEY_ALBUM,
                    mCurrentMediaItem.getMediaAlbumName());
        }
        if (mCurrentMediaItem.getMediaArtistName() != null) {
            metaDataBuilder.putString(MediaMetadata.METADATA_KEY_ARTIST,
                    mCurrentMediaItem.getMediaArtistName());
        }
        if (mCurrentMediaItem.getMediaAlbumArtResId() != 0) {
            Bitmap albumArtBitmap = BitmapFactory.decodeResource(getResources(),
                    mCurrentMediaItem.getMediaAlbumArtResId());
            metaDataBuilder.putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, albumArtBitmap);
        }
        mMediaSession.setMetadata(metaDataBuilder.build());
    }

    private void updateMediaSessionPlayState() {
        PlaybackStateCompat.Builder playbackStateBuilder = new PlaybackStateCompat.Builder();
        int playState;
        if (isPlaying()) {
            playState = PlaybackStateCompat.STATE_PLAYING;
        } else {
            playState = PlaybackStateCompat.STATE_PAUSED;
        }
        long currentPosition = getCurrentPosition();
        playbackStateBuilder.setState(playState, currentPosition, (float) 1.0).setActions(
                getPlaybackStateActions()
        );
        mMediaSession.setPlaybackState(playbackStateBuilder.build());
    }

    /**
     * Sets the media session's activity launched when clicking on NowPlayingCard. This returns to
     * the media screen that is playing or paused; the launched activity corresponds to the
     * currently shown media session in the NowPlayingCard on TV launcher.
     */
    private void updateMediaSessionIntent() {
        if (mMediaSession == null) {
            return;
        }
        Intent nowPlayIntent = new Intent(getApplicationContext(), MusicExampleActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, nowPlayIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mMediaSession.setSessionActivity(pi);
    }

    /**
     * @return The available set of actions for the media session. These actions should be provided
     * for the MediaSession PlaybackState in order for
     * {@link MediaSessionCompat.Callback#onMediaButtonEvent} to call relevant methods of onPause() or
     * onPlay().
     */
    private long getPlaybackStateActions() {
        return PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE |
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
    }

    void reset() {
        if (mPlayer != null) {
            mPlayer.reset();
            mInitialized = false;
        }
    }

    /**
     * starts playback of the previously opened media file
     */
    public void play() {
        if (mPlayer != null && mInitialized && !isPlaying()) {
            mPlayer.start();
            updateMediaSessionPlayState();
            notifyMediaStateChanged(MediaUtils.MEDIA_STATE_PLAYING);
        }
    }

    /**
     * pauses playback (call play() to resume)
     */
    public void pause() {
        if (mPlayer != null && mPlayer.isPlaying()){
            mPlayer.pause();
            updateMediaSessionPlayState();
            notifyMediaStateChanged(MediaUtils.MEDIA_STATE_PAUSED);
        }
    }

    /**
     * skip to next item
     */
    public void next() {
        if (mMediaItemList.size() == 0) {
            return;
        }
        if (mCurrentMediaPosition == mMediaItemList.size() - 1) {
            // The last media item is played and repeatAll action is enabled;
            // start over the media list from the beginning
            mCurrentMediaPosition = 0;
        } else {
            // Move on to playing the next media item in the list
            mCurrentMediaPosition++;
        }
        notifyMediaItemChanged(mMediaItemList.get(mCurrentMediaPosition));
        prepareNewMedia();
    }

    /**
     * skip to previous item
     */
    public void previous() {
        if (mMediaItemList.size() == 0) {
            return;
        }
        if (mCurrentMediaPosition == 0) {
            // The last media item is played and repeatAll action is enabled;
            // start over the media list from the beginning
            mCurrentMediaPosition = mMediaItemList.size() - 1;
        } else {
            // Move on to playing the next media item in the list
            mCurrentMediaPosition--;
        }
        notifyMediaItemChanged(mMediaItemList.get(mCurrentMediaPosition));
        prepareNewMedia();
    }

    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    /**
     * @return The current playback position in milliseconds.
     */
    public int getCurrentPosition() {
        if (mInitialized && mPlayer != null) {
            return mPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * @return The duration of the current media item in milliseconds
     */
    public int getDuration() {
        return (mInitialized && mPlayer != null) ? mPlayer.getDuration() : 0;
    }

    public MediaMetaData getCurrentMediaItem() {
        return mInitialized ? mCurrentMediaItem : null;
    }

    /**
     * Seeks to the given new position in milliseconds of the current media item
     * @param newPosition The new position of the current media item in milliseconds
     */
    public void seekTo(int newPosition) {
        if (mPlayer != null) {
            mPlayer.seekTo(newPosition);
        }
    }

    /**
     * Configures service as a foreground service.
     */
    void setUpAsForeground(String text) {
        Intent notificationIntent = new Intent(this, MusicExampleActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (shouldCreateNowPlayingChannel()) {
            createNowPlayingChannel();
        }


        // Build the notification object.
        mNotificationBuilder = new NotificationCompat.Builder(getApplicationContext(), NOW_PLAYING_CHANNEL)
                .setSmallIcon(R.drawable.ic_favorite_border_white_24dp)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("RandomMusicPlayer")
                .setContentText(text)
                .setContentIntent(pi)
                .setOngoing(true);

        startForeground(NOTIFICATION_ID, mNotificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNowPlayingChannel() {
        NotificationChannel channel = new NotificationChannel(NOW_PLAYING_CHANNEL, getString(R.string.now_playing_channel_name), NotificationManager.IMPORTANCE_LOW);
        channel.setDescription(getString(R.string.now_playing_channel_description));
        getNotificationManager().createNotificationChannel(channel);
    }

    private Boolean shouldCreateNowPlayingChannel() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !notificationChannelExists();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Boolean notificationChannelExists() {
        return getNotificationManager().getNotificationChannel(NOW_PLAYING_CHANNEL) != null;
    }

    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        mMediaPlayerHandler.removeCallbacksAndMessages(null);
        if (mPlayer != null) {
            // stop and release the media player since it's no longer in use
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
        if (mMediaSession != null) {
            mMediaSession.release();
            mMediaSession = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonIntent) {
            KeyEvent keyEvent = mediaButtonIntent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            Log.d(TAG, "onMediaButtonEvent in MediaSessionCallback called with event: " + keyEvent);
            return super.onMediaButtonEvent(mediaButtonIntent);
        }

        @Override
        public void onPlay() {
            play();
        }

        @Override
        public void onPause() {
            pause();
        }

        @Override
        public void onSkipToNext() {
            next();
        }

        @Override
        public void onSkipToPrevious() {
            previous();
        }

        @Override
        public void onFastForward() {
            super.onFastForward();
        }

        @Override
        public void onRewind() {
            super.onRewind();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onSeekTo(long pos) {
            seekTo((int)pos);
        }
    }
}
