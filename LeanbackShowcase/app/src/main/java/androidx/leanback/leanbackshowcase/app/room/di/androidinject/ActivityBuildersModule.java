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

import androidx.leanback.leanbackshowcase.app.room.controller.overview.LiveDataRowsActivity;
import androidx.leanback.leanbackshowcase.app.room.controller.search.SearchActivity;
import androidx.leanback.leanbackshowcase.app.room.di.androidinjectorannotation.LiveDataOverviewActivitySubcomponent;
import androidx.leanback.leanbackshowcase.app.room.di.scope.PerActivity;

import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.ClassKey;

@Module
public abstract class ActivityBuildersModule {

    @PerActivity
    @ContributesAndroidInjector(modules
            = {SearchActivityModule.class,
            SearchFragmentInjectorInstallmentFactoryBindingModule.class})
    abstract SearchActivity contributeToAndriodInjectorForSearchActivity();

    @ClassKey(LiveDataRowsActivity.class)
    abstract AndroidInjector.Factory<?> bindActivityInjectorFactory(LiveDataOverviewActivitySubcomponent.Builder builder);
}
