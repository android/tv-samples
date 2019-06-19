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

package androidx.leanback.leanbackshowcase.app.room.di.androidinject;

import androidx.leanback.leanbackshowcase.app.room.controller.detail.LiveDataDetailViewWithVideoBackgroundFragment;

import androidx.leanback.leanbackshowcase.app.room.di.presenter.PresenterModule;
import androidx.leanback.leanbackshowcase.app.room.di.scope.PerFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


// generated fragment sub component will be added to this module
// this module will be installed to a higher level component the activity's component
// so module + contribute injector annotation make sure we don't have the previous two steps
@Module
public abstract class LiveDataDetailViewWithVideoBackgroundFragmentModule {

  @PerFragment
  @ContributesAndroidInjector (modules = {PresenterModule.class})
  abstract LiveDataDetailViewWithVideoBackgroundFragment contributeLiveDataDetailViewWithVideoBackgroundFragment();

}
