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

package android.support.v17.leanback.supportleanbackshowcase.app.room.controller.search;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.app.room.adapter.ListAdapter;
import android.support.v17.leanback.supportleanbackshowcase.app.room.db.entity.VideoEntity;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.adapter.qualifier.ListAdapterForRelatedRowQualifier;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.adapter.qualifier.SearchFragmentArrayObjectAdapterForRowsQualifier;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.androidinject.Injectable;
import android.support.v17.leanback.supportleanbackshowcase.app.room.network.NetworkLiveData;
import android.support.v17.leanback.supportleanbackshowcase.app.room.viewmodel.VideosViewModel;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.View;

import dagger.android.support.AndroidSupportInjection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;

public class SearchFragment extends android.support.v17.leanback.app.SearchSupportFragment
        implements android.support.v17.leanback.app.SearchSupportFragment.SearchResultProvider,
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
