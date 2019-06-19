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

import android.app.DownloadManager;

import androidx.lifecycle.LifecycleObserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class DownloadCompleteBroadcastReceiver extends BroadcastReceiver
        implements LifecycleObserver{

    // singleton design pattern
    private static DownloadCompleteBroadcastReceiver sReceiver;

    private List<DownloadCompleteListener> downloadingCompletionListener;

    public interface DownloadCompleteListener {
        void onDownloadingCompleted(DownloadingTaskDescription desc);
    }

    public void registerListener(DownloadCompleteListener listener) {
        downloadingCompletionListener.add(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final DownloadManager dm = (DownloadManager) context.getSystemService(
                Context.DOWNLOAD_SERVICE);
        Long downloadingTaskId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
        if (NetworkManagerUtil.downloadingTaskQueue.containsKey(downloadingTaskId)) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadingTaskId);
            Cursor cursor = dm.query(query);

            // retrieve downloaded content's information through download manager.
            if (!cursor.moveToFirst()) {
                return;
            }

            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    != DownloadManager.STATUS_SUCCESSFUL) {
                return;
            }

            String path = cursor.getString(
                    cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

            final DownloadingTaskDescription desc =
                    NetworkManagerUtil.downloadingTaskQueue.get(downloadingTaskId);

            // update local storage path
            desc.setStoragePath(path);

            for (final DownloadCompleteListener listener: downloadingCompletionListener) {
                        listener.onDownloadingCompleted(desc);
            }
        }
    }

    public static DownloadCompleteBroadcastReceiver getInstance() {
        if (sReceiver == null) {
            sReceiver = new DownloadCompleteBroadcastReceiver();
        }
        return sReceiver;
    }

    private DownloadCompleteBroadcastReceiver() {
        downloadingCompletionListener = new ArrayList<>();
    }
}
