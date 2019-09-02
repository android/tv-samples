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
import androidx.leanback.app.DetailsSupportFragment;
import androidx.leanback.app.DetailsSupportFragmentBackgroundController;
import androidx.leanback.media.MediaPlayerGlue;
import androidx.leanback.leanbackshowcase.app.room.controller.detail.LiveDataDetailActivity;
import androidx.leanback.leanbackshowcase.app.room.di.scope.PerFragment;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import androidx.leanback.widget.OnActionClickedListener;
import androidx.leanback.widget.PresenterSelector;
import dagger.Module;
import dagger.Provides;


@Module
public class LiveDataDetailFragmentUiModule {

    @Provides
    @PerFragment
    public FullWidthDetailsOverviewSharedElementHelper provideHelper(Activity activity,
            PresenterSelector mPs, DetailsOverviewRow row, OnActionClickedListener listener) {
        FullWidthDetailsOverviewSharedElementHelper mHelper =
                new FullWidthDetailsOverviewSharedElementHelper();
        mHelper.setSharedElementEnterTransition(activity,
                LiveDataDetailActivity.SHARED_ELEMENT_NAME);

        ((FullWidthDetailsOverviewRowPresenter) mPs.getPresenter(row))
                .setListener(mHelper);
        ((FullWidthDetailsOverviewRowPresenter) mPs.getPresenter(row))
                .setParticipatingEntranceTransition(false);
        ((FullWidthDetailsOverviewRowPresenter) mPs.getPresenter(row))
                .setOnActionClickedListener(listener);
        return mHelper;

    }


    @Provides
    @PerFragment
    DetailsSupportFragmentBackgroundController provideFragmentBackgroundController(
            DetailsSupportFragment fragment) {
        return new DetailsSupportFragmentBackgroundController(fragment);
    }

    @Provides
    @PerFragment
    MediaPlayerGlue provideMediaPlayerGlue(Activity activity) {
        return new MediaPlayerGlue(activity);
    }
}
