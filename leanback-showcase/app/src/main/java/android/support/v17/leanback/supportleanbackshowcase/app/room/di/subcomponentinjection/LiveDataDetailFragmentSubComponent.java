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

package android.support.v17.leanback.supportleanbackshowcase.app.room.di.subcomponentinjection;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.support.v17.leanback.app.DetailsSupportFragment;
import android.support.v17.leanback.app.DetailsSupportFragmentBackgroundController;
import android.support.v17.leanback.media.MediaPlayerGlue;
import android.support.v17.leanback.supportleanbackshowcase.app.room.adapter.ListAdapter;
import android.support.v17.leanback.supportleanbackshowcase.app.room.controller.detail.LiveDataDetailViewWithVideoBackgroundFragment;
import android.support.v17.leanback.supportleanbackshowcase.app.room.db.entity.VideoEntity;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.action.ActionsModule;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.adapter.AdapterModule;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.adapter.qualifier.DetailFragmentArrayObjectAdapterForActionsQualifier;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.adapter.qualifier.DetailFragmentArrayObjectAdapterForRowsQualifier;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.adapter.qualifier.ListAdapterForRelatedRowQualifier;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.display.DisplayModule;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.listener.ListenerModule;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.presenter.PresenterModule;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.row.RowModule;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.scope.PerFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.PresenterSelector;
import dagger.BindsInstance;
import dagger.Subcomponent;
import java.util.Map;

@PerFragment
@Subcomponent(modules = {PresenterModule.class, RowModule.class,
        AdapterModule.class, LiveDataDetailFragmentUiModule.class, ActionsModule.class,
        ListenerModule.class, DisplayModule.class})
public interface LiveDataDetailFragmentSubComponent {

    Map<Class, OnItemViewClickedListener> onItemViewClickedListenerMap();

    ViewModelProvider.Factory viewModelProviderFactory();

    PresenterSelector presenterSelector();

    @Subcomponent.Builder
    interface Builder {


        @BindsInstance
        Builder activity(Activity activity);

        @BindsInstance
        Builder detailsSupportFragment(DetailsSupportFragment fragment);

        @BindsInstance
        Builder actionClickedListener(OnActionClickedListener listener);

        LiveDataDetailFragmentSubComponent build();
    }

    void inject(LiveDataDetailViewWithVideoBackgroundFragment frag);

    DetailsSupportFragmentBackgroundController fragmentBackgroundController();

    MediaPlayerGlue mediaPlayerGlue();


    @DetailFragmentArrayObjectAdapterForActionsQualifier
    ArrayObjectAdapter overviewRowArrayObjectAdapter();


    DetailsOverviewRow detailsOverviewRow();

    @ListAdapterForRelatedRowQualifier
    ListAdapter<VideoEntity> relatedAdapter();

    ListRow provideRelatedListRow();

    @DetailFragmentArrayObjectAdapterForRowsQualifier
    ArrayObjectAdapter rowsArrayObjectAdapter();

    FullWidthDetailsOverviewSharedElementHelper helper();
}
