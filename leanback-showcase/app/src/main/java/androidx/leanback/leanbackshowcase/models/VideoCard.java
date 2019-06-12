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
 */

package androidx.leanback.leanbackshowcase.models;


import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.leanback.leanbackshowcase.R;

/**
 * The video card data structure used to hold the fields of each video card fetched from the
 * url: {@link R.string.videos_url}
 */
public class VideoCard extends Card {

    @SerializedName("sources") private List<String> mVideoSources = null;
    @SerializedName("background") private String mBackgroundUrl = "";
    @SerializedName("studio") private String mStudio = "";

    public VideoCard() {
        super();
        setType(Type.VIDEO_GRID);
    }

    public List<String> getVideoSources() {
        return mVideoSources;
    }

    public void setVideoSources(List<String> sources) {
        mVideoSources = sources;
    }

    public String getBackground() {
        return mBackgroundUrl;
    }

    public void setBackground(String background) {
        mBackgroundUrl = background;
    }

    public String getStudio() {
        return mStudio;
    }

    public void setStudio(String studio) {
        mStudio = studio;
    }
}
