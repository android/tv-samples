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

package androidx.leanback.leanbackshowcase.app.room.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.leanback.leanbackshowcase.app.room.db.constant.DatabaseColumnConstant;
import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;

import java.util.List;

@Dao
public interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllVideos(List<VideoEntity> videos);

    @Query("SELECT * FROM " + DatabaseColumnConstant.VideoEntry.TABLE_NAME
            + " WHERE " + DatabaseColumnConstant.VideoEntry.COLUMN_AUTO_GENERATE_ID
            + " = :videoId")
    LiveData<VideoEntity> loadVideoById(long videoId);


    @Query("SELECT * FROM " + DatabaseColumnConstant.VideoEntry.TABLE_NAME
            + " WHERE " + DatabaseColumnConstant.VideoEntry.COLUMN_CATEGORY
            + " = :category")
    LiveData<List<VideoEntity>> loadVideoInSameCateogry(String category);

    @Query("SELECT * FROM " + DatabaseColumnConstant.VideoEntry.TABLE_NAME
            + " WHERE "+ DatabaseColumnConstant.VideoEntry.COLUMN_NAME
            + " LIKE " + ":queryMessage"
            + " OR " + DatabaseColumnConstant.VideoEntry.COLUMN_CATEGORY
            +" LIKE " + ":queryMessage")
    LiveData<List<VideoEntity>> searchVideos(String queryMessage);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateVideo(VideoEntity video);
}
