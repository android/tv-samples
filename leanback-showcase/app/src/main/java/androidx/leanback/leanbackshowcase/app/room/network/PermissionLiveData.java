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

package androidx.leanback.leanbackshowcase.app.room.network;

import androidx.lifecycle.MutableLiveData;
import androidx.annotation.MainThread;

public class PermissionLiveData {
    private static MutableLiveData<Boolean> sPermissionLiveData;

    @MainThread
    public static MutableLiveData<Boolean> get() {
        if (sPermissionLiveData == null) {
            sPermissionLiveData = new MutableLiveData<>() ;
        }
        return sPermissionLiveData;
    }
}
