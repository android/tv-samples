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

package androidx.leanback.leanbackshowcase.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.ActivityOptionsCompat;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.leanbackshowcase.app.cards.CardExampleActivity;
import androidx.leanback.leanbackshowcase.app.details.DetailViewExampleActivity;
import androidx.leanback.leanbackshowcase.app.details.DetailViewExampleWithVideoBackgroundActivity;
import androidx.leanback.leanbackshowcase.app.dialog.DialogExampleActivity;
import androidx.leanback.leanbackshowcase.app.grid.GridExampleActivity;
import androidx.leanback.leanbackshowcase.app.grid.VideoGridExampleActivity;
import androidx.leanback.leanbackshowcase.app.media.MusicExampleActivity;
import androidx.leanback.leanbackshowcase.app.media.VideoExampleActivity;
import androidx.leanback.leanbackshowcase.app.media.VideoExampleWithExoPlayerActivity;
import androidx.leanback.leanbackshowcase.app.page.PageAndListRowActivity;
import androidx.leanback.leanbackshowcase.app.room.controller.overview.LiveDataRowsActivity;
import androidx.leanback.leanbackshowcase.app.rows.DynamicVideoRowsActivity;
import androidx.leanback.leanbackshowcase.app.settings.SettingsExampleActivity;
import androidx.leanback.leanbackshowcase.app.wizard.WizardExampleActivity;
import androidx.leanback.leanbackshowcase.cards.presenters.CardPresenterSelector;
import androidx.leanback.leanbackshowcase.models.Card;
import androidx.leanback.leanbackshowcase.models.CardRow;
import androidx.leanback.leanbackshowcase.models.Movie;
import androidx.leanback.leanbackshowcase.utils.Utils;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import com.google.gson.Gson;


public class MainFragment extends BrowseSupportFragment {

    private ArrayObjectAdapter mRowsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUIElements();
        setupRowAdapter();
        setupEventListeners();
    }

    private void setupRowAdapter() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        createRows();
        setAdapter(mRowsAdapter);
    }

    private void createRows() {
        String json = Utils
                .inputStreamToString(getResources().openRawResource(R.raw.launcher_cards));
        CardRow[] rows = new Gson().fromJson(json, CardRow[].class);
        for (CardRow row : rows) {
            mRowsAdapter.add(createCardRow(row));
        }
    }

    private ListRow createCardRow(CardRow cardRow) {
        PresenterSelector presenterSelector = new CardPresenterSelector(getActivity());
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenterSelector);
        for (Card card : cardRow.getCards()) {
            listRowAdapter.add(card);
        }
        return new ListRow(listRowAdapter);
    }

    private void setupUIElements() {
        setTitle(getString(R.string.browse_title));
        setBadgeDrawable(getResources().getDrawable(R.drawable.title_android_tv, null));
        setHeadersState(HEADERS_DISABLED);
        setHeadersTransitionOnBackEnabled(false);
        setBrandColor(getResources().getColor(R.color.fastlane_background));
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {

        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            Intent intent = null;
            Card card = (Card) item;
            int id = card.getId();
            switch (id) {
                case 0: {
                    intent = new Intent(getActivity().getBaseContext(),
                            CardExampleActivity.class);
                    break;
                }
                case 1:
                    intent = new Intent(getActivity().getBaseContext(),
                            PageAndListRowActivity.class);
                    break;
                case 2: {
                    intent = new Intent(getActivity().getBaseContext(),
                            GridExampleActivity.class);
                    break;
                }
                case 3: {
                    intent = new Intent(getActivity().getBaseContext(),
                            VideoGridExampleActivity.class);
                    break;
                }
                case 4: {
                    intent = new Intent(getActivity().getBaseContext(),
                            DetailViewExampleActivity.class);
                    break;
                }
                case 5: {
                    intent = new Intent(getActivity().getBaseContext(),
                            DetailViewExampleWithVideoBackgroundActivity.class);
                    break;
                }
                case 6: {
                    intent = new Intent(getActivity().getBaseContext(),
                            VideoExampleActivity.class);
                    break;
                }
                case 7: {
                    intent = new Intent(getActivity().getBaseContext(),
                            VideoExampleWithExoPlayerActivity.class);
                    break;
                }
                case 8: {
                    intent = new Intent(getActivity().getBaseContext(),
                            MusicExampleActivity.class);
                    break;
                }
                case 9: {
                    // Let's create a new Wizard for a given Movie. The movie can come from any sort
                    // of data source. To simplify this example we decode it from a JSON source
                    // which might be loaded from a server in a real world example.
                    intent = new Intent(getActivity().getBaseContext(),
                            WizardExampleActivity.class);

                    // Prepare extras which contains the Movie and will be passed to the Activity
                    // which is started through the Intent/.
                    Bundle extras = new Bundle();
                    String json = Utils.inputStreamToString(
                            getResources().openRawResource(R.raw.wizard_example));
                    Movie movie = new Gson().fromJson(json, Movie.class);
                    extras.putSerializable("movie", movie);
                    intent.putExtras(extras);

                    // Finally, start the wizard Activity.
                    break;
                }
                case 10: {
                    intent = new Intent(getActivity().getBaseContext(),
                            SettingsExampleActivity.class);
                    startActivity(intent);
                    return;
                }
                case 11: {
                    intent = new Intent(getActivity().getBaseContext(),
                            DialogExampleActivity.class);
                    break;
                }
                case 12: {
                    intent = new Intent(getActivity().getBaseContext(),
                            DynamicVideoRowsActivity.class);
                    startActivity(intent);
                    return;
                }
                case 13: {
                    intent = new Intent(getActivity().getBaseContext(),
                            LiveDataRowsActivity.class);
                    startActivity(intent);
                    return;
                }
                default:
                    break;
            }
            if (intent != null) {
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity())
                        .toBundle();
                startActivity(intent, bundle);
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {

        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
        }
    }
}
