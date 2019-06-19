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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.leanback.app.GuidedStepFragment;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * This fragment is designed specific for user to choose which channel they
 * want to publish/ un-publish
 * <p>
 * It extends the GuidedStepFragment to share the similar UI as GuidedStepFragment
 */
public class PublishChannelFragment extends GuidedStepFragment {
    /**
     * Tracking the action which is currently selected by the user
     */
    private ChannelContents mSelectedChannelContents;
    private static final String TAG = "PublishChannelFragment";
    private static final boolean DEBUG = true;

    /**
     * Request code when add channel activity (UI) is started
     */
    private static final int ADD_CHANNEL_REQUEST = 1;

    /**
     * Bitmap which will be put at the front of each checkbox
     */
    private static final int OPTION_DRAWABLE = R.drawable.row_app_banner;

    /**
     * Helper function to add non-checked Action to this fragment
     */
    private static void addAction(List<GuidedAction> actions, Context context, long id,
                                  String title, String desc) {
        actions.add(new GuidedAction.Builder(context)
                .id(id)
                .title(title)
                .description(desc)
                .build());
    }

    /**
     * Helper function to add checked Action to this fragment
     * <p>
     * In this fragment, the checked action is customized as checkbox
     */
    private static void addCheckedAction(List<GuidedAction> actions,
                                         int iconResId,
                                         Context context,
                                         String title,
                                         String desc,
                                         int id,
                                         boolean isActionChecked) {
        GuidedAction guidedAction = new GuidedAction.Builder(context)
                .title(title)
                .description(desc)
                .checkSetId(GuidedAction.CHECKBOX_CHECK_SET_ID)
                .icon(context.getResources().getDrawable(iconResId))
                .build();
        guidedAction.setId(id);
        /**
         * Set checkbox status to false initially
         */
        guidedAction.setChecked(isActionChecked);
        actions.add(guidedAction);
    }

    /**
     * Using different theme as attached activity for consistent UI design requirement
     * <p>
     * The theme can be customized in themes.xml
     */
    @Override
    public int onProvideTheme() {
        return R.style.Theme_Leanback_GuidedStep;
    }

    /**
     * This function will be executed every time when the permission request activity is finished.
     * <p>
     * If the answer allows to add channel to main screen, Async task LoadAddedChannels will be
     * executed to load which channel has been published to main screen.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_CHANNEL_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (DEBUG) {
                    Log.d(TAG, "channel added");
                }

                /**
                 * Every time when add channel activity is finished, the LoadAddedChannels async task
                 * will be executed to fetch channels' publish status
                 */
                Toast.makeText(this.getActivity(), "Channel Added!", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "could not add channel");
            }
        }
    }

    /**
     * The list of channel contents, obtained from ChannelContents class
     */
    private List<ChannelContents> mChannelContents;

    @Override
    @NonNull
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.setting_page_title);
        String breadcrumb = getString(R.string.setting_page_breadcrumb);
        String description = getString(R.string.setting_page_description);
        Drawable icon = getActivity().getDrawable(R.drawable.title_android_tv);
        return new GuidanceStylist.Guidance(title, description, breadcrumb, icon);
    }

    @Override
    public GuidanceStylist onCreateGuidanceStylist() {
        return new GuidanceStylist() {
            @Override
            public int onProvideLayoutId() {
                return R.layout.setting_page;
            }
        };
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        ChannelContents.initializePlaylists(this.getActivity());

        mChannelContents = ChannelContents.sChannelContents;

        /**
         * The id of check box was set to the index of current channel
         */
        for (int i = 0; i < mChannelContents.size(); i++) {
            addCheckedAction(actions,
                    OPTION_DRAWABLE,
                    getActivity(),
                    mChannelContents.get(i).getName(),
                    mChannelContents.get(i).getDescription(),
                    i,
                    mChannelContents.get(i).isChannelPublished());
        }
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        /**
         * Find channel through action ID
         */
        int currentId = (int) action.getId();
        mSelectedChannelContents = mChannelContents.get(currentId);

        /**
         * When the action cannot select valid mediaItem, just return from from this function
         */
        if (mSelectedChannelContents == null) {
            return;
        }

        /**
         * Add/ Remove channel from Home Screen using Async task
         * to make sure the UI thread will not be blocked
         */
        if (action.isChecked()) {
            /**
             * Create and execute the async task to add channel on home screen
             * Always update publish status through LoadAddedChannels task
             */
            new ChannelContents.CreateChannelInMainScreen(getActivity()).execute(mSelectedChannelContents);
        } else {
            Toast.makeText(this.getActivity(),
                    getResources().getString(R.string.channel_removed_from_home_screen),
                    Toast.LENGTH_SHORT).show();
            /**
             * Create and execute the async task to remove channel on home screen
             * Always update publish status through LoadAddedChannels task
             */
            new ChannelContents.RemoveChannelInMainScreen(getActivity()).execute(mSelectedChannelContents);
        }
    }
}

