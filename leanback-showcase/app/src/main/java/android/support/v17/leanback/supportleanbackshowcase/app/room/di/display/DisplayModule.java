/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.v17.leanback.supportleanbackshowcase.app.room.di.display;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.DetailsSupportFragmentBackgroundController;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.app.room.adapter.ListAdapter;
import android.support.v17.leanback.supportleanbackshowcase.app.room.controller.detail.LiveDataDetailActivity;
import android.support.v17.leanback.supportleanbackshowcase.app.room.db.entity.VideoEntity;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.scope.PerFragment;
import android.support.v17.leanback.supportleanbackshowcase.app.room.ui.LiveDataRowPresenter;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import android.support.v17.leanback.widget.ListRow;
import android.util.DisplayMetrics;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class DisplayModule {

    @PerFragment
    @Provides
    public DisplayMetrics provideDisplayMetrics(Activity activity) {

        DisplayMetrics metrics = new DisplayMetrics();

        // measure background to show background image
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    @PerFragment
    @Provides
    public BackgroundManager provideBackgroundManager(Activity activity) {

        final BackgroundManager backgroundManager = BackgroundManager.getInstance(activity);

        // backgroundManager.attach(activity.getWindow());
        return backgroundManager;
    }

    @PerFragment
    @Provides
    public Drawable provideDefaultBackground(Activity activity) {
        Drawable defaultBackground = activity.getResources()
                .getDrawable(R.drawable.no_cache_no_internet, null);
        return defaultBackground;
    }

    @PerFragment
    @Provides
    public RequestOptions provideRequestOptions(Drawable defaultBackground) {
        RequestOptions defaultPlaceHolder = new RequestOptions().
                placeholder(defaultBackground);
        return defaultPlaceHolder;
    }

    @PerFragment
    @Provides
    public ListAdapter<ListRow> provideListAdapterOfListRows(LiveDataRowPresenter presenter) {
        return new ListAdapter<>(presenter);
    }
}
