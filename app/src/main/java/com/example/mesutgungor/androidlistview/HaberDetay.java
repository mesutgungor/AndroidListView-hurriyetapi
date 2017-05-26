package com.example.mesutgungor.androidlistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class HaberDetay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haber_detay);
        WebView webView = (WebView)findViewById(R.id.webview);
        String haberurl = getIntent().getStringExtra("HABERURL");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(haberurl);


    }
}
