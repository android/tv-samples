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

package androidx.leanback.leanbackshowcase.app.room.api;

import androidx.leanback.leanbackshowcase.app.room.db.constant.GsonConstant;
import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * The structure of the json file is
 * {
 *   "googlevideos": [{
 *     "category": "Google+",
 *     "videos": [{
 *       "description": "",
 *          ...
 *     }]
 *   }]
 * }
 *
 * So this class is used as a helper class for Gson library to reconstruct the object.
 *
 * It also contains a static inner class VideosGroupByCategory. So we can deserialize the video
 * contents in that inside the google tag.
 */
public class VideosWithGoogleTag {
    @SerializedName(GsonConstant.GOOGLE_VIDEO_TAG)
    private List<VideosGroupByCategory> mAllResources;

    public List<VideosGroupByCategory> getAllResources() {
        return mAllResources;
    }

    /**
     * Helper class for Gson to deserialize the json structure
     * [{
     *     "category": "Google+",
     *     "videos": [{
     *       "description": "",
     *          ...
     *     }]
     * }]
     */
    public static class VideosGroupByCategory {
        @SerializedName("category")
        private String mCategory;

        @SerializedName("videos")
        private List<VideoEntity> mVideos;

        public String getCategory() {
            return mCategory;
        }

        public List<VideoEntity> getVideos() {
            return mVideos;
        }
    }

}
