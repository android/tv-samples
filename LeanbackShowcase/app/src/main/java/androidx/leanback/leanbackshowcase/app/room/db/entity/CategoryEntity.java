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
import androidx.room.PrimaryKey;
import androidx.leanback.leanbackshowcase.app.room.db.constant.DatabaseColumnConstant;


@Entity(tableName = DatabaseColumnConstant.CategoryEntry.TABLE_NAME)
public class CategoryEntity {
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = DatabaseColumnConstant.CategoryEntry.COLUMN_AUTOGENERATE_ID)
    private int mId;

    @ColumnInfo(name = DatabaseColumnConstant.CategoryEntry.COLUMN_CATEGORY_NAME)
    private String mCategoryName;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        this.mCategoryName = categoryName;
    }
}
