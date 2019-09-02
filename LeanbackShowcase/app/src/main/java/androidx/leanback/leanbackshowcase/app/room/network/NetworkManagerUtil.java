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
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import androidx.leanback.leanbackshowcase.app.room.controller.app.SampleApplication;
import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;
import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The utility class to simpify the network operation.
 */
public class NetworkManagerUtil {

    // resource category
    private static final String VIDEO = "video";
    private static final String BACKGROUND = "background";
    private static final String CARD = "card";

    // For debugging purpose.
    private static final Boolean DEBUG = false;
    private static final String TAG = "NetworkManagerUtil";

    // Prompt Notification.
    private static final String DOWNLOAD_VIDEO = "Download Video";
    private static final String DOWNLOAD_SELECTED_VIDEO = "Download Selected Video";
    private static final String DOWNLOAD_BACKGROUND_IMAGE = "Download Background Image";
    private static final String DOWNLOADING_BACKGROUND_IMAGE_FILE =
            "Downloading background image file";
    private static final String DOWNLOAD_CARD_IMAGE = "Download card image";
    private static final String DOWNLOADING_CARD_IMAGE_FILE = "Downloading card image file";
    public static final String SUFFIX_SEPARATOR = ".";
    // A queue which maintain all downloading task
    final static Map<Long, DownloadingTaskDescription> downloadingTaskQueue = new HashMap<>();

    public static void download(VideoEntity mSelectedVideo) {
        String videoUrl = mSelectedVideo.getVideoUrl();
        String bgUrl = mSelectedVideo.getBgImageUrl();
        String cardUrl = mSelectedVideo.getCardImageUrl();

        String[] downloadUrls = new String[]{videoUrl, bgUrl, cardUrl};
        String[] categories = new String[]{VIDEO, BACKGROUND, CARD};

        // download all related resources
        for (int i = 0; i < downloadUrls.length; i++) {
            Long downloadManagerGeneratedTaskId = downloadFromNetworkHelper(mSelectedVideo.getId(),
                    downloadUrls[i], categories[i]);
            downloadingTaskQueue.put(downloadManagerGeneratedTaskId, new DownloadingTaskDescription(
                    mSelectedVideo, categories[i]
            ));
        }
    }

    /**
     * The helper function to submit actual download task to download manager.
     *
     * @param videoId        The id of this video. Used to name the resource stored in local
     *                       storage.
     * @param downloadingUrl The url of the resource for downloading.
     * @param category       The resource category.
     * @return Download manager generated id. (Return 0 when error occurs)
     */
    private static long downloadFromNetworkHelper(long videoId, String downloadingUrl,
                                                  String category) {

        // Currently we only support downloading for the following format.
        String[] allowedTypes = {"png", "jpg", "jpeg", "gif", "webp", "mp4"};

        // Extract the suffix and test if the suffix satisfies our requirement.
        String suffix = downloadingUrl.substring(
                downloadingUrl.lastIndexOf(SUFFIX_SEPARATOR) + 1).toLowerCase();
        if (!Arrays.asList(allowedTypes).contains(suffix)) {
            if (DEBUG) {
                Log.e(TAG, "Unsupported file type, cannot download");
            }
            return 0L;
        }

        DownloadManager.Request request;
        try {
            request = new DownloadManager.Request(Uri.parse(downloadingUrl));
        } catch (IllegalArgumentException e) {
            if (DEBUG) {
                Log.e(TAG, "Cannot get request object from download manager");
            }
            return 0L;
        }

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        String mediaName;

        // Configure request and media storage name according to resource category
        switch (category) {
            case VIDEO:
                request.setTitle(DOWNLOAD_VIDEO);
                request.setDescription(DOWNLOAD_SELECTED_VIDEO);
                mediaName = VIDEO;

                break;
            case BACKGROUND:
                request.setTitle(DOWNLOAD_BACKGROUND_IMAGE);
                request.setDescription(DOWNLOADING_BACKGROUND_IMAGE_FILE);
                mediaName = BACKGROUND;
                break;
            case CARD:
                request.setTitle(DOWNLOAD_CARD_IMAGE);
                request.setDescription(DOWNLOADING_CARD_IMAGE_FILE);
                mediaName = CARD;
                break;
            default:
                if (DEBUG) {
                    Log.d(TAG, "Not valid resource category");
                }
                return 0L;
        }

        // Set downloading location and file name.
        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS
                        + File.separator
                        + mediaName,
                mediaName + videoId + SUFFIX_SEPARATOR + suffix);

        // submit the downloading task to download manager.
        final DownloadManager dm =
                (DownloadManager) SampleApplication.getInstance().getSystemService(
                        Context.DOWNLOAD_SERVICE);
        final long DownloadManagerGeneratedId = dm.enqueue(request);

        return DownloadManagerGeneratedId;
    }
}
