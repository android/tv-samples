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

package androidx.leanback.leanbackshowcase.app.room.db.constant;


public class DatabaseColumnConstant {

    /**
     * Define the name of column in video entry
     */
    public static final class VideoEntry {

        // Name of the video table.
        public static final String TABLE_NAME = "videos";

        // Name of auto generated id name
        public static final String COLUMN_AUTO_GENERATE_ID = "_id";

        // Column with the foreign key into the category table.
        public static final String COLUMN_CATEGORY = "category";

        // Name of the video.
        public static final String COLUMN_NAME = "video_name";

        // Description of the video.
        public static final String COLUMN_DESC = "video_description";

        // The url to the video content.
        public static final String COLUMN_VIDEO_URL = "video_url";

        // The url to the video content.
        public static final String COLUMN_VIDEO_TRAILER_URL = "trailer_video_url";

        // The url to the video content.
        public static final String COLUMN_VIDEO_IS_RENTED = "video_is_rented";


        // The url to the background image.
        public static final String COLUMN_BG_IMAGE_URL = "bg_image_url";

        // The card image for the video.
        public static final String COLUMN_CARD_IMAGE_URL = "card_image_url";

        // The studio name.
        public static final String COLUMN_STUDIO = "studio";

        // The uri to the downloaded video resource.
        public static final String COLUMN_VIDEO_CACHE = "video_downloaded_uri";

        // The uri to the downloaded background image resource.
        public static final String COLUMN_BG_IMAGE_CACHE = "bg_image_downloaded_uri";

        // The uri to the downloaded card image resource.
        public static final String COLUMN_CARD_IMG_CACHE = "card_image_downloaded_uri";

        public static final String COLUMN_VIDEO_STATUS = "working_status";
    }

    /**
     * Define the name of column in category entry
     */
    public static final class CategoryEntry {

        // Name of the category table.
        public static final String TABLE_NAME = "categories";

        // Name of auto generated id name
        public static final String COLUMN_AUTOGENERATE_ID = "_id";

        // Name of the column of category name
        public static final String COLUMN_CATEGORY_NAME = "category_name";
    }
}
