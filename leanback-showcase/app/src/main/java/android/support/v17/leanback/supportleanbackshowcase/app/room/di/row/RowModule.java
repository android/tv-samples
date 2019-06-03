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

package android.support.v17.leanback.supportleanbackshowcase.app.room.di.row;

import android.support.v17.leanback.supportleanbackshowcase.app.room.adapter.ListAdapter;
import android.support.v17.leanback.supportleanbackshowcase.app.room.db.entity.VideoEntity;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.adapter.qualifier.DetailFragmentArrayObjectAdapterForActionsQualifier;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.adapter.qualifier.ListAdapterForRelatedRowQualifier;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.scope.PerFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import dagger.Module;
import dagger.Provides;

@Module
public class RowModule {
    private static final String RELATED_ROW = "Related Row";

    @Provides
    @PerFragment
    DetailsOverviewRow provideDetailsOverviewRow(
            @DetailFragmentArrayObjectAdapterForActionsQualifier ArrayObjectAdapter adapter) {
        DetailsOverviewRow row = new DetailsOverviewRow(new Object());
        row.setActionsAdapter(adapter);
        return row;
    }


    // In this provision method, the header item was set to "Related Row" on default, since the
    // ListRow used in this application all starts with the same header item Related Row
    // But the main responsibility for Dagger is to construct the object. It can be configured
    // accordingly in the runtime.
    @Provides
    @PerFragment
    ListRow provideRelatedListRow(
            @ListAdapterForRelatedRowQualifier ListAdapter<VideoEntity> relatedRowAdapter) {

        HeaderItem mRelatedRowHeaderItem = new HeaderItem(RELATED_ROW);
        return new ListRow(mRelatedRowHeaderItem,relatedRowAdapter);
    }
}
