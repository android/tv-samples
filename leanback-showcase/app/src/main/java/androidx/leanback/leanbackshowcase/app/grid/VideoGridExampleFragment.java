/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package androidx.leanback.leanbackshowcase.app.grid;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.leanback.app.VerticalGridFragment;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.leanbackshowcase.app.media.MediaMetaData;
import androidx.leanback.leanbackshowcase.app.media.VideoExampleActivity;
import androidx.leanback.leanbackshowcase.cards.presenters.VideoCardViewPresenter;
import androidx.leanback.leanbackshowcase.models.VideoCard;
import androidx.leanback.leanbackshowcase.models.VideoRow;
import androidx.leanback.leanbackshowcase.cards.presenters.CardPresenterSelector;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.VerticalGridPresenter;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Example fragment displaying videos in a vertical grid using {@link VerticalGridFragment}.
 * It fetches the videos from the the url in {@link R.string.videos_url} and displays the metadata
 * fetched from each video in an ImageCardView (using {@link VideoCardViewPresenter}).
 * On clicking on each one of these video cards, a fresh instance of the
 * VideoExampleActivity starts which plays the video item.
 */
public class VideoGridExampleFragment extends VerticalGridFragment implements
        OnItemViewSelectedListener, OnItemViewClickedListener {

    private static final int COLUMNS = 4;
    private static final int ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_MEDIUM;
    private static final String TAG = "VideoGridExampleFragment";
    private static final String TAG_CATEGORY = "googlevideos";
    // Hashmap mapping category names to the list of videos in that category. This is fetched from
    // the url
    private Map<String, List<VideoCard>> categoryVideosMap = new HashMap<>();

    private ArrayObjectAdapter mAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.video_grid_example_title));
        setupRowAdapter();
    }
    private void setupRowAdapter() {
        VerticalGridPresenter videoGridPresenter = new VerticalGridPresenter(ZOOM_FACTOR);
        videoGridPresenter.setNumberOfColumns(COLUMNS);
        // note: The click listeners must be called before setGridPresenter for the event listeners
        // to be properly registered on the viewholders.
        setOnItemViewSelectedListener(this);
        setOnItemViewClickedListener(this);
        setGridPresenter(videoGridPresenter);

        PresenterSelector cardPresenterSelector = new CardPresenterSelector(getActivity());
        // VideoCardViewPresenter videoCardViewPresenter = new VideoCardViewPresenter(getActivity());
        mAdapter = new ArrayObjectAdapter(cardPresenterSelector);
        setAdapter(mAdapter);

        prepareEntranceTransition();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                createRows();
            }
        }, 1000);
    }

    private void createRows() {
        String urlToFetch = getResources().getString(R.string.videos_url);
        fetchVideosInfo(urlToFetch);
    }

    /**
     * Called when videos metadata are fetched from the url. The result of this fetch is returned
     * in the form of a JSON object.
     * @param jsonObj The json object containing the information about all the videos.
     */
    private void onFetchVideosInfoSuccess(JSONObject jsonObj) {
        try {
            String videoRowsJson = jsonObj.getString(TAG_CATEGORY);
            VideoRow[] videoRows = new Gson().fromJson(videoRowsJson, VideoRow[].class);
            for(VideoRow videoRow : videoRows) {
                if (!categoryVideosMap.containsKey(videoRow.getCategory())) {
                    categoryVideosMap.put(videoRow.getCategory(), new ArrayList<VideoCard>());
                }
                categoryVideosMap.get(videoRow.getCategory()).addAll(videoRow.getVideos());
                mAdapter.addAll(mAdapter.size(), videoRow.getVideos());
                startEntranceTransition();
            }
        } catch (JSONException ex) {
            Log.e(TAG, "A JSON error occurred while fetching videos: " + ex.toString());
        }
    }

    /**
     * Called when an exception occurred while fetching videos meta data from the url.
     * @param ex The exception occurred in the asynchronous task fetching videos.
     */
    private void onFetchVideosInfoError(Exception ex) {
        Log.e(TAG, "Error fetching videos from " + getResources().getString(R.string.videos_url) +
                ", Exception: " + ex.toString());
        Toast.makeText(getContext(), "Error fetching videos from json file",
                Toast.LENGTH_LONG).show();
    }

    /**
     * The result type of the background computation of the url fetcher
     */
    private static class FetchResult {
        private boolean isSuccess;
        private Exception exception;
        JSONObject jsonObj;

        FetchResult(JSONObject obj) {
            jsonObj = obj;
            isSuccess = true;
            exception = null;
        }

        FetchResult(Exception ex) {
            jsonObj = null;
            isSuccess = false;
            exception = ex;
        }
    }

    /**
     * Fetches videos metadata from urlString on a background thread. Callback methods are invoked
     * upon success or failure of this fetching.
     * @param urlString The json file url to fetch from
     */
    private void fetchVideosInfo(final String urlString) {

        new AsyncTask<Void, Void, FetchResult>() {
            @Override
            protected void onPostExecute(FetchResult fetchResult) {
                if (fetchResult.isSuccess) {
                    onFetchVideosInfoSuccess(fetchResult.jsonObj);
                } else {
                    onFetchVideosInfoError(fetchResult.exception);
                }
            }

            @Override
            protected FetchResult doInBackground(Void... params) {
                BufferedReader reader = null;
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    reader = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream(),
                                    StandardCharsets.UTF_8));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return new FetchResult(new JSONObject(sb.toString()));
                } catch (JSONException ex) {
                    Log.e(TAG, "A JSON error occurred while fetching videos: " + ex.toString());
                    return new FetchResult(ex);
                } catch (IOException ex) {
                    Log.e(TAG, "An I/O error occurred while fetching videos: " + ex.toString());
                    return new FetchResult(ex);
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException ex) {
                            Log.e(TAG, "JSON reader could not be closed! " + ex);
                        }
                    }
                }
            }
        }.execute();
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                              RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (item instanceof  VideoCard) {
            VideoCard itemCard = (VideoCard) item;
            List<String> videoSources = itemCard.getVideoSources();
            if (videoSources == null || videoSources.isEmpty()) {
                return;
            }

            MediaMetaData metaData = new MediaMetaData();
            metaData.setMediaSourcePath(videoSources.get(0));
            metaData.setMediaTitle(itemCard.getTitle());
            metaData.setMediaArtistName(itemCard.getDescription());
            metaData.setMediaAlbumArtUrl(itemCard.getImageUrl());
            Intent intent = new Intent(getActivity(), VideoExampleActivity.class);
            intent.putExtra(VideoExampleActivity.TAG, metaData);
            intent.setData(Uri.parse(metaData.getMediaSourcePath()));
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                               RowPresenter.ViewHolder rowViewHolder, Row row) {

    }
}
