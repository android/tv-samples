package com.example.android.tva11ydemo.standard;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.tva11ydemo.R;
import com.example.android.tva11ydemo.model.MovieList;

/**
 * StandardViewActivity defines the movie list view.
 */
public class StandardViewActivity extends AppCompatActivity {
  private String TAG = StandardViewActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.standard_view);
    fillOutContent();
  }

  private void fillOutContent() {
    RecyclerView firstRecycleView;
    RecyclerView.Adapter adapter;
    TextView firstRecycleViewTitle;

    LinearLayout parentView = (LinearLayout)findViewById(R.id.standard_view_parent);
    for (String category: MovieList.getCategories()) {
      View view = getLayoutInflater().inflate(R.layout.standard_row_item, null);
      firstRecycleViewTitle = (TextView) view.findViewById(R.id.first_recycle_view_title);
      firstRecycleViewTitle.setText(category);
      firstRecycleViewTitle.setLayoutParams(
          new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
      firstRecycleView = (RecyclerView) view.findViewById(R.id.first_recycle_view);
      firstRecycleView.setHasFixedSize(true);
      firstRecycleView.setLayoutManager(
          new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
              false));

      adapter = new MovieAdapter(MovieList.getMovieList());
      firstRecycleView.setAdapter(adapter);
      parentView.addView(view);
    }

  }
}
