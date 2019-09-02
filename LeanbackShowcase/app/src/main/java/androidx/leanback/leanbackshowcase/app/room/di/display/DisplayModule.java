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

package androidx.leanback.leanbackshowcase.app.room.di.display;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.leanbackshowcase.app.room.adapter.ListAdapter;
import androidx.leanback.leanbackshowcase.app.room.di.scope.PerFragment;
import androidx.leanback.leanbackshowcase.app.room.ui.LiveDataRowPresenter;
import androidx.leanback.widget.ListRow;
import android.util.DisplayMetrics;

import com.bumptech.glide.request.RequestOptions;

import dagger.Module;
import dagger.Provides;

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
