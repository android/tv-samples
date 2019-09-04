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


import android.app.Activity;

import androidx.leanback.leanbackshowcase.app.room.controller.search.SearchActivity;
import androidx.leanback.leanbackshowcase.app.room.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchActivityModule {

    // the SearchActivityComponent is extended from AndroidInjector, when it is injected to
    // SearchActivity, the activity will be added to the graph automatically without using
    // @BindInstance like what we have done in processing the traditional sub component
    @Provides
    @PerActivity
    Activity provideSearchActivityModule(SearchActivity searchActivity) {
        return searchActivity;
    }

}
