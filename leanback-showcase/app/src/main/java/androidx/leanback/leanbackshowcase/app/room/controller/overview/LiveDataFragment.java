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

package androidx.leanback.leanbackshowcase.app.room.controller.overview;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.leanbackshowcase.app.room.adapter.ListAdapter;
import androidx.leanback.leanbackshowcase.app.room.config.AppConfiguration;
import androidx.leanback.leanbackshowcase.app.room.db.entity.CategoryEntity;
import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;
import androidx.leanback.leanbackshowcase.app.room.di.adapter.qualifier.ListAdapteWithLiveDataRowPresenterQualifier;
import androidx.leanback.leanbackshowcase.app.room.network.DownloadCompleteBroadcastReceiver;
import androidx.leanback.leanbackshowcase.app.room.network.DownloadingTaskDescription;
import androidx.leanback.leanbackshowcase.app.room.network.NetworkLiveData;
import androidx.leanback.leanbackshowcase.app.room.ui.LiveDataRowPresenter;
import androidx.leanback.leanbackshowcase.app.room.ui.LiveDataRowPresenter.DataLoadedListener;
import androidx.leanback.leanbackshowcase.app.room.ui.VideoCardPresenter;
import androidx.leanback.leanbackshowcase.app.room.viewmodel.VideosViewModel;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import dagger.android.support.AndroidSupportInjection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

// TOOD: why we need injectable interface here
// just like regular class it can specify some component to provide
// but if you don't use the dagger component in the code and call the inject(this) method,
// it is still basically nothing
// so if you don't have injectable interface in the class, no code androidinjection.inject() will
// be instrumented, so still nothing happens

// but dagger is sophisicated, it will check anyway
public class LiveDataFragment extends BrowseSupportFragment
        implements DownloadCompleteBroadcastReceiver.DownloadCompleteListener,
        LiveDataRowPresenter.DataLoadedListener {

    // For debugging purpose
    private static final Boolean DEBUG = false;
    private static final String TAG = "LiveDataFragment";

    // Resource category
    private static final String BACKGROUND = "background";
    private static final String CARD = "card";
    private static final String VIDEO = "video";

    @Inject
    @ListAdapteWithLiveDataRowPresenterQualifier
    ListAdapter<ListRow> mRowsAdapter;

    @Inject
    Map<Class, OnItemViewClickedListener> viewOnClickListenerMap;

    @Inject
    Map<Class, OnItemViewSelectedListener> viewOnSelectListenerMap;

    @Inject
    Map<Class, OnClickListener> onClickListener;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    VideoCardPresenter videoCardPresenter;

    public DataLoadedListener getDataLoadedListener(){
        return this;
    }

    private VideosViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);

        Log.e(TAG, "onCreate: " + " the map is: " + viewOnSelectListenerMap.get(this.getClass()));

        // set top level adapter
        setAdapter(mRowsAdapter);

        // retrive appropriate listener from listener map and set on it
        setOnItemViewClickedListener(viewOnClickListenerMap.get(this.getClass()));
        setOnItemViewSelectedListener(viewOnSelectListenerMap.get(this.getClass()));
        setOnSearchClickedListener(onClickListener.get(this.getClass()));

        // register broadcast receiver
        DownloadCompleteBroadcastReceiver.getInstance().registerListener(this);

        // tweak the ui
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setTitle(getString(R.string.livedata));

        // enable transition
        prepareEntranceTransition();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscribeNetworkInfo();
        mViewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
                .get(VideosViewModel.class);
        subscribeUi(mViewModel);
    }

    @Override
    public void onDataLoaded() {
        startEntranceTransition();
    }

    @Override
    public void onDownloadingCompleted(final DownloadingTaskDescription desc) {
        final VideoEntity videoEntity = desc.getVideo();
        switch (desc.getCategory()) {

            case VIDEO:
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {

                        if (AppConfiguration.IS_NETWORK_LATENCY_ENABLED) {
                            addLatency(3000L);
                        }
                        mViewModel.updateDatabase(videoEntity, VIDEO, desc.getStoragePath());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                "video " + videoEntity.getId() + " " +
                                        "downloaded",
                                Toast.LENGTH_SHORT).show();
                    }
                }.execute();
                break;

            case BACKGROUND:
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {

                        if (AppConfiguration.IS_NETWORK_LATENCY_ENABLED) {
                            addLatency(2000L);
                        }
                        mViewModel.updateDatabase(videoEntity, BACKGROUND, desc.getStoragePath());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                "background" + videoEntity.getId() + " " +
                                        "downloaded",
                                Toast.LENGTH_SHORT).show();
                    }
                }.execute();
                break;

            case CARD:
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {

                        if (AppConfiguration.IS_NETWORK_LATENCY_ENABLED) {
                            addLatency(1000L);
                        }
                        mViewModel.updateDatabase(videoEntity, CARD, desc.getStoragePath());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                "card " + videoEntity.getId() + " downloaded",
                                Toast.LENGTH_SHORT).show();
                    }
                }.execute();
                break;
        }
    }

    private void subscribeNetworkInfo() {
        NetworkLiveData.sync(getActivity())
                .observe((LifecycleOwner) getActivity(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if (aBoolean) {
                            getActivity().findViewById(R.id.no_internet).setVisibility(View.GONE);

                            // TODO: an appropriate method to re-create the database
                        } else {
                            getActivity().findViewById(R.id.no_internet)
                                    .setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void subscribeUi(final VideosViewModel viewModel) {
        viewModel.getAllCategories().observe((LifecycleOwner) getActivity(),
                new Observer<List<CategoryEntity>>() {
                    @Override
                    public void onChanged(@Nullable List<CategoryEntity> categoryEntities) {
                        if (categoryEntities != null) {
                            List<ListRow> rows = new ArrayList<>();

                            // The ListRow here cannot be used with dependency injection, since it
                            // rely on the run time information getCategoryName
                            for (CategoryEntity categoryEntity : categoryEntities) {
                                ListRow row = new ListRow(
                                        new HeaderItem(categoryEntity.getCategoryName()),
                                        new ListAdapter<>(videoCardPresenter));
                                rows.add(row);
                            }

                            mRowsAdapter.setItems(rows, new Comparator<ListRow>() {
                                @Override
                                public int compare(ListRow o1, ListRow o2) {
                                    return o1.getId() == o2.getId() ? 0 : -1;
                                }
                            }, new Comparator<ListRow>() {
                                @Override
                                public int compare(ListRow o1, ListRow o2) {
                                    return o1.getHeaderItem().getName()
                                            .equals(o2.getHeaderItem().getName()) ? 0 : -1;
                                }
                            });
                        }
                    }
                });
    }

    private void addLatency(Long ms) {
        try {
            // add 1s latency for video downloading, when network latency option
            // is enabled.
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            if (DEBUG) {
                Log.e(TAG, "doInBackground: ", e);
            }
        }
    }
}
