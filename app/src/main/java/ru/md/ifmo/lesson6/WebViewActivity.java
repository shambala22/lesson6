package ru.md.ifmo.lesson6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebViewActivity extends Activity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        webView = new WebView(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra("link");
        webView.loadUrl(url);
        webView.setWebViewClient(new AppWebViewClient());

        this.setContentView(webView);
    }

    @Override
    public boolean onKeyDown(final int key, final KeyEvent event) {
        if ((key == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(key, event);
    }

    private class AppWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }

}
