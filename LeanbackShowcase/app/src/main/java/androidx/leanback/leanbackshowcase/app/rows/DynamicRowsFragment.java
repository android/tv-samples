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

package androidx.leanback.leanbackshowcase.app.rows;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.leanbackshowcase.app.media.VideoExampleActivity;
import androidx.leanback.leanbackshowcase.cards.presenters.IconCardInChannelPublishPresenter;
import androidx.leanback.leanbackshowcase.models.Card;
import androidx.leanback.leanbackshowcase.models.CardRow;
import androidx.leanback.leanbackshowcase.utils.CardListRow;
import androidx.leanback.leanbackshowcase.utils.Utils;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import java.net.URI;
import java.util.List;

/**
 * The main fragment which is responsible for all interaction
 * <p>
 * like adding/ removing rows from app to launcher, play video within the app
 */
public class DynamicRowsFragment extends BrowseFragment {

    private static final String TAG = "DynamicRowsFragment";
    private static final boolean DEBUG = false;
    private static final int BACKGROUND_UPDATE_DELAY = 300;

    private final Handler mHandler = new Handler();
    private DisplayMetrics mMetrics;
    private URI mBackgroundURI;
    private BackgroundManager mBackgroundManager;
    private Runnable mBackgroudUpdateRunnable;
    /**
     * All channel contents related to this app.
     */
    private List<ChannelContents> mChannelContents;

    /**
     * Fragment UI setup
     *
     * The following function/ class is closely related to the UI which is related to the fragment
     * itself
     */


    /**
     * Basic UI setting, including header state/ search button etc
     */
    private void setupUi() {
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setTitle(getString(R.string.channel_publish_title));
        setOnSearchClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getString(R.string.implement_search),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupUi();
        ChannelContents.initializePlaylists(this.getActivity());

        mChannelContents = ChannelContents.sChannelContents;
        loadRows();

        prepareBackgroundManager();
        setupEventListeners();
        mBackgroudUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                if (mBackgroundURI != null) {
                    updateBackgroundImage(mBackgroundURI.toString());
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mBackgroudUpdateRunnable);
    }

    /**
     * Populate card/ adding-removing button into ArrayObjectAdapter
     */
    private void loadRows() {
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        for (int i = 0; i < mChannelContents.size(); i++) {
            ChannelContents playlist = mChannelContents.get(i);
            List<VideoContent> clips = playlist.getVideos();
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new VideoContentCardPresenter());
            for (int j = 0; j < clips.size(); ++j) {
                listRowAdapter.add(clips.get(j));
            }
            HeaderItem header = new HeaderItem(i, playlist.getName());
            rowsAdapter.add(new ListRow(header, listRowAdapter));
        }

        /**
         * Add an additional settings row at the bottom for channel publish customization
         */
        String json = Utils
                .inputStreamToString(getResources().openRawResource(R.raw.settings_row));
        CardRow settingsRow = new Gson().fromJson(json, CardRow.class);
        /**
         * Set specialized icon presenter to current row
         */
        ArrayObjectAdapter listRowAdapter =
                new ArrayObjectAdapter(new IconCardInChannelPublishPresenter(getActivity()));
        for (Card card : settingsRow.getCards()) {
            listRowAdapter.add(card);
        }
        HeaderItem settingRowHeader = new HeaderItem(settingsRow.getTitle());
        Row settingCardRow = new CardListRow(settingRowHeader, listRowAdapter, settingsRow);
        rowsAdapter.add(settingCardRow);
        setAdapter(rowsAdapter);
    }

    /**
     * Click and select event listener
     */

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (isAdded()) {
                if (item instanceof VideoContent) {
                    VideoContent clip = (VideoContent) item;
                    Intent intent = new Intent(getActivity(), VideoPlaybackActivity.class);
                    intent.putExtra(VideoExampleActivity.TAG, clip);
                    startActivity(intent);
                } else {
                    /**
                     * Add to home screen feature is not allowed in the system less than android O
                     * a toast will be popped up as notification
                     */
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                        Toast.makeText(getActivity(), "Add to home screen not supported", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    Intent intent = new Intent(getActivity(), ChannelPublishActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof VideoContent) {
                mBackgroundURI = ((VideoContent) item).getBackgroundImageURI();
                startBackgroundTimer();
            } else {
                /**
                 * make sure the background of other items are removed so the setting icon can be
                 * seen clearly
                 */
                mHandler.removeCallbacks(mBackgroudUpdateRunnable);
                mBackgroundManager.setDrawable(null);
            }
        }
    }

    /**
     * Helper functions for background image loading and associated animation
     */

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    protected void updateBackgroundImage(String uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;
        Glide.with(getActivity())
                .load(uri)
                .into(new SimpleTarget<Drawable>(width, height) {
                    @Override
                    public void onResourceReady(Drawable resource,
                                                Transition<? super Drawable> glideAnimation) {
                        mBackgroundManager.setDrawable(resource);
                    }
                });
        mHandler.removeCallbacks(mBackgroudUpdateRunnable);
    }

    private void startBackgroundTimer() {
        mHandler.removeCallbacks(mBackgroudUpdateRunnable);
        mHandler.postDelayed(mBackgroudUpdateRunnable, BACKGROUND_UPDATE_DELAY);
    }
}
