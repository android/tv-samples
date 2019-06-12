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


package androidx.leanback.leanbackshowcase.app.room.db.repo;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import android.os.AsyncTask;
import androidx.annotation.WorkerThread;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.leanbackshowcase.app.room.controller.app.SampleApplication;
import androidx.leanback.leanbackshowcase.app.room.api.VideoDownloadingService;
import androidx.leanback.leanbackshowcase.app.room.api.VideosWithGoogleTag;
import androidx.leanback.leanbackshowcase.app.room.config.AppConfiguration;
import androidx.leanback.leanbackshowcase.app.room.db.AppDatabase;
import androidx.leanback.leanbackshowcase.app.room.db.dao.CategoryDao;
import androidx.leanback.leanbackshowcase.app.room.db.dao.VideoDao;
import androidx.leanback.leanbackshowcase.app.room.db.entity.CategoryEntity;
import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;
import androidx.leanback.leanbackshowcase.utils.Utils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class VideosRepository {

    // For debugging purpose
    private static final boolean DEBUG = false;
    private static final String TAG = "VideosRepository";

    // meta data type constant
    private static final String RENTED = "rented";
    private static final String STATUS = "status";
    private static final String CARD = "card";
    private static final String BACKGROUND = "background";
    private static final String VIDEO = "video";

    private static VideosRepository sVideosRepository;

    private AppDatabase mDb;
    private VideoDao mVideoDao;
    private CategoryDao mCategoryDao;

    // maintain the local cache so the live data can be shared among different components
    private Map<String, LiveData<List<VideoEntity>>> mVideoEntitiesCache;
    private LiveData<List<CategoryEntity>> mCategories;

    public static VideosRepository getVideosRepositoryInstance() {
        if (sVideosRepository == null) {
            sVideosRepository = new VideosRepository();
        }
        return sVideosRepository;
    }

    /**
     * View Model talks to repository through this method to fetch the live data.
     *
     * @param category category
     * @return The list of categories which is wrapped in a live data.
     */
    public LiveData<List<VideoEntity>> getVideosInSameCategoryLiveData(String category) {

        // always try to retrive from local cache firstly
        if (mVideoEntitiesCache.containsKey(category)) {
            return mVideoEntitiesCache.get(category);
        }
        LiveData<List<VideoEntity>> videoEntities = mVideoDao.loadVideoInSameCateogry(category);
        mVideoEntitiesCache.put(category, videoEntities);
        return videoEntities;
    }

    public LiveData<List<CategoryEntity>> getAllCategories() {

        if (mCategories == null) {
            mCategories = mCategoryDao.loadAllCategories();
        }
        return mCategories;
    }

    public LiveData<List<VideoEntity>> getSearchResult(String query) {
        return mVideoDao.searchVideos(query);
    }

    public LiveData<VideoEntity> getVideoById(Long id) {
        return mVideoDao.loadVideoById(id);
    }



    /**
     * Helper function to access the database and update the video information in the database.
     *
     * @param video    video entity
     * @param category which fields to update
     * @param value    updated value
     */
    @WorkerThread
    public synchronized void updateDatabase(VideoEntity video, String category, String value) {
        try {
            mDb.beginTransaction();
            switch (category) {
                case VIDEO:
                    video.setVideoLocalStorageUrl(value);
                    break;
                case BACKGROUND:
                    video.setVideoBgImageLocalStorageUrl(value);
                    break;
                case CARD:
                    video.setVideoCardImageLocalStorageUrl(value);
                    break;
                case STATUS:
                    video.setStatus(value);
                    break;
                case RENTED:
                    video.setRented(true);
                    break;
            }
            mDb.videoDao().updateVideo(video);
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }

    @Inject
    public VideosRepository() {
        createAndPopulateDatabase();
        mVideoDao = mDb.videoDao();
        mCategoryDao = mDb.categoryDao();
        mVideoEntitiesCache = new HashMap<>();
    }

    private void createAndPopulateDatabase() {
        mDb = Room.databaseBuilder(SampleApplication.getInstance(),
                AppDatabase.class, AppDatabase.DATABASE_NAME).build();

        // insert contents into database
        try {
            String url =
                    "https://storage.googleapis.com/android-tv/";
            initializeDb(mDb, url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeDb(AppDatabase db, String url) throws IOException {

        // json data
        String json;

        if (AppConfiguration.IS_DEBUGGING_VERSION) {

            // when use debugging version, we won't fetch data from network but using local
            // json file (only contain 4 video entities in 2 categories.)
            json = Utils.inputStreamToString(SampleApplication
                    .getInstance()
                    .getApplicationContext()
                    .getResources()
                    .openRawResource(R.raw.live_movie_debug));
            Gson gson = new Gson();
            VideosWithGoogleTag videosWithGoogleTag = gson.fromJson(json,
                    VideosWithGoogleTag.class);
            populateDatabase(videosWithGoogleTag,db);
        } else {
            buildDatabase(db, url);
        }
    }

    /**
     * Takes the contents of a JSON object and populates the database
     *
     * @param db Room database.
     */
    private static void buildDatabase(final AppDatabase db, String url) throws IOException {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VideoDownloadingService service = retrofit.create(VideoDownloadingService.class);

        Call<VideosWithGoogleTag> videosWithGoogleTagCall = service.getVideosList();
        videosWithGoogleTagCall.enqueue(new Callback<VideosWithGoogleTag>() {
            @Override
            public void onResponse(Call<VideosWithGoogleTag> call, Response<VideosWithGoogleTag> response) {
                VideosWithGoogleTag videosWithGoogleTag = response.body();
                if (videosWithGoogleTag == null) {
                    Log.d(TAG, "onResponse: result is null");
                    return;
                }
                populateDatabase(videosWithGoogleTag, db);
            }

            @Override
            public void onFailure(Call<VideosWithGoogleTag> call, Throwable t) {
                Log.d(TAG, "Fail to download the content");
            }
        });

    }

    private static void populateDatabase(VideosWithGoogleTag videosWithGoogleTag, final AppDatabase db) {
        for (final VideosWithGoogleTag.VideosGroupByCategory videosGroupByCategory :
                videosWithGoogleTag.getAllResources()) {

            // create category table
            final CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setCategoryName(videosGroupByCategory.getCategory());

            // create video table with customization
            postProcessing(videosGroupByCategory);

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        db.beginTransaction();
                        db.categoryDao().insertCategory(categoryEntity);
                        db.videoDao().insertAllVideos(videosGroupByCategory.getVideos());
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    return null;
                }
            }.execute();

        }
    }

    /**
     * Helper function to make some customization on raw data
     */
    private static void postProcessing(VideosWithGoogleTag.VideosGroupByCategory videosGroupByCategory) {
        for (VideoEntity each : videosGroupByCategory.getVideos()) {
            each.setCategory(videosGroupByCategory.getCategory());
            each.setVideoLocalStorageUrl("");
            each.setVideoBgImageLocalStorageUrl("");
            each.setVideoCardImageLocalStorageUrl("");
            each.setVideoUrl(each.getVideoUrls().get(0));
            each.setRented(false);
            each.setStatus("");
            each.setTrailerVideoUrl("https://storage.googleapis.com/android-tv/Sample%20videos/Google%2B/Google%2B_%20Say%20more%20with%20Hangouts.mp4");
        }
    }
}
