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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.leanbackshowcase.app.room.controller.detail.LiveDataDetailActivity;
import androidx.leanback.leanbackshowcase.app.room.controller.detail.LiveDataDetailViewWithVideoBackgroundFragment;
import androidx.leanback.leanbackshowcase.app.room.controller.overview.LiveDataFragment;
import androidx.leanback.leanbackshowcase.app.room.controller.search.SearchActivity;
import androidx.leanback.leanbackshowcase.app.room.controller.search.SearchFragment;
import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;
import androidx.leanback.leanbackshowcase.app.room.di.scope.PerFragment;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.core.app.ActivityOptionsCompat;
import android.util.DisplayMetrics;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class ListenerModule {

    private static final int BACKGROUND_UPDATE_DELAY = 300;

    private RunnableClass lastTime;

    @PerFragment
    @Provides
    @IntoMap
    @ListenerModuleKey(LiveDataFragment.class)
    public OnItemViewClickedListener provideOnItemViewClickListenerForLiveDataFragment(
            final Activity currentActivity) {
        return new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                    RowPresenter.ViewHolder rowViewHolder, Row row) {
                Intent intent;
                Long videoItemId = ((VideoEntity) item).getId();
                intent = new Intent(currentActivity, LiveDataDetailActivity.class);
                intent.putExtra(LiveDataDetailActivity.VIDEO_ID, videoItemId);

                VideoEntity cachedBundle = (VideoEntity) item;

                intent.putExtra(LiveDataDetailActivity.CACHED_CONTENT, cachedBundle);

                // enable the scene transition animation when the detail's activity is launched
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        currentActivity,
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        LiveDataDetailActivity.SHARED_ELEMENT_NAME).toBundle();
                currentActivity.startActivity(intent, bundle);
            }
        };
    }

    @PerFragment
    @Provides
    @IntoMap
    @ListenerModuleKey(SearchFragment.class)
    public OnItemViewClickedListener provideOnItemViewClickListenerForLiveDataDetailFragmentSearch(
            final Activity currentActivity) {
        return new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                    RowPresenter.ViewHolder rowViewHolder, Row row) {
                if (item instanceof VideoEntity) {
                    Intent intent;
                    Long videoItemId = ((VideoEntity) item).getId();
                    intent = new Intent(currentActivity, LiveDataDetailActivity.class);
                    intent.putExtra(LiveDataDetailActivity.VIDEO_ID, videoItemId);

                    VideoEntity cachedBundle = (VideoEntity) item;

                    intent.putExtra(LiveDataDetailActivity.CACHED_CONTENT, cachedBundle);

                    // enable the scene transition animation when the detail's activity is launched
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            currentActivity,
                            ((ImageCardView) itemViewHolder.view).getMainImageView(),
                            LiveDataDetailActivity.SHARED_ELEMENT_NAME).toBundle();
                    currentActivity.startActivity(intent, bundle);
                }
            }
        };
    }

    @PerFragment
    @Provides
    @IntoMap
    @ListenerModuleKey(LiveDataDetailViewWithVideoBackgroundFragment.class)
    public OnItemViewClickedListener provideOnItemViewClickListenerForLiveDataDetailFragment(
            final Activity currentActivity) {
        return new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                    RowPresenter.ViewHolder rowViewHolder, Row row) {
                if (item instanceof VideoEntity) {
                    Intent intent;
                    Long videoItemId = ((VideoEntity) item).getId();
                    intent = new Intent(currentActivity, LiveDataDetailActivity.class);
                    intent.putExtra(LiveDataDetailActivity.VIDEO_ID, videoItemId);

                    VideoEntity cachedBundle = (VideoEntity) item;

                    intent.putExtra(LiveDataDetailActivity.CACHED_CONTENT, cachedBundle);

                    // enable the scene transition animation when the detail's activity is launched
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            currentActivity,
                            ((ImageCardView) itemViewHolder.view).getMainImageView(),
                            LiveDataDetailActivity.SHARED_ELEMENT_NAME).toBundle();
                    currentActivity.startActivity(intent, bundle);
                }
            }
        };
    }


    @PerFragment
    @Provides
    public Handler provideMainThreadHandler() {
        return new Handler(Looper.getMainLooper());
    }

    @PerFragment
    @Provides
    @IntoMap
    @ListenerModuleKey(LiveDataFragment.class)
    public OnItemViewSelectedListener provideOnItemViewSelectedListener(final Activity activity,
            final DisplayMetrics metrics, final BackgroundManager backgroundManager,
            final RequestOptions defaultPlaceHolder, final Drawable finalDrawable, final Handler mainHandler) {
        return new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                    RowPresenter.ViewHolder rowViewHolder, Row row) {
                VideoEntity selectedVideo = (VideoEntity) item;
                RunnableClass backgroundRunnable = new RunnableClass(selectedVideo, activity,
                        metrics, backgroundManager, defaultPlaceHolder, finalDrawable);

                if (lastTime != null) {
                    mainHandler.removeCallbacks(lastTime);
                }
                mainHandler.postDelayed(backgroundRunnable, BACKGROUND_UPDATE_DELAY);
                lastTime = backgroundRunnable;
            }
        };
    }

    @PerFragment
    @Provides
    @IntoMap
    @ListenerModuleKey(LiveDataFragment.class)
    public View.OnClickListener provideOnClickSearchButtonListener(final Activity currentActivity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(currentActivity, SearchActivity.class);
                currentActivity.startActivity(intent);
            }
        };
    }

    /**
     * Define this runnable class explicitly so it can take dependency injected parameter to
     * construct the runnable object for execution
     */
    private class RunnableClass implements Runnable {

        private VideoEntity mSelectedVideo;
        private Activity mActivity;
        private DisplayMetrics mDisplayMetrics;
        private BackgroundManager mBackgroundManager;
        private RequestOptions mDefaultPlaceHolder;
        private Drawable mDrawable;

        public RunnableClass(VideoEntity selectedVideo, final Activity activity,
                DisplayMetrics metrics, BackgroundManager backgroundManager,
                RequestOptions defaultPlaceHolder, final Drawable drawable) {
            mSelectedVideo = selectedVideo;
            mActivity = activity;
            mDisplayMetrics = metrics;
            mBackgroundManager = backgroundManager;
            mDefaultPlaceHolder = defaultPlaceHolder;
            mDrawable = drawable;
        }

        @Override
        public void run() {
            loadAndSetBackgroundImageParameter(mSelectedVideo, mActivity, mDisplayMetrics,
                    mBackgroundManager, mDefaultPlaceHolder, mDrawable);
        }
    }

    private void loadAndSetBackgroundImageParameter(VideoEntity selectedVideo,
            final Activity activity, DisplayMetrics metrics,
            final BackgroundManager backgroundManager, RequestOptions defaultPlaceHolder,
            Drawable defualtDrawble) {
        if (selectedVideo == null) {
            return;
        }
        String url1 = selectedVideo.getVideoBgImageLocalStorageUrl();
        String url2 = selectedVideo.getBgImageUrl();
        String loadedUri;
        if (url1.isEmpty()) {
            loadedUri = url2;
        } else {
            loadedUri = url1;
        }

        // wait until runtime the activity has window
        if (!backgroundManager.isAttached()) {
            backgroundManager.attach(activity.getWindow());
        }

        // glide on error
        Glide.with(activity)
                .asBitmap()
                .load(loadedUri)
                .apply(defaultPlaceHolder)
                .into(new SimpleTarget<Bitmap>(metrics.widthPixels, metrics.heightPixels) {
                    @Override
                    public void onResourceReady(Bitmap resource,
                            Transition<? super Bitmap> glideAnimation) {
                        backgroundManager.setDrawable(
                                new BitmapDrawable(activity.getResources(), resource));
                    }
                });
    }
}
