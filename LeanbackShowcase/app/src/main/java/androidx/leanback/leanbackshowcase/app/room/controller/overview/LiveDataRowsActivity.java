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

package androidx.leanback.leanbackshowcase.app.room.controller.overview;

import android.Manifest;
import android.app.DownloadManager;
import androidx.fragment.app.FragmentActivity;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.leanbackshowcase.app.room.network.DownloadCompleteBroadcastReceiver;
import androidx.leanback.leanbackshowcase.app.room.network.PermissionLiveData;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;

/**
 * Extend from LifecycleActivity so this activity can be used as the owner of lifecycle event
 */
public class LiveDataRowsActivity extends FragmentActivity implements HasSupportFragmentInjector{

    private static final int WRITE_PERMISSION = 0;

    private DownloadCompleteBroadcastReceiver mReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_livedata_rows);


        if (ContextCompat.checkSelfPermission(LiveDataRowsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // If the permission is not authorized in the first time. A new permission access
            // request will be created.
            if (ActivityCompat.shouldShowRequestPermissionRationale(LiveDataRowsActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(LiveDataRowsActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(LiveDataRowsActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_PERMISSION);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        // Handle the permission request callback.
        switch (requestCode) {
            case WRITE_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // If the WRITE_EXTERNAL_STORAGE is authorized, we will update the value in
                    // shared preference so other component (VideoCardPresenter) can access this
                    // information.
                    PermissionLiveData.get().setValue(true);
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // The broadcast receiver will only be registered when the browse fragment can be loaded at
        // the main frame.
        mReceiver = DownloadCompleteBroadcastReceiver.getInstance();
        registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }


    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    // @Inject
    // DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    //
    // @Override
    // public AndroidInjector<Fragment> supportFragmentInjector() {
    //   return dispatchingAndroidInjector;
    // }
}
