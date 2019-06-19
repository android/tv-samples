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

import androidx.leanback.leanbackshowcase.app.room.controller.search.SearchFragment;

import androidx.leanback.leanbackshowcase.app.room.di.scope.PerFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;


/**
 * As we have discussed before, this module will be responsible for twoi tasks
 * 1. It will connect to the sub component which is responsible for search fragment's injection
 * 2.
 */
@Module
public abstract class SearchFragmentInjectorInstallmentFactoryBindingModule {
  @PerFragment
  @ContributesAndroidInjector(modules = SearchFragmentUIModule.class)
  abstract SearchFragment contributeSearchFragment();
}
