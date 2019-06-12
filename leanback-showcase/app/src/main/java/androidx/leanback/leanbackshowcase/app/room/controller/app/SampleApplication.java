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

package androidx.leanback.leanbackshowcase.app.room.controller.app;

import android.app.Activity;
import android.app.Application;

import androidx.leanback.leanbackshowcase.app.room.di.androidinject.AppInjector;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import javax.inject.Inject;

/**
 * Customized application class.
 */
public class SampleApplication extends Application implements HasActivityInjector {

    private static SampleApplication sApp;

    // This class will be instantiated once when the application is started, no need to design it
    // as a singleton on purpose.
    public static SampleApplication getInstance() {
        return sApp;
    }

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;

        AppInjector.init(this);

        // If we want to process the dependency injection manually, we have to add this line.
        // DaggerAppComponent.builder().application(this).build().inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
