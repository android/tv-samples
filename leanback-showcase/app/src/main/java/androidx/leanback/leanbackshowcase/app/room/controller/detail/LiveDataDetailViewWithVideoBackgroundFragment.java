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

package androidx.leanback.leanbackshowcase.app.room.controller.detail;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.DetailsSupportFragment;
import androidx.leanback.app.DetailsSupportFragmentBackgroundController;
import androidx.leanback.media.MediaPlayerGlue;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.leanbackshowcase.app.room.adapter.ListAdapter;
import androidx.leanback.leanbackshowcase.app.room.config.AppConfiguration;
import androidx.leanback.leanbackshowcase.app.room.controller.app.SampleApplication;
import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;
import androidx.leanback.leanbackshowcase.app.room.di.action.qualifier.LoadingActionQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.action.qualifier.PlayActionQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.action.qualifier.PreviewActionQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.action.qualifier.RentActionQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.adapter.qualifier.DetailFragmentArrayObjectAdapterForActionsQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.adapter.qualifier.DetailFragmentArrayObjectAdapterForRowsQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.adapter.qualifier.ListAdapterForRelatedRowQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.subcomponentinjection.DaggerApplicationComponent;
import androidx.leanback.leanbackshowcase.app.room.network.NetworkLiveData;
import androidx.leanback.leanbackshowcase.app.room.viewmodel.VideosViewModel;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.OnActionClickedListener;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.PresenterSelector;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;


public class LiveDataDetailViewWithVideoBackgroundFragment extends DetailsSupportFragment {

    // For debugging purpose.
    private static final Boolean DEBUG = true;
    private static final String TAG = "leanback.DetailsFrag";

    // Resource category
    private static final String BACKGROUND = "background";
    private static final String CARD = "card";
    private static final String VIDEO = "video";
    private static final String TRAILER = "trailer";
    private static final String TRAILER_VIDEO = " (Trailer)";
    private static final String RENTED_VIDEO = " (Rented)";
    private static final String RENTED = "rented";

    @Inject
    DetailsSupportFragmentBackgroundController mDetailsBgController;

    @Inject
    @DetailFragmentArrayObjectAdapterForRowsQualifier
    ArrayObjectAdapter mRowsAdapter;

    @Inject
    DetailsOverviewRow mDescriptionOverviewRow;

    @Inject
    MediaPlayerGlue mVideoGlue;

    @Inject
    @DetailFragmentArrayObjectAdapterForActionsQualifier
    ArrayObjectAdapter mActionAdapter;

    @Inject
    BackgroundManager mBackgroundManager;

    @Inject
    FullWidthDetailsOverviewSharedElementHelper mHelper;

    @Inject
    DisplayMetrics mMetrics;

    @Inject
    ListRow mRelatedRow;

    @Inject
    @ListAdapterForRelatedRowQualifier
    ListAdapter<VideoEntity> mRelatedRowAdapter;

    @Inject
    PresenterSelector mPs;

    @Inject
    @PlayActionQualifier
    Action mActionPlay;

    @Inject
    @PreviewActionQualifier
    Action mActionPreview;

    @Inject
    @RentActionQualifier
    Action mActionRent;

    @Inject
    @LoadingActionQualifier
    Action mActionLoading;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Map<Class, OnItemViewClickedListener> viewOnClickListenerMap;

    @Inject
    RequestOptions mDefaultPlaceHolder;

    private VideoEntity mObservedVideo;

    private long mSelectedVideoId;

    private VideosViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // As we have stated in the readme, we have used three different approach to implement the
        // dependency injection on the app level. For LiveDataDetailViewWithVideoBackgroundFragment
        // .java, we use the traditionaly subcomponent method to implement the DI
        DaggerApplicationComponent.builder().application(SampleApplication.getInstance())
                .build().presenterBuilder().activity(this.getActivity())
                .detailsSupportFragment(this).actionClickedListener(new ActionClickedListener())
                .build().inject(this);

        mSelectedVideoId = getActivity().getIntent().getLongExtra(
                LiveDataDetailActivity.VIDEO_ID, -1L);

        mObservedVideo = getActivity().getIntent().getParcelableExtra(
                LiveDataDetailActivity.CACHED_CONTENT);

        setSelectedPosition(0, false);
        setAdapter(mRowsAdapter);
        mBackgroundManager.attach(getActivity().getWindow());

        mHelper.startPostponedEnterTransition();
        setOnItemViewClickedListener(viewOnClickListenerMap.get(this.getClass()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDetailsBgController.enableParallax();

        // First loading "cached" data to finish transition animation
        mActionAdapter.clear();
        mActionAdapter.add(mActionLoading);

        mDescriptionOverviewRow.setItem(mObservedVideo);
        loadAndSetVideoCardImage();

        mViewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
                .get(VideosViewModel.class);
        subscribeToModel(mViewModel);
        subscribeToNetworkLiveData();
    }

    private void subscribeToModel(final VideosViewModel model) {
        // Update video Id (live data) and the ui will be updated automatically.
        model.setVideoId(mSelectedVideoId);

        model.getVideoById().observe((LifecycleOwner) getActivity(), new Observer<VideoEntity>() {
            @Override
            public void onChanged(@Nullable VideoEntity videoEntity) {
                if (videoEntity != null) {

                    mObservedVideo = videoEntity;
                    mDetailsBgController.setupVideoPlayback(mVideoGlue);

                    // different loading strategy based on whether the video is rented or not
                    if (!videoEntity.isRented()) {
                        mActionAdapter.clear();
                        mActionAdapter.add(mActionRent);
                        mActionAdapter.add(mActionPreview);

                        mVideoGlue.setTitle(mObservedVideo.getTitle().concat(TRAILER_VIDEO));
                        mVideoGlue.setVideoUrl(findLocalContentUriOrNetworkUrl(TRAILER));
                    } else {
                        getActivity().findViewById(R.id.renting_progressbar)
                                .setVisibility(View.GONE);
                        getActivity().findViewById(R.id.loading_renting).setVisibility(View.GONE);

                        mActionAdapter.clear();
                        mActionAdapter.add(mActionPlay);

                        mVideoGlue.setTitle(mObservedVideo.getTitle().concat(RENTED_VIDEO));
                        mVideoGlue.setVideoUrl(findLocalContentUriOrNetworkUrl(VIDEO));
                    }

                    mDescriptionOverviewRow.setItem(mObservedVideo);
                    loadAndSetVideoCardImage();
                    model.setCategory(mObservedVideo.getCategory());
                }
            }
        });

        model.getVideosInSameCategory()
                .observe((LifecycleOwner) getActivity(), new Observer<List<VideoEntity>>() {
                    @Override
                    public void onChanged(@Nullable List<VideoEntity> videoEntities) {
                        if (videoEntities != null) {
                            mRelatedRowAdapter
                                    .setItems(videoEntities, new Comparator<VideoEntity>() {
                                        @Override
                                        public int compare(VideoEntity o1, VideoEntity o2) {
                                            return o1.getId() == o2.getId() ? 0 : -1;
                                        }
                                    }, new Comparator<VideoEntity>() {
                                        @Override
                                        public int compare(VideoEntity o1, VideoEntity o2) {
                                            return o1.equals(o2) ? 0 : -1;
                                        }
                                    });
                        }
                    }
                });
    }

    private void subscribeToNetworkLiveData() {
        NetworkLiveData.sync(getActivity())
                .observe((LifecycleOwner) getActivity(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if (aBoolean) {
                            getActivity().findViewById(R.id.no_internet_detail)
                                    .setVisibility(View.GONE);
                        } else {
                            getActivity().findViewById(R.id.no_internet_detail)
                                    .setVisibility(View.VISIBLE);
                        }
                    }
                });
    }


    // Action Listener is dependent on Runtime information. Cannot be injected in the Listener's
    // module
    private final class ActionClickedListener implements OnActionClickedListener {

        @Override
        public void onActionClicked(Action action) {
            if (action == mActionRent) {

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (AppConfiguration.IS_RENTING_OPERATION_DELAY_ENABLED) {
                            addDelay(2000L);
                        }

                        // update the database with rented field
                        mViewModel.updateDatabase(mObservedVideo, RENTED, "");
                        return null;
                    }
                }.execute();

                getActivity().findViewById(R.id.renting_progressbar).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.loading_renting).setVisibility(View.VISIBLE);
            } else if (action == mActionPlay) {
                mDetailsBgController.switchToVideo();
            } else if (action == mActionPreview) {
                mDetailsBgController.switchToVideo();
            } else if (action == mActionLoading) {
                Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadAndSetVideoCardImage() {
        String loadedUri = findLocalContentUriOrNetworkUrl(CARD);
        Glide.with(this)
                .asBitmap()
                .load(loadedUri)
                .apply(mDefaultPlaceHolder)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final Bitmap resource,
                            Transition<? super Bitmap> glideAnimation) {
                        mDescriptionOverviewRow.setImageBitmap(getActivity(), resource);
                    }
                });
        setSelectedPosition(0, false);
    }

    private String findLocalContentUriOrNetworkUrl(String type) {
        String loadedUri;
        switch (type) {
            case BACKGROUND:
                if (!mObservedVideo.getVideoBgImageLocalStorageUrl().isEmpty()) {
                    loadedUri = mObservedVideo.getVideoBgImageLocalStorageUrl();
                } else {
                    loadedUri = mObservedVideo.getBgImageUrl();
                }
                break;
            case CARD:
                if (!mObservedVideo.getVideoCardImageLocalStorageUrl().isEmpty()) {
                    loadedUri = mObservedVideo.getVideoCardImageLocalStorageUrl();
                } else {
                    loadedUri = mObservedVideo.getCardImageUrl();
                }
                break;
            case VIDEO:
                if (!mObservedVideo.getVideoLocalStorageUrl().isEmpty()) {
                    loadedUri = mObservedVideo.getVideoLocalStorageUrl();
                } else {
                    loadedUri = mObservedVideo.getVideoUrl();
                }
                break;
            case TRAILER:
                loadedUri = mObservedVideo.getTrailerVideoUrl();
                break;
            default:
                if (DEBUG) {
                    Log.d(TAG, "Not valid image resource type");
                }
                return "";
        }
        return loadedUri;
    }

    private void addDelay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            if (DEBUG) {
                Log.e(TAG, "addDelay: ", e);
            }
        }
    }
}
