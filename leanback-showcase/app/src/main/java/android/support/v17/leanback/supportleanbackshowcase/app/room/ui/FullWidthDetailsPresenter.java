package android.support.v17.leanback.supportleanbackshowcase.app.room.ui;

import android.support.v17.leanback.supportleanbackshowcase.R;
import android.support.v17.leanback.supportleanbackshowcase.app.room.controller.app.SampleApplication;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.RowPresenter;
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
