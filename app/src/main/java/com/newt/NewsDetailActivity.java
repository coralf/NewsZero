package com.newt;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.newt.utils.Utility;

public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.immerse(this);
        setContentView(R.layout.activity_news_detail);
        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        WebView webView = (WebView) findViewById(R.id.wv_news_detail);

        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }
}
