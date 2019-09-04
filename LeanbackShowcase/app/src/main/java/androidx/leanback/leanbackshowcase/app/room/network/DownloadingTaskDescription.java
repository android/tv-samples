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

package androidx.leanback.leanbackshowcase.app.room.network;

import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;

public class DownloadingTaskDescription {
    private VideoEntity mVideo;
    private String mCategory;
    private String storagePath;

    public DownloadingTaskDescription(VideoEntity video, String category) {
        mVideo = video;
        mCategory = category;
    }

    public VideoEntity getVideo() {
        return mVideo;
    }


    public String getCategory() {
        return mCategory;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }
}

