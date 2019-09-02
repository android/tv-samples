/*
 * Copyright 2019 Google LLC
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

import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.leanbackshowcase.app.room.controller.app.SampleApplication;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowPresenter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jingjiangli on 9/20/17.
 */

public class FullWidthDetailsPresenter extends FullWidthDetailsOverviewRowPresenter{

  public FullWidthDetailsPresenter(Presenter detailsPresenter) {
    super(detailsPresenter);
  }

  @Override
  protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
    // Customize Actionbar and Content by using custom colors.
    RowPresenter.ViewHolder viewHolder = super.createRowViewHolder(parent);

    View actionsView = viewHolder.view.
        findViewById(R.id.details_overview_actions_background);
    actionsView.setBackgroundColor(SampleApplication.getInstance().
        getColor(R.color.detail_view_actionbar_background));

    View detailsView = viewHolder.view.findViewById(R.id.details_frame);
    detailsView.setBackgroundColor(
        SampleApplication.getInstance().getResources().getColor(R.color.detail_view_background));
    return viewHolder;
  }
}
