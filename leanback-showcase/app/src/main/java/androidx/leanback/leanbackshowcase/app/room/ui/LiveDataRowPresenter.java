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


package androidx.leanback.leanbackshowcase.app.room.ui;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.leanback.leanbackshowcase.app.room.adapter.ListAdapter;
import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;
import androidx.leanback.leanbackshowcase.app.room.viewmodel.VideosInSameCategoryViewModel;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.RowPresenter;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * The presenter for live data row
 */
public class LiveDataRowPresenter extends ListRowPresenter {

    private ListRow mRow;
    private List<DataLoadedListener> mDataLoadedListeners;
    private LifecycleOwner mLifecycleOwner;

    private ViewModelProvider.Factory viewModelFactory;

    public LiveDataRowPresenter(ViewModelProvider.Factory factory) {
        super();
        mDataLoadedListeners = new ArrayList<>();
        viewModelFactory = factory;
    }

    public interface DataLoadedListener {
        void onDataLoaded();
    }

    /**
     * Register Entrance Listener.
     */
    public void registerDataLoadedListener(DataLoadedListener listener) {
        mDataLoadedListeners.add(listener);
    }

    /**
     * Dispatch the even when the data is bound to the adapter.
     */
    public void notifyDataLoaded() {
        for (int i = 0; i < mDataLoadedListeners.size(); i++) {
            mDataLoadedListeners.get(i).onDataLoaded();
        }
    }

    @Override
    protected LiveDataRowPresenterViewHolder createRowViewHolder(ViewGroup parent) {
        ListRowPresenter.ViewHolder listRowPresenterViewHolder
                = (ListRowPresenter.ViewHolder) super.createRowViewHolder(parent);
        return new LiveDataRowPresenterViewHolder(
                listRowPresenterViewHolder.view,
                listRowPresenterViewHolder.getGridView(),
                listRowPresenterViewHolder.getListRowPresenter());
    }


    @Override
    protected void onBindRowViewHolder(RowPresenter.ViewHolder holder, Object item) {
        super.onBindRowViewHolder(holder, item);
        mRow = (ListRow) item;
        LiveDataRowPresenterViewHolder vh = (LiveDataRowPresenterViewHolder)holder;

        String category = mRow.getHeaderItem().getName();

        final ListAdapter<VideoEntity> adapter = (ListAdapter<VideoEntity>) mRow.getAdapter();

        FragmentActivity attachedFragmentActivity = (FragmentActivity) holder.view.getContext();

        // In our case, attached activity should be a lifecycle owner
        mLifecycleOwner = (LifecycleOwner) attachedFragmentActivity;


        // view model will not be re-created as long as the lifecycle owner
        // lifecycle observer and tag doesn't change

        VideosInSameCategoryViewModel viewModel = ViewModelProviders.of(attachedFragmentActivity, viewModelFactory).get(VideosInSameCategoryViewModel.class);


        // bind live data to view holder
        vh.setLiveData(viewModel.getVideosInSameCategory(category));

        // observe the live data when this row is bound to view holder
        vh.getLiveData().observe(mLifecycleOwner,
                new Observer<List<VideoEntity>>() {
                    @Override
                    public void onChanged(
                            @Nullable List<VideoEntity> videoEntities) {
                        if (videoEntities != null) {

                            // When the data is bound to the adapter, dispatch start Entrance
                            // transition event
                            notifyDataLoaded();

                            adapter.setItems(videoEntities,
                                    new Comparator<VideoEntity>() {
                                        @Override
                                        public int compare(VideoEntity o1,
                                                           VideoEntity o2) {
                                            return o1.getId() == o2.getId() ? 0 : -1;
                                        }
                                    }, new Comparator<VideoEntity>() {
                                        @Override
                                        public int compare(VideoEntity o1,
                                                           VideoEntity o2) {
                                            return o1.equals(o2) ? 0 : -1;
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    protected void onUnbindRowViewHolder(RowPresenter.ViewHolder holder) {
        super.onUnbindRowViewHolder(holder);
        LiveDataRowPresenterViewHolder vh = (LiveDataRowPresenterViewHolder)holder;
        vh.getLiveData().removeObservers(mLifecycleOwner);
    }

    /**
     * Extend view holder to hold the live data
     */
    private class LiveDataRowPresenterViewHolder extends ListRowPresenter.ViewHolder {

        private LiveData<List<VideoEntity>> mLiveData;

        public LiveDataRowPresenterViewHolder(View rootView, HorizontalGridView gridView, ListRowPresenter p) {
            super(rootView, gridView, p);
        }


        public void setLiveData(LiveData<List<VideoEntity>> liveData) {
            mLiveData = liveData;
        }

        public final LiveData<List<VideoEntity>> getLiveData() {
            return mLiveData;
        }
    }
}
