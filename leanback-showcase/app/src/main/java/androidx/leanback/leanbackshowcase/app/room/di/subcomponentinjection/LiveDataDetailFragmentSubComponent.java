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

package androidx.leanback.leanbackshowcase.app.room.di.subcomponentinjection;

import android.app.Activity;
import androidx.lifecycle.ViewModelProvider;
import androidx.leanback.app.DetailsSupportFragment;
import androidx.leanback.app.DetailsSupportFragmentBackgroundController;
import androidx.leanback.media.MediaPlayerGlue;
import androidx.leanback.leanbackshowcase.app.room.adapter.ListAdapter;
import androidx.leanback.leanbackshowcase.app.room.controller.detail.LiveDataDetailViewWithVideoBackgroundFragment;
import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;
import androidx.leanback.leanbackshowcase.app.room.di.action.ActionsModule;
import androidx.leanback.leanbackshowcase.app.room.di.adapter.AdapterModule;
import androidx.leanback.leanbackshowcase.app.room.di.adapter.qualifier.DetailFragmentArrayObjectAdapterForActionsQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.adapter.qualifier.DetailFragmentArrayObjectAdapterForRowsQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.adapter.qualifier.ListAdapterForRelatedRowQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.display.DisplayModule;
import androidx.leanback.leanbackshowcase.app.room.di.listener.ListenerModule;
import androidx.leanback.leanbackshowcase.app.room.di.presenter.PresenterModule;
import androidx.leanback.leanbackshowcase.app.room.di.row.RowModule;
import androidx.leanback.leanbackshowcase.app.room.di.scope.PerFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.OnActionClickedListener;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.PresenterSelector;
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
