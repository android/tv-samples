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

package androidx.leanback.leanbackshowcase.app.room.controller.search;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.leanbackshowcase.app.room.adapter.ListAdapter;
import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;
import androidx.leanback.leanbackshowcase.app.room.di.adapter.qualifier.ListAdapterForRelatedRowQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.adapter.qualifier.SearchFragmentArrayObjectAdapterForRowsQualifier;
import androidx.leanback.leanbackshowcase.app.room.di.androidinject.Injectable;
import androidx.leanback.leanbackshowcase.app.room.network.NetworkLiveData;
import androidx.leanback.leanbackshowcase.app.room.viewmodel.VideosViewModel;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.core.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.View;

import dagger.android.support.AndroidSupportInjection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class SearchFragment extends androidx.leanback.app.SearchSupportFragment
        implements androidx.leanback.app.SearchSupportFragment.SearchResultProvider,
        Injectable {


    @Inject
    Map<Class, OnItemViewClickedListener> viewOnClickListenerMap;

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    @Inject
    @SearchFragmentArrayObjectAdapterForRowsQualifier
    ArrayObjectAdapter mRowsAdapter;

    @Inject
    @ListAdapterForRelatedRowQualifier
    ListAdapter<VideoEntity> mRelatedAdapter;

    private VideosViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        setBadgeDrawable(ResourcesCompat.getDrawable(getActivity().getResources(),
                R.drawable.ic_add_row_circle_black_24dp, getActivity().getTheme()));

        setTitle("Search Using Live Data");
        setSearchResultProvider(this);
        setOnItemViewClickedListener(viewOnClickListenerMap.get(this.getClass()));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
                .get(VideosViewModel.class);

        subscribeUi(mViewModel);
        subscribeNetwork();
    }

    private void subscribeNetwork() {
        NetworkLiveData.sync(this.getActivity())
                .observe((LifecycleOwner) this.getActivity(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean isNetworkAvailable) {
                        if (isNetworkAvailable) {
                            getActivity().findViewById(R.id.no_internet_search)
                                    .setVisibility(View.GONE);
                        } else {
                            getActivity().findViewById(R.id.no_internet_search)
                                    .setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {

        if (!TextUtils.isEmpty(newQuery) && !newQuery.equals("nil")) {
            getActivity().findViewById(R.id.search_progressbar).setVisibility(View.VISIBLE);
            newQuery = "%" + newQuery + "%";
            mViewModel.setQueryMessage(newQuery);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        if (!TextUtils.isEmpty(query) && !query.equals("nil")) {
            getActivity().findViewById(R.id.search_progressbar).setVisibility(View.VISIBLE);
            query = "%" + query + "%";
            mViewModel.setQueryMessage(query);
        }
        return true;
    }

    private void subscribeUi(VideosViewModel viewModel) {
        viewModel.getSearchResult()
                .observe((LifecycleOwner) this.getActivity(), new Observer<List<VideoEntity>>() {
                    @Override
                    public void onChanged(@Nullable List<VideoEntity> videoEntities) {

                        if (videoEntities != null && !videoEntities.isEmpty()) {
                            getActivity().findViewById(R.id.no_search_result)
                                    .setVisibility(View.GONE);
                            getActivity().findViewById(R.id.search_progressbar)
                                    .setVisibility(View.GONE);
                            mRelatedAdapter.setItems(videoEntities, new Comparator<VideoEntity>() {
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
                        } else {
                            // When the search result is null (when data base has not been created) or
                            // empty, the text view field will be visible and telling user that no search
                            // result is available
                            getActivity().findViewById(R.id.no_search_result)
                                    .setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
