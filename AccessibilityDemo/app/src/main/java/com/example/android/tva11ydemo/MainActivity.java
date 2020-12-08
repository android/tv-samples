package com.example.android.tva11ydemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android.tva11ydemo.custom.bestpractices.CustomViewBestPracticeActivity;
import com.example.android.tva11ydemo.custom.CustomViewWithExploreByTouchHelperActivity;
import com.example.android.tva11ydemo.custom.CustomViewWithAccessibilityNodeProviderActivity;
import com.example.android.tva11ydemo.standard.StandardViewActivity;
import com.example.android.tva11ydemo.web.WebViewActivity;

/**
 * MainActivity defines the home page of the app. It defines five buttons to open different samples.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private String TAG = MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);
    setupClickListeners();
  }

  private void setupClickListeners() {
    findViewById(R.id.standard_view_button).setOnClickListener(this);
    findViewById(R.id.custom_view_with_explore_by_touch_helper_button).setOnClickListener(this);
    findViewById(R.id.custom_view_with_accessibility_node_provider_button).setOnClickListener(this);
    findViewById(R.id.custom_view_best_practice_button).setOnClickListener(this);
    findViewById(R.id.web_view_button).setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    Intent intent;
    switch (view.getId()) {
      case R.id.standard_view_button:
        intent = new Intent(getApplicationContext(), StandardViewActivity.class);
        break;
      case R.id.custom_view_with_explore_by_touch_helper_button:
        intent = new Intent(getApplicationContext(),
            CustomViewWithExploreByTouchHelperActivity.class);
        break;
      case R.id.custom_view_with_accessibility_node_provider_button:
        intent = new Intent(getApplicationContext(),
            CustomViewWithAccessibilityNodeProviderActivity.class);
        break;
      case R.id.custom_view_best_practice_button:
        intent = new Intent(getApplicationContext(), CustomViewBestPracticeActivity.class);
        break;
      case R.id.web_view_button:
        intent = new Intent(getApplicationContext(), WebViewActivity.class);
        break;
      default:
        Log.e(TAG, "Stop launch activity: Undefined button ID received: " + view.getId());
        return;
    }
    startActivity(intent);
  }

}
