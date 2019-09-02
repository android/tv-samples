/*
 * Copyright (C) 2015 The Android Open Source Project
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

package androidx.leanback.leanbackshowcase.cards.presenters;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.leanbackshowcase.models.Card;
import androidx.leanback.widget.ImageCardView;

/**
 * This Presenter will display a card which consists of a big image followed by a colored footer.
 * Not only the colored footer is unique to this card, but also it's footer (info) will be visible
 * even when its parent row is inactive.
 */
public class SingleLineCardPresenter extends ImageCardViewPresenter {

    public SingleLineCardPresenter(Context context) {
        super(context, R.style.SingleLineCardTheme);
    }

    @Override public void onBindViewHolder(Card card, ImageCardView cardView) {
        super.onBindViewHolder(card, cardView);
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(R.styleable.lbImageCardView);
        android.util.Log.d("SHAAN", "lbImageCardViewType ="+typedArray.getInt(R.styleable.lbImageCardView_lbImageCardViewType, -1));
        cardView.setInfoAreaBackgroundColor(card.getFooterColor());
    }

}
