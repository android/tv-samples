package com.example.android.tva11ydemo.custom;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.android.tva11ydemo.R;

/**
 * CustomViewWithAccessibilityNodeProviderActivity defines the activity which use
 * AccessibilityNodeProvider implement accessibility support.
 */
public class CustomViewWithAccessibilityNodeProviderActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.custom_view_with_accessibility_node_provider);
  }
}
