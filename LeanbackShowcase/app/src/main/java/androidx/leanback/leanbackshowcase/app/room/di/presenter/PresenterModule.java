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

package androidx.leanback.leanbackshowcase.app.room.di.presenter;

import androidx.lifecycle.ViewModelProvider;
import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;
import androidx.leanback.leanbackshowcase.app.room.di.scope.PerFragment;
import androidx.leanback.leanbackshowcase.app.room.ui.DetailsDescriptionPresenter;
import androidx.leanback.leanbackshowcase.app.room.ui.LiveDataRowPresenter;
import androidx.leanback.leanbackshowcase.app.room.ui.LiveDataRowPresenter.DataLoadedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;
import dagger.Module;
import dagger.Provides;
import java.util.Map;
import androidx.leanback.leanbackshowcase.app.room.ui.VideoCardPresenter;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;

import dagger.multibindings.IntoMap;

@Module
public class PresenterModule {


    @PerFragment
    @Provides
    @IntoMap
    @PresenterSelectorKey(DetailsOverviewRow.class)
    public Presenter provideDetailsOverviewRowPresenter() {

        DetailsDescriptionPresenter pre = new DetailsDescriptionPresenter();
        return new FullWidthDetailsOverviewRowPresenter(pre);
    }


    @PerFragment
    @Provides
    @IntoMap
    @PresenterSelectorKey(ListRow.class)
    public Presenter provideListRowPresenter() {
        return new ListRowPresenter();
    }

    @PerFragment
    @Provides
    @IntoMap
    @PresenterSelectorKey(VideoEntity.class)
    public Presenter provideVideoCardPresenter() {
        return new VideoCardPresenter();
    }

    @PerFragment
    @Provides
    PresenterSelector providePresenterSelector(final Map<Class<?>, Presenter> map) {

        return new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object item) {
                Class<?> cls = item.getClass();
                return map.get(cls);
            }

            @Override
            public Presenter[] getPresenters() {
                return map.values().toArray(new Presenter[map.size()]);
            }
        };
    }

    @PerFragment
    @Provides
    @IntoMap
    @PresenterSelectorKey(VideoEntity.class)
    public VideoCardPresenter provideVideoCardPresenterSpecific() {
        return new VideoCardPresenter();
    }

    @PerFragment
    @Provides
    public LiveDataRowPresenter provideLiveDataRowPresenter(ViewModelProvider.Factory factory,
            DataLoadedListener activity) {
        LiveDataRowPresenter rowPresenter = new LiveDataRowPresenter(factory);

        rowPresenter.registerDataLoadedListener(activity);
        return rowPresenter;
    }
}

