/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.leanback.leanbackshowcase.app.room.controller.detail;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.leanback.leanbackshowcase.R;
import androidx.fragment.app.Fragment;
import dagger.android.DispatchingAndroidInjector;
import javax.inject.Inject;


/**
 * Extend from LifecycleActivity so this activity can be used as the owner of lifecycle event
 */
public class LiveDataDetailActivity extends FragmentActivity {

    public static final String SHARED_ELEMENT_NAME = "hero";
    public static final String VIDEO_ID = "video_id";
    public static final String CACHED_CONTENT = "video_cached";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_fragment_with_video_background);
    }

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
}
