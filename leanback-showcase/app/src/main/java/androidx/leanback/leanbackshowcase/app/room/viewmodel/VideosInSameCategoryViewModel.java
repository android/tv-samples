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
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import androidx.leanback.leanbackshowcase.app.room.db.repo.VideosRepository;
import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;

import java.util.List;

import javax.inject.Inject;

public class VideosInSameCategoryViewModel extends AndroidViewModel {

    // instance of the repository
    // TODO not injected into here
    private VideosRepository mRepository;

    @Inject
    public VideosInSameCategoryViewModel(@NonNull Application application, VideosRepository repository) {
        super(application);
        mRepository = repository;
    }

    /**
     * Return the video entity list in same category using live data
     *
     * @return live data
     */
    public LiveData<List<VideoEntity>> getVideosInSameCategory(String category) {

        // The design here is: The view model will talk to repository to fetch the live data.
        // The data base is created on the main thread (The database creation won't block the UI)
        // If the database is not prepared (i.e. no valid data existed in the database), it will
        // return an empty live data automatically, there is no need to declare an empty live data
        // explicitly
        return mRepository.getVideosInSameCategoryLiveData(category);
    }
}
