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

package androidx.leanback.leanbackshowcase.app.room.di.adapter;


import androidx.leanback.leanbackshowcase.app.room.adapter.ListAdapter;
import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;
import androidx.leanback.leanbackshowcase.app.room.di.adapter.qualifier.DetailFragmentArrayObjectAdapterForActionsQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.adapter.qualifier.DetailFragmentArrayObjectAdapterForRowsQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.adapter.qualifier.ListAdapteWithLiveDataRowPresenterQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.adapter.qualifier.ListAdapterForRelatedRowQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.adapter.qualifier.SearchFragmentArrayObjectAdapterForRowsQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.scope.PerFragment;
import androidx.leanback.leanbackshowcase.app.room.ui.LiveDataRowPresenter;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.PresenterSelector;
import dagger.Module;
import dagger.Provides;

/**
 * the adapter should live inside of each fragment and shouldn't be shared among different fragments
 */
@Module
public class AdapterModule {

    @DetailFragmentArrayObjectAdapterForActionsQualifier
    @PerFragment
    @Provides
    ArrayObjectAdapter provideOverviewRowArrayObjectAdapter() {
        return new ArrayObjectAdapter();
    }

    @DetailFragmentArrayObjectAdapterForRowsQualifier
    @PerFragment
    @Provides
    ArrayObjectAdapter provideRowsArrayObjectAdapter(DetailsOverviewRow detailsOverviewRow, ListRow relatedRow, PresenterSelector mps) {
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(mps);
        rowsAdapter.add(detailsOverviewRow);
        rowsAdapter.add(relatedRow);
        return rowsAdapter;
    }

    @SearchFragmentArrayObjectAdapterForRowsQualifier
    @PerFragment
    @Provides
    ArrayObjectAdapter provideRowsArrayObjectAdapterInSearchFragment( ListRow relatedRow, PresenterSelector mps) {
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(mps.getPresenter(relatedRow));

        rowsAdapter.add(relatedRow);
        return rowsAdapter;
    }

    @ListAdapteWithLiveDataRowPresenterQualifier
    @PerFragment
    @Provides
    ListAdapter<ListRow> provideListRowListAdapter(LiveDataRowPresenter presenter) {
        return new ListAdapter<>(presenter);
    }


    @ListAdapterForRelatedRowQualifier
    @PerFragment
    @Provides
    ListAdapter<VideoEntity> provideRelatedAdapter(PresenterSelector mps) {
        return new ListAdapter<>(mps.getPresenter(new VideoEntity()));
    }
}
