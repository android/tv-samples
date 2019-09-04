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

package androidx.leanback.leanbackshowcase.app.room.di.listener;

import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;

import android.view.View;

import java.util.Map;
import javax.inject.Inject;

public class ListenerFactory {

    private Map<Class, OnItemViewClickedListener> mOnItemViewClickedListenerMap;
    private Map<Class, OnItemViewSelectedListener> mOnItemViewSelectedListenerMap;
    private Map<Class, View.OnClickListener> mViewOnClickListenerMap;

    @Inject
    public ListenerFactory(Map<Class, OnItemViewClickedListener> onItemViewClickedListenerMap,
            Map<Class, OnItemViewSelectedListener> onItemViewSelectedListenerMap,
            Map<Class, View.OnClickListener> viewOnClickListenerMap) {
        mOnItemViewClickedListenerMap = onItemViewClickedListenerMap;
        mOnItemViewSelectedListenerMap = onItemViewSelectedListenerMap;
        mViewOnClickListenerMap = viewOnClickListenerMap;

    }

    public OnItemViewClickedListener getOnItemViewClickedListener(Class key) {
        return mOnItemViewClickedListenerMap.get(key);
    }

    public OnItemViewSelectedListener getOnItemViewSelectedListener(Class key) {
        return mOnItemViewSelectedListenerMap.get(key);
    }

    public View.OnClickListener getViewOnClickListener(Class key) {
        return mViewOnClickListenerMap.get(key);
    }
}
