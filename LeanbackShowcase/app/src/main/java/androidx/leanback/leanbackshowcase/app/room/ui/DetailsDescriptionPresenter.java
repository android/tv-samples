/*
 * Copyright (C) 2014 The Android Open Source Project
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

package androidx.leanback.leanbackshowcase.app.room.ui;

import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;
import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder vh, Object item) {
        // If the bond item is a video item, will extract the information from the video directly

        if (item instanceof VideoEntity) {
            VideoEntity videoItem = (VideoEntity) item;
            if (((VideoEntity) item).isRented()) {

                String newTitle = videoItem.getTitle() + " (rented)";
                vh.getTitle().setText(newTitle);
            } else {
                vh.getTitle().setText(videoItem.getTitle());
            }


            if (isRemovable(videoItem)) {
                String newSubTitle = videoItem.getStudio() + " (downloaded)";
                vh.getSubtitle().setText(newSubTitle);
            } else if (!videoItem.getStatus().isEmpty() && !isDownloadable(videoItem)) {
                String newSubTitle = videoItem.getStudio() + " (" + videoItem.getStatus() + ")";
                vh.getSubtitle().setText(newSubTitle);
            } else {
                vh.getSubtitle().setText(videoItem.getStudio());
            }

            vh.getBody().setText(videoItem.getDescription());
        }
    }

    /**
     * When all the local storage paths (including video content, background and card image )
     * for the video entity is not empty, and current working status is not removing. It means
     * user can perform remove video entity operation at this point
     *
     * @return If user can perform remove video operation or not.
     */
    private boolean isRemovable(VideoEntity videoEntity) {
        return !videoEntity.getVideoCardImageLocalStorageUrl().isEmpty()
                && !videoEntity.getVideoBgImageLocalStorageUrl().isEmpty()
                && !videoEntity.getVideoLocalStorageUrl().isEmpty()
                && !videoEntity.getStatus().equals("removing");
    }

    /**
     * When all the local storage paths (including video content, background and card image )
     * for the video entity is empty, and current working status is not downloading. It means
     * user can perform download video entity operation at this point
     *
     * @return If user can perform download video operation or not.
     */
    private boolean isDownloadable(VideoEntity videoEntity) {
        return videoEntity.getVideoCardImageLocalStorageUrl().isEmpty()
                && videoEntity.getVideoBgImageLocalStorageUrl().isEmpty()
                && videoEntity.getVideoLocalStorageUrl().isEmpty()
                && !videoEntity.getStatus().equals("downloading") ;
    }
}
