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

package androidx.leanback.leanbackshowcase.app.room.di.action;


import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.leanbackshowcase.app.room.controller.app.SampleApplication;
import androidx.leanback.leanbackshowcase.app.room.di.action.qualifier.LoadingActionQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.action.qualifier.PlayActionQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.action.qualifier.PreviewActionQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.action.qualifier.RentActionQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.scope.PerFragment;
import androidx.leanback.widget.Action;
import androidx.core.content.res.ResourcesCompat;

import dagger.Module;
import dagger.Provides;


@Module
public class ActionsModule {

  public static final int ACTION_PLAY = 1;
  public static final int ACTION_RENT = 2;
  public static final int ACTION_PREVIEW = 3;
  public static final int ACTION_LOADING = 4;

  @PerFragment
  @PlayActionQualifier
  @Provides
  Action providePlayAction() {

    return new Action(ACTION_PLAY,
        SampleApplication.getInstance().getString(R.string.livedata_action_play));
  }

  @PerFragment
  @RentActionQualifier
  @Provides
  Action provideRentAction() {

    return new Action(ACTION_RENT,
        SampleApplication.getInstance().getString(R.string.livedata_actoin_rent),
        SampleApplication.getInstance().getString(R.string.livedata_rent_price), ResourcesCompat
        .getDrawable(
            SampleApplication.getInstance().getResources(),
            R.drawable.ic_favorite_border_white_24dp,
            SampleApplication.getInstance().getTheme()));
  }

  @PerFragment
  @PreviewActionQualifier
  @Provides
  Action providePreviewAction() {
    return new Action(ACTION_PREVIEW,
        SampleApplication.getInstance().getString(R.string.livedata_action_preview));
  }

  @PerFragment
  @LoadingActionQualifier
  @Provides
  Action provideLoadingAction() {
    return new Action(ACTION_LOADING,
        SampleApplication.getInstance().getString(R.string.livedata_action_loading));
  }
}

