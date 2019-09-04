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

import android.content.Context;
import android.content.res.Resources;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;
import androidx.core.content.ContextCompat;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

/**
 * A specialized VideoContentCardPresenter to generate Views and bind Objects to them on demand.
 * <p>
 * It will change background color/ card image accordingly
 */
public class VideoContentCardPresenter extends Presenter {
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Context context = parent.getContext();
        sDefaultBackgroundColor = ContextCompat.getColor(context, R.color.default_background);
        sSelectedBackgroundColor = ContextCompat.getColor(context, R.color.selected_background);

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
        return new ViewHolder(cardView);
    }

    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        VideoContent clip = (VideoContent) item;
        if (clip.getCardImageUrl() != null) {
            ImageCardView cardView = (ImageCardView) viewHolder.view;
            Resources resources = cardView.getContext().getResources();
            cardView.setTitleText(clip.getTitle());
            cardView.setContentText(clip.getDescription());
            int cardWidth = Math.round(resources.getDimensionPixelSize(R.dimen.card_width)
            );
            int cardHeight = resources.getDimensionPixelSize(R.dimen.card_height);
            cardView.setMainImageDimensions(cardWidth, cardHeight);
            Glide.with(viewHolder.view.getContext())
                    .load(clip.getCardImageUrl())
                    .into(cardView.getMainImageView());
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}
