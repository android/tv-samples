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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import androidx.annotation.Nullable;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.leanbackshowcase.app.room.config.AppConfiguration;
import androidx.leanback.leanbackshowcase.app.room.db.entity.VideoEntity;
import androidx.leanback.leanbackshowcase.app.room.network.NetworkLiveData;
import androidx.leanback.leanbackshowcase.app.room.network.NetworkManagerUtil;
import androidx.leanback.leanbackshowcase.app.room.network.PermissionLiveData;
import androidx.leanback.leanbackshowcase.app.room.viewmodel.VideosViewModel;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.PopupMenu;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import javax.inject.Inject;

/**
 * The video card presenter which can respond to long click action and present current video's
 * state (Downloading/ Removing/ Downloaded)
 */
public class VideoCardPresenter extends Presenter {

    // For debugging purpose
    private static final boolean DEBUG = true;
    private static final String TAG =  "VideoCardPresenter";

    // String constant
    private static final String VIDEO = "video";
    private static final String BACKGROUND = "background";
    private static final String CARD = "card";
    private static final String STATUS = "status";
    private static final String DOWNLOADING = "downloading";
    private static final String REMOVING = "removing";
    private static final String REMOVE_LOCAL_VIDEO = "Remove Local Video";
    private static final String DOWNLOADED = " (Downloaded)";
    private static final String RENTED = " (rented)";
    private static final String DOWNLOAD_VIDEO = "Download Video";
    private static final String DOWNLOAD_VIDEO_NO_PERMISSION = "Download Video (No Permission)";
    private static final String DOWNLOAD_VIDEO_NO_NETWORK = "Download Video (No Network)";

    // The default resource when the network or local content are not available.
    private static int sSelectedBackgroundColor = -1;
    private static int sDefaultBackgroundColor = -1;
    private static Drawable sDefaultCardImage;

    @Inject
    public VideoCardPresenter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Context mContext = parent.getContext();
        sDefaultBackgroundColor =
                ContextCompat.getColor(parent.getContext(), R.color.default_background);
        sSelectedBackgroundColor =
                ContextCompat.getColor(parent.getContext(), R.color.selected_background);
        sDefaultCardImage =
                parent.getResources().getDrawable(R.drawable.no_cache_no_internet, null);
        ImageCardView cardView = new ImageCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new CardViewHolder(cardView, mContext);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Object item) {
        VideoEntity video = (VideoEntity) item;
        CardViewHolder cardViewHolder = (CardViewHolder) viewHolder;
        cardViewHolder.bind(video);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;

        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }

    /**
     * Helper function to update selected video clip's background color. Info field should also
     * be updated for consistent ui.
     *
     * @param view
     * @param selected
     */
    private void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;

        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    /**
     * The view holder which will encapsulate all the information related to currently bond video.
     */
    private final class CardViewHolder extends ViewHolder implements
            View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {
        private VideoEntity mVideo;
        private Context mContext;
        private PopupMenu mPopupMenu;
        private FragmentActivity mFragmentActivity;
        private LifecycleOwner mOwner;


        // when glide library cannot fetch data from internet, and there is no local content, it
        // will be used as place holder
        private RequestOptions mDefaultPlaceHolder;
        private Drawable mDefaultBackground;

        private ImageCardView mCardView;

        private VideosViewModel mViewModel;


        CardViewHolder(ImageCardView view, Context context) {
            super(view);
            mContext = context;
            Context wrapper = new ContextThemeWrapper(mContext, R.style.MyPopupMenu);
            mPopupMenu = new PopupMenu(wrapper, view);
            mPopupMenu.inflate(R.menu.popup_menu);

            mPopupMenu.setOnMenuItemClickListener(this);
            view.setOnLongClickListener(this);

            mOwner = (LifecycleOwner) mContext;

            mDefaultBackground = mContext.getResources().getDrawable(R.drawable.no_cache_no_internet, null);
            mDefaultPlaceHolder = new RequestOptions().
                    placeholder(mDefaultBackground);

            mCardView = (ImageCardView) CardViewHolder.this.view;
            Resources resources = mCardView.getContext().getResources();
            mCardView.setMainImageDimensions(Math.round(
                    resources.getDimensionPixelSize(R.dimen.card_width)),
                    resources.getDimensionPixelSize(R.dimen.card_height));

            mFragmentActivity = (FragmentActivity) context;
            mViewModel = ViewModelProviders.of(mFragmentActivity).get(VideosViewModel.class);
        }

        @Override
        public boolean onLongClick(View v) {
            mPopupMenu.show();
            return true;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.download_video_related_resource:
                    new AsyncTask<VideoEntity, Void, Void>() {
                        VideoEntity mVideoEntity;

                        @Override
                        protected Void doInBackground(VideoEntity... videos) {
                            mVideoEntity = videos[0];

                            // View Model will talk to the repository which will be responsible
                            // for all interaction with room database
                            mViewModel.updateDatabase(mVideoEntity, STATUS, DOWNLOADING);
                            return null;
                        }
                    }.execute(mVideo);
                    NetworkManagerUtil.download(mVideo);
                    return true;
                case R.id.remove_video_related_resource:
                    new AsyncTask<VideoEntity, Void, Void>() {
                        VideoEntity mVideoEntity;

                        @Override
                        protected Void doInBackground(VideoEntity... videos) {
                            mVideoEntity = videos[0];
                            mViewModel.updateDatabase(mVideoEntity, STATUS, REMOVING);
                            return null;
                        }
                    }.execute(mVideo);
                    RemoveFile();
                    return true;
                default:
                    return false;
            }
        }


        private void bind(VideoEntity video) {
            if (DEBUG) {
                Log.e(TAG, "bind: " + video);
            }
            mVideo = video;

            if (!video.isRented()) {
                mCardView.setTitleText(video.getTitle());
            } else {
                mCardView.setTitleText(video.getTitle() + RENTED);
            }

            if (isRemovable()) {
                mCardView.setContentText(video.getStudio() + DOWNLOADED);
            } else if (!video.getStatus().isEmpty() && !isDownloadable()) {
                mCardView.setContentText(video.getStudio() + " (" + video.getStatus() + ")");
            } else {
                mCardView.setContentText(video.getStudio());
            }

            String loadedUri;
            if (!video.getVideoCardImageLocalStorageUrl().isEmpty()) {
                loadedUri = video.getVideoCardImageLocalStorageUrl();
            } else {
                loadedUri = video.getCardImageUrl();
            }
            if (video.getCardImageUrl() != null) {
                Glide.with(mCardView.getContext())
                        .load(loadedUri)
                        .apply(mDefaultPlaceHolder)
                        .into(mCardView.getMainImageView());
            }

            updatePopMenu(video);
        }

        /**
         * Helper function to update pop up menu's item based on network environment and video
         * entity's status
         *
         * @param videoEntity
         */
        private void updatePopMenu(final VideoEntity videoEntity) {
            if (isDownloadable()) {
                setInvisible(R.id.remove_video_related_resource);

                PermissionLiveData.get().observe(mOwner, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if (!aBoolean) {
                            updatePopupMenuItem(R.id.download_video_related_resource, false,
                                    DOWNLOAD_VIDEO_NO_PERMISSION);
                            return;
                        }
                    }
                });

                NetworkLiveData.sync(mContext).observe(mOwner, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean isNetworkAvailable) {
                        if (isNetworkAvailable) {
                            updatePopupMenuItem(R.id.download_video_related_resource, true,
                                    DOWNLOAD_VIDEO);
                        } else {
                            updatePopupMenuItem(R.id.download_video_related_resource, false,
                                    DOWNLOAD_VIDEO_NO_NETWORK);
                        }
                    }
                });
            } else if (isRemovable()) {
                updatePopupMenuItem(R.id.remove_video_related_resource, true, REMOVE_LOCAL_VIDEO);
                setInvisible(R.id.download_video_related_resource);
            } else {
                updatePopupMenuItem(R.id.download_video_related_resource, false,
                        videoEntity.getStatus());
                setInvisible(R.id.remove_video_related_resource);
            }
        }

        private void updatePopupMenuItem(int id, boolean enabled, String title) {
            mPopupMenu.getMenu().findItem(id).setVisible(true).setTitle(title).setEnabled(enabled);
        }

        private void setInvisible(int id) {
            mPopupMenu.getMenu().findItem(id).setVisible(false);
        }


        private void RemoveFile() {
            new FileRemoving().execute(new VideoWithCategory(VIDEO, mVideo));
            new FileRemoving().execute(new VideoWithCategory(CARD, mVideo));
            new FileRemoving().execute(new VideoWithCategory(BACKGROUND, mVideo));
        }

        /**
         * When all the local storage paths (including video content, background and card image )
         * for the video entity is empty, and current working status is not downloading. It means
         * user can perform download video entity operation at this point
         *
         * @return If user can perform download video operation or not.
         */
        private boolean isDownloadable() {
            return mVideo.getVideoCardImageLocalStorageUrl().isEmpty()
                    && mVideo.getVideoBgImageLocalStorageUrl().isEmpty()
                    && mVideo.getVideoLocalStorageUrl().isEmpty()
                    && !mVideo.getStatus().equals(DOWNLOADING);
        }

        /**
         * When all the local storage paths (including video content, background and card image )
         * for the video entity is not empty, and current working status is not removing. It means
         * user can perform remove video entity operation at this point
         *
         * @return If user can perform remove video operation or not.
         */
        private boolean isRemovable() {
            return !mVideo.getVideoCardImageLocalStorageUrl().isEmpty()
                    && !mVideo.getVideoBgImageLocalStorageUrl().isEmpty()
                    && !mVideo.getVideoLocalStorageUrl().isEmpty()
                    && !mVideo.getStatus().equals(REMOVING);
        }

        private class VideoWithCategory {
            private String mCategory;
            private VideoEntity mVideo;

            public VideoWithCategory(String category, VideoEntity video) {
                this.mCategory = category;
                this.mVideo = video;
            }

            public String getCategory() {
                return mCategory;
            }


            public VideoEntity getVideo() {
                return mVideo;
            }

        }

        private class FileRemoving extends AsyncTask<VideoWithCategory, Void, Void> {
            private static final int VIDEO_PATH_START_INDEX = 6;
            private String cat;
            private String url;
            private long id;

            @Override
            protected Void doInBackground(VideoWithCategory... videos) {
                VideoWithCategory par = videos[0];
                cat = par.getCategory();
                id = par.getVideo().getId();
                switch (cat) {
                    case BACKGROUND:
                        url = par.getVideo().getVideoBgImageLocalStorageUrl().substring(VIDEO_PATH_START_INDEX);
                        break;
                    case CARD:
                        url = par.getVideo().getVideoCardImageLocalStorageUrl().substring(VIDEO_PATH_START_INDEX);
                        break;
                    case VIDEO:
                        url = par.getVideo().getVideoLocalStorageUrl().substring(VIDEO_PATH_START_INDEX);
                        break;
                }
                File fileToDelete = new File(url);
                if (fileToDelete.exists()) {
                    fileToDelete.delete();
                    if (AppConfiguration.IS_FILE_OPERATION_LATENCY_ENABLED) {
                        switch (cat) {
                            case BACKGROUND:
                                addLatency(1000L);
                                break;
                            case CARD:
                                addLatency(2000L);
                                break;
                            case VIDEO:
                                addLatency(3000L);
                                break;
                        }
                    }
                    mViewModel.updateDatabase(par.getVideo(), cat, "");
                } else {
                    if (DEBUG) {
                        Log.e(TAG, "doInBackground (delete file): " + url + " cannot find file");
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                switch (cat) {
                    case BACKGROUND:
                        Toast.makeText(mContext, "bg " + id + " removed", Toast.LENGTH_SHORT).show();
                        break;
                    case CARD:
                        Toast.makeText(mContext, "card " + id + " removed", Toast.LENGTH_SHORT).show();
                        break;
                    case VIDEO:
                        Toast.makeText(mContext, "video " + id + " removed", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            private void addLatency(Long ms) {
                try {
                    // add 1s latency for video downloading, when network latency option
                    // is enabled.
                    Thread.sleep(ms);
                } catch (InterruptedException e) {
                    if (DEBUG) {
                        Log.e(TAG, "doInBackground (add latency): ", e);
                    }
                }
            }
        }
    }
}
