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

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Holds the meta data such as media title, artist and cover art. It'll be used by the {@link
 * MediaPlayerGlue} for passing media item information between app and {@link MediaPlayerGlue}, and
 * between {@link MusicMediaPlayerGlue} and {@link MusicPlaybackService}.
 */
public class MediaMetaData implements Parcelable{

    private Uri mMediaSourceUri = null;
    private String mMediaSourcePath;
    private String mMediaTitle;
    private String mMediaArtistName;
    private String mMediaAlbumName;
    private int mMediaAlbumArtResId;
    private String mMediaAlbumArtUrl;

    MediaMetaData(Uri mediaSourceUri, String mediaSourcePath, String mediaTitle,
                  String mediaArtistName, String mediaAlbumName, int mediaAlbumArtResId,
                  String mediaAlbumArtUrl) {
        mMediaSourceUri = mediaSourceUri;
        mMediaSourcePath = mediaSourcePath;
        mMediaTitle = mediaTitle;
        mMediaArtistName = mediaArtistName;
        mMediaAlbumName = mediaAlbumName;
        mMediaAlbumArtResId = mediaAlbumArtResId;
        mMediaAlbumArtUrl = mediaAlbumArtUrl;
    }

    public MediaMetaData() {
    }

    public MediaMetaData(Parcel in) {
        mMediaSourceUri = in.readParcelable(null);
        mMediaSourcePath = in.readString();
        mMediaTitle = in.readString();
        mMediaArtistName = in.readString();
        mMediaAlbumName = in.readString();
        mMediaAlbumArtResId = in.readInt();
        mMediaAlbumArtUrl = in.readString();
    }


    public Uri getMediaSourceUri() {
        return mMediaSourceUri;
    }

    public void setMediaSourceUri(Uri mediaSourceUri) {
        mMediaSourceUri = mediaSourceUri;
    }

    public String getMediaSourcePath() {
        return mMediaSourcePath;
    }

    public void setMediaSourcePath(String mediaSourcePath) {
        mMediaSourcePath = mediaSourcePath;
    }

    public String getMediaTitle() {
        return mMediaTitle;
    }

    public void setMediaTitle(String mediaTitle) {
        mMediaTitle = mediaTitle;
    }

    public String getMediaArtistName() {
        return mMediaArtistName;
    }

    public void setMediaArtistName(String mediaArtistName) {
        mMediaArtistName = mediaArtistName;
    }

    public String getMediaAlbumName() {
        return mMediaAlbumName;
    }

    public void setMediaAlbumName(String mediaAlbumName) {
        mMediaAlbumName = mediaAlbumName;
    }


    public int getMediaAlbumArtResId() {
        return mMediaAlbumArtResId;
    }

    public void setMediaAlbumArtResId(int mediaAlbumArtResId) {
        mMediaAlbumArtResId = mediaAlbumArtResId;
    }

    public String getMediaAlbumArtUrl() {
        return mMediaAlbumArtUrl;
    }

    public void setMediaAlbumArtUrl(String mediaAlbumArtUrl) {
        mMediaAlbumArtUrl = mediaAlbumArtUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mMediaSourceUri, flags);
        dest.writeString(mMediaSourcePath);
        dest.writeString(mMediaTitle);
        dest.writeString(mMediaArtistName);
        dest.writeString(mMediaAlbumName);
        dest.writeInt(mMediaAlbumArtResId);
        dest.writeString(mMediaAlbumArtUrl);
    }

    public static final Parcelable.Creator<MediaMetaData> CREATOR = new Creator<MediaMetaData>() {
        @Override
        public MediaMetaData createFromParcel(Parcel parcel) {
            return new MediaMetaData(parcel);
        }

        @Override
        public MediaMetaData[] newArray(int size) {
            return new MediaMetaData[size];
        }
    };

}
