package com.example.android.tva11ydemo.web;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.android.tva11ydemo.R;

/**
 * WebViewActivity initiates and assigns URL to WebView.
 */
public class WebViewActivity extends AppCompatActivity {

  private final String TAG = WebViewActivity.class.getSimpleName();
  private final String WEB_URL = "file:///android_asset/index.html";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.web_view);
    WebView webView = (WebView) findViewById(R.id.web_view);
    webView.getSettings().setJavaScriptEnabled(true);
    webView.setWebChromeClient(new WebChromeClient());
    webView.loadUrl(WEB_URL);
  }
}

