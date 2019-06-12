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

package androidx.leanback.leanbackshowcase.app.room.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.leanback.leanbackshowcase.app.room.db.constant.DatabaseColumnConstant;
import androidx.leanback.leanbackshowcase.app.room.db.constant.GsonConstant;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = DatabaseColumnConstant.VideoEntry.TABLE_NAME)
public class VideoEntity implements  Parcelable {

    @SerializedName(GsonConstant.DESCRIPTION)
    @ColumnInfo(name = DatabaseColumnConstant.VideoEntry.COLUMN_DESC)
    private String mDescription;

    @SerializedName(GsonConstant.VIDEO_URLS)
    @Ignore
    private List<String> mVideoUrls;

    @SerializedName(GsonConstant.CARD_IMAGE_URL)
    @ColumnInfo(name = DatabaseColumnConstant.VideoEntry.COLUMN_CARD_IMAGE_URL)
    private String mCardImageUrl;

    @SerializedName(GsonConstant.BACKGROUND_IMAGE_URL)
    @ColumnInfo(name = DatabaseColumnConstant.VideoEntry.COLUMN_BG_IMAGE_URL)
    private String mBgImageUrl;

    @SerializedName(GsonConstant.TITLE)
    @ColumnInfo(name = DatabaseColumnConstant.VideoEntry.COLUMN_NAME)
    private String mTitle;

    @SerializedName(GsonConstant.STUDIO)
    @ColumnInfo(name = DatabaseColumnConstant.VideoEntry.COLUMN_STUDIO)
    private String mStudio;

    /**
     * The following field cannot be instantiate from json file.
     */
    // Primary key to store the video item.
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DatabaseColumnConstant.VideoEntry.COLUMN_AUTO_GENERATE_ID)
    private long mId;

    // The url points to the local storage of the video content.
    @ColumnInfo(name = DatabaseColumnConstant.VideoEntry.COLUMN_VIDEO_CACHE)
    private String mVideoLocalStorageUrl;

    // The url points to the local storage of the video background image.
    @ColumnInfo(name = DatabaseColumnConstant.VideoEntry.COLUMN_BG_IMAGE_CACHE)
    private String mVideoBgImageLocalStorageUrl;

    // The url points to the local storage of the video card image.
    @ColumnInfo(name = DatabaseColumnConstant.VideoEntry.COLUMN_CARD_IMG_CACHE)
    private String mVideoCardImageLocalStorageUrl;

    // The mCategory of current video
    // A single mCategory may contain many different videos
    @ColumnInfo(name = DatabaseColumnConstant.VideoEntry.COLUMN_CATEGORY)
    private String mCategory;

    // The first url in urls array
    @ColumnInfo(name = DatabaseColumnConstant.VideoEntry.COLUMN_VIDEO_URL)
    private String mVideoUrl;

    public String getTrailerVideoUrl() {
        return mTrailerVideoUrl;
    }

    public void setTrailerVideoUrl(String mTrailerVideoUrl) {
        this.mTrailerVideoUrl = mTrailerVideoUrl;
    }

    // The first url in urls array
    @ColumnInfo(name = DatabaseColumnConstant.VideoEntry.COLUMN_VIDEO_TRAILER_URL)
    private String mTrailerVideoUrl;

    @ColumnInfo(name = DatabaseColumnConstant.VideoEntry.COLUMN_VIDEO_IS_RENTED)
    private boolean mRented;

    @ColumnInfo(name = DatabaseColumnConstant.VideoEntry.COLUMN_VIDEO_STATUS)
    private String mStatus;

    /**
     * The constructor is requried by room database
     */
    public VideoEntity() {
        // no op
    }

    /**
     * get status
     */
    public String getStatus() {
        return mStatus;
    }

    /**
     * set status
     */
    public void setStatus(String status) {
        this.mStatus = status;
    }


    /**
     * Is video entity rented or not
     */
    public boolean isRented() {
        return mRented;
    }

    /**
     * update video rented status
     */
    public void setRented(boolean mIsRented) {
        this.mRented = mIsRented;
    }

    /**
     * setId
     */
    public void setId(long id) {
        mId = id;
    }

    /**
     * getId
     */
    public long getId() {
        return mId;
    }

    /**
     * getCategory
     */
    public String getCategory() {
        return mCategory;
    }

    /**
     * setCategory
     */
    public void setCategory(String category) {
        this.mCategory = category;
    }

    /**
     * getVideoUrls
     */
    public List<String> getVideoUrls() {
        return mVideoUrls;
    }

    /**
     * setVideoUrls
     */
    public void setVideoUrls(List<String> videoUrls) {
        mVideoUrls = videoUrls;
    }

    /**
     * getDescription
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * setDescription
     */
    public void setDescription(String description) {
        this.mDescription = description;
    }

    /**
     * getVideoUrl
     */
    public String getVideoUrl() {
        return mVideoUrl;
    }

    /**
     * setVideoUrl
     */
    public void setVideoUrl(String videoUrl) {
        this.mVideoUrl = videoUrl;
    }

    /**
     * getCardImageUrl
     */
    public String getCardImageUrl() {
        return mCardImageUrl;
    }

    /**
     * setCardImageUrl
     */
    public void setCardImageUrl(String cardImageUrl) {
        this.mCardImageUrl = cardImageUrl;
    }

    /**
     * getBgImageUrl
     */
    public String getBgImageUrl() {
        return mBgImageUrl;
    }

    /**
     * setBgImageUrl
     */
    public void setBgImageUrl(String bgImageUrl) {
        this.mBgImageUrl = bgImageUrl;
    }

    /**
     * getTitle
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * setTitle
     */
    public void setTitle(String title) {
        this.mTitle = title;
    }

    /**
     * getStudio
     */
    public String getStudio() {
        return mStudio;
    }

    /**
     * setStudio
     */
    public void setStudio(String studio) {
        this.mStudio = studio;
    }

    /**
     * getVideoLocalStorageUrl
     */
    public String getVideoLocalStorageUrl() {
        return mVideoLocalStorageUrl;
    }

    /**
     * setVideoLocalStorageUrl
     */
    public void setVideoLocalStorageUrl(String videoLocalStorageUrl) {
        this.mVideoLocalStorageUrl = videoLocalStorageUrl;
    }

    /**
     * getVideoBgImageLocalStorageUrl
     */
    public String getVideoBgImageLocalStorageUrl() {
        return mVideoBgImageLocalStorageUrl;
    }

    /**
     * setVideoBgImageLocalStorageUrl
     */
    public void setVideoBgImageLocalStorageUrl(String videoBgImageLocalStorageUrl) {
        this.mVideoBgImageLocalStorageUrl = videoBgImageLocalStorageUrl;
    }

    /**
     * getVideoCardImageLocalStorageUrl
     */
    public String getVideoCardImageLocalStorageUrl() {
        return mVideoCardImageLocalStorageUrl;
    }

    /**
     * setVideoCardImageLocalStorageUrl
     */
    public void setVideoCardImageLocalStorageUrl(String videoCardImageLocalStorageUrl) {
        this.mVideoCardImageLocalStorageUrl = videoCardImageLocalStorageUrl;
    }

    // This constructor is needed since we will pass the video item between different activities.
    protected VideoEntity(Parcel in) {
        mId = in.readLong();
        mCategory = in.readString();
        mTitle = in.readString();
        mDescription = in.readString();
        mBgImageUrl = in.readString();
        mCardImageUrl = in.readString();
        mVideoUrl = in.readString();
        mStudio = in.readString();
        mVideoCardImageLocalStorageUrl = in.readString();
        mVideoBgImageLocalStorageUrl = in.readString();
        mVideoLocalStorageUrl = in.readString();
        mStatus = in.readString();
        mTrailerVideoUrl = in.readString();

        // Rented information (boolean) will be passed as a byte type
        mRented = in.readByte() != 0;
    }

    /**
     * Required by parcelable interface
     */
    public static final Parcelable.Creator<VideoEntity> CREATOR =
            new Parcelable.Creator<VideoEntity>() {

        @Override
        public VideoEntity createFromParcel(Parcel in) {
            return new VideoEntity(in);
        }

        @Override
        public VideoEntity[] newArray(int size) {
            return new VideoEntity[size];
        }
    };

    /**
     * Required by parcelable interface
     */
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mCategory);
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeString(mBgImageUrl);
        dest.writeString(mCardImageUrl);
        dest.writeString(mVideoUrl);
        dest.writeString(mStudio);
        dest.writeString(mVideoLocalStorageUrl);
        dest.writeString(mVideoBgImageLocalStorageUrl);
        dest.writeString(mVideoCardImageLocalStorageUrl);
        dest.writeString(mStatus);
        dest.writeString(mTrailerVideoUrl);

        // Rented information (boolean) will be passed as a byte type
        dest.writeByte((byte) (mRented ? 1:0));
    }

    /**
     * For debugging
     *
     * @return video entity all fields description
     */
    @Override
    public String toString() {
        return "VideoEntity{" +
                "mDescription='" + mDescription + '\'' +
                ", mVideoUrls=" + mVideoUrls +
                ", mCardImageUrl='" + mCardImageUrl + '\'' +
                ", mBgImageUrl='" + mBgImageUrl + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mStudio='" + mStudio + '\'' +
                ", mId=" + mId +
                ", mVideoLocalStorageUrl='" + mVideoLocalStorageUrl + '\'' +
                ", mVideoBgImageLocalStorageUrl='" + mVideoBgImageLocalStorageUrl + '\'' +
                ", mVideoCardImageLocalStorageUrl='" + mVideoCardImageLocalStorageUrl + '\'' +
                ", mCategory='" + mCategory + '\'' +
                ", mVideoUrl='" + mVideoUrl + '\'' +
                ", mTrailerVideoUrl='" + mTrailerVideoUrl + '\'' +
                ", mRented=" + mRented +
                ", mStatus='" + mStatus + '\'' +
                '}';
    }

    /**
     * Used to determine if the two video entity has the same content or not
     *
     * @param o another video entity
     * @return If those two video entitiy has the same content or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VideoEntity that = (VideoEntity) o;

        if (mId != that.mId) return false;

        if (mRented != that.mRented) return false;

        if (mDescription != null ? !mDescription.equals(that.mDescription) :
                that.mDescription != null)
            return false;

        if (mVideoUrls != null ? !mVideoUrls.equals(that.mVideoUrls) : that.mVideoUrls != null)
            return false;

        if (mCardImageUrl != null ? !mCardImageUrl.equals(that.mCardImageUrl) :
                that.mCardImageUrl != null)
            return false;

        if (mBgImageUrl != null ? !mBgImageUrl.equals(that.mBgImageUrl) : that.mBgImageUrl != null)
            return false;

        if (mTitle != null ? !mTitle.equals(that.mTitle) : that.mTitle != null) return false;

        if (mStudio != null ? !mStudio.equals(that.mStudio) : that.mStudio != null) return false;

        if (mVideoLocalStorageUrl != null ? !mVideoLocalStorageUrl.equals(
                that.mVideoLocalStorageUrl)
                : that.mVideoLocalStorageUrl != null)
            return false;

        if (mVideoBgImageLocalStorageUrl != null ? !mVideoBgImageLocalStorageUrl.equals(
                that.mVideoBgImageLocalStorageUrl) : that.mVideoBgImageLocalStorageUrl != null)
            return false;

        if (mVideoCardImageLocalStorageUrl != null ? !mVideoCardImageLocalStorageUrl.equals(
                that.mVideoCardImageLocalStorageUrl) : that.mVideoCardImageLocalStorageUrl != null)
            return false;

        if (mCategory != null ? !mCategory.equals(that.mCategory) : that.mCategory != null)
            return false;

        if (mVideoUrl != null ? !mVideoUrl.equals(that.mVideoUrl) : that.mVideoUrl != null)
            return false;

        return mStatus != null ? mStatus.equals(that.mStatus) : that.mStatus == null;
    }

    @Override
    public int hashCode() {
        int result = mDescription != null ? mDescription.hashCode() : 0;
        result = 31 * result + (mVideoUrls != null ? mVideoUrls.hashCode() : 0);
        result = 31 * result + (mCardImageUrl != null ? mCardImageUrl.hashCode() : 0);
        result = 31 * result + (mBgImageUrl != null ? mBgImageUrl.hashCode() : 0);
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + (mStudio != null ? mStudio.hashCode() : 0);
        result = 31 * result + (int) (mId ^ (mId >>> 32));
        result = 31 * result + (mVideoLocalStorageUrl != null ? mVideoLocalStorageUrl.hashCode() : 0);
        result = 31 * result + (mVideoBgImageLocalStorageUrl != null ? mVideoBgImageLocalStorageUrl.hashCode() : 0);
        result = 31 * result + (mVideoCardImageLocalStorageUrl != null ? mVideoCardImageLocalStorageUrl.hashCode() : 0);
        result = 31 * result + (mCategory != null ? mCategory.hashCode() : 0);
        result = 31 * result + (mVideoUrl != null ? mVideoUrl.hashCode() : 0);
        result = 31 * result + (mRented ? 1 : 0);
        result = 31 * result + (mStatus != null ? mStatus.hashCode() : 0);
        return result;
    }
}
