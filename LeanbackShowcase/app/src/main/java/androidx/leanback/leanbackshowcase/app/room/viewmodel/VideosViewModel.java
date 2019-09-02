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

package androidx.leanback.leanbackshowcase.app.room.viewmodel;

import android.app.Application;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.leanback.leanbackshowcase.app.room.db.repo.VideosRepository;
import androidx.leanback.leanbackshowcase.app.room.db.entity.CategoryEntity;
import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;

import java.util.List;
import javax.inject.Inject;

public class VideosViewModel extends AndroidViewModel {
    // live data connect to database
    private final LiveData<List<CategoryEntity>> mAllCategories;
    private final LiveData<List<VideoEntity>> mSearchResults;
    private final LiveData<VideoEntity> mVideoById;
    private final LiveData<List<VideoEntity>> mAllVideosByCategory;

    // mutable live data can be changed by ui controllers through setter
    private final MutableLiveData<String> mQuery = new MutableLiveData<>();
    private final MutableLiveData<Long> mVideoId = new MutableLiveData<>();
    private final MutableLiveData<String> mVideoCategory = new MutableLiveData<>();

    private final VideosRepository mRepository;

    @Inject
    public VideosViewModel(Application application, VideosRepository repository) {
        super(application);

        mRepository = repository;

        mAllCategories = mRepository.getAllCategories();

        mSearchResults = Transformations.switchMap(
                mQuery, new Function<String, LiveData<List<VideoEntity>>>() {
                    @Override
                    public LiveData<List<VideoEntity>> apply(final String queryMessage) {
                        return mRepository.getSearchResult(queryMessage);
                    }
                });


        mVideoById = Transformations.switchMap(
                mVideoId, new Function<Long, LiveData<VideoEntity>>() {
                    @Override
                    public LiveData<VideoEntity> apply(final Long videoId) {
                        return mRepository.getVideoById(videoId);
                    }
                });

        /**
         * Using switch map function to react to the change of observed variable, the benefits of
         * this mapping method is we don't have to re-create the live data every time.
         */
        mAllVideosByCategory = Transformations.switchMap(mVideoCategory, new Function<String, LiveData<List<VideoEntity>>>() {
            @Override
            public LiveData<List<VideoEntity>> apply(String category) {
                return mRepository.getVideosInSameCategoryLiveData(category);
            }
        });
    }

    public LiveData<List<VideoEntity>> getSearchResult() {
        return mSearchResults;
    }

    public LiveData<VideoEntity> getVideoById() {
        return mVideoById;
    }

    public LiveData<List<VideoEntity>> getVideosInSameCategory() {
        return mAllVideosByCategory;
    }

    public LiveData<List<CategoryEntity>> getAllCategories() {
        return mAllCategories;
    }

    public void setQueryMessage(String queryMessage) {
        mQuery.setValue(queryMessage);
    }

    public void setVideoId(Long videoIdVal) {
        mVideoId.setValue(videoIdVal);
    }

    public void setCategory(String category) {
        mVideoCategory.setValue(category);
    }

    public void updateDatabase(VideoEntity video, String category, String value) {
        mRepository.updateDatabase(video, category, value);
    }
}
