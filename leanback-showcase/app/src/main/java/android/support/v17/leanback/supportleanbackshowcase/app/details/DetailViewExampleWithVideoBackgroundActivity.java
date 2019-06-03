package android.support.v17.leanback.supportleanbackshowcase.app.details;

import android.app.Activity;
import android.os.Bundle;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.supportleanbackshowcase.R;

/**
 * Contains a {@link DetailsFragment} with video background in order to display more details
 * for a given card.
 */

public class DetailViewExampleWithVideoBackgroundActivity extends Activity {

    static final int BUY_MOVIE_REQUEST = 987;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_example);

        if (savedInstanceState == null) {
            DetailViewExampleWithVideoBackgroundFragment fragment =
                    new DetailViewExampleWithVideoBackgroundFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.details_fragment, fragment)
                    .commit();
        }
    }
}
