/*
 * Copyright (C) 2017 The Android Open Source Project
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

package androidx.leanback.leanbackshowcase.app.rows;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.tvprovider.media.tv.TvContractCompat;
import androidx.tvprovider.media.tv.BasePreviewProgram.AspectRatio;

import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * VideoContent class
 * <p>
 * Parcelable interface has been implemented, so it can be passed between different components
 */
public class VideoContent implements Parcelable {

    /**
     * The description of this video
     */
    @SerializedName("description") private String mDescription;

    /**
     * The url of video
     */
    @SerializedName("source") private String mVideoUrl;

    /**
     * The url of preview video
     */
    @SerializedName("preview") private String mPreviewVideoUrl;

    /**
     * The category (Used as the name for the channel which contains this video)
     */
    @SerializedName("category") private String mCategory;

    /**
     * The title of this video
     */
    @SerializedName("title") private String mTitle;

    /**
     * This ID is basically for internal use, each video will have a unique ID associated with it
     * So different component in the application can use it to locate the unique video resource.
     */
    @SerializedName("videoId") private String mVideoId;

    /**
     * The url of the image shown on the card
     */
    @SerializedName("card") private String mCardImageUrl;

    /**
     * The background image url
     */
    @SerializedName("background") private String mBgImageUrl;

    /**
     * width/ height ratio for the card in the channel which has been added to main screen.
     *
     * In this sample app, it has been assigned to ASPECT_RATIO_16_9.
     */
    private int mAspectRatio = TvContractCompat.PreviewProgramColumns.ASPECT_RATIO_16_9;

    /**
     * This ID is generated from add this video to main screen operation, i.e. the unique
     * identification for the program in main screen.
     *
     * The purpose is to find the video which has been added to main screen.
     */
    private long mProgramId;

    /**
     * Getter and Setter for class's member
     *
     * Set access permission to public so other component outside of this package can access
     */

    public void setProgramId(long programId) {
        mProgramId = programId;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public String getPreviewVideoUrl() {
        return mPreviewVideoUrl;
    }

    public String getCardImageUrl() {
        return mCardImageUrl;
    }

    public URI getBackgroundImageURI() {
        try {
            return new URI(mBgImageUrl);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public @AspectRatio int getAspectRatio() {
        return mAspectRatio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mVideoId);
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeString(mBgImageUrl);
        dest.writeString(mCardImageUrl);
        dest.writeString(mVideoUrl);
        dest.writeString(mPreviewVideoUrl);
        dest.writeString(mCategory);
        dest.writeLong(mProgramId);
    }

    // Constructor to construct video content from parcel
    private VideoContent(Parcel in) {
        mVideoId = in.readString();
        mTitle = in.readString();
        mDescription = in.readString();
        mBgImageUrl = in.readString();
        mCardImageUrl = in.readString();
        mVideoUrl = in.readString();
        mPreviewVideoUrl = in.readString();
        mCategory = in.readString();
    }

    public static final Creator<VideoContent> CREATOR = new Creator<VideoContent>() {
        public VideoContent createFromParcel(Parcel in) {
            return new VideoContent(in);
        }

        public VideoContent[] newArray(int size) {
            return new VideoContent[size];
        }
    };

    /**
     * For debugging purpose
     */
    @Override
    public String toString() {
        return "VideoContent{" +
                "mDescription='" + mDescription + '\'' +
                ", mVideoUrl='" + mVideoUrl + '\'' +
                ", mPreviewVideoUrl='" + mPreviewVideoUrl + '\'' +
                ", mCategory='" + mCategory + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mVideoId='" + mVideoId + '\'' +
                ", mCardImageUrl='" + mCardImageUrl + '\'' +
                ", mBgImageUrl='" + mBgImageUrl + '\'' +
                ", mAspectRatio=" + mAspectRatio +
                ", mProgramId=" + mProgramId +
                '}';
    }
}
