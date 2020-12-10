package com.example.android.tva11ydemo.custom;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.android.tva11ydemo.R;

/**
 * dCustomViewWithExploreByTouchHelperActivity defines the activity which use ExploreByTouchHelper
 * implement accessibility support.
 *
 * The difference between those two "CustomViewWith.*Activity" is that the value of
 * "use_explore_by_touch_helper" in their layout file are different.
 */
public class CustomViewWithExploreByTouchHelperActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.custom_view_with_explore_by_touch_helper);
  }
}
