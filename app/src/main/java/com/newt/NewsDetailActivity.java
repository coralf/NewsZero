package com.newt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.mob.commons.SHARESDK;
import com.newt.utils.Utility;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailActivity extends AppCompatActivity {

    private ImageView ivShareSdk;
    private String url;
    private String title;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.immerse(this);
        setContentView(R.layout.activity_news_detail);
        ShareSDK.initSDK(this, "1e64ec8ebf538");
        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        WebView webView = (WebView) findViewById(R.id.wv_news_detail);
        ivShareSdk = (ImageView) findViewById(R.id.iv_share_sdk);

        ivShareSdk.setVisibility(View.VISIBLE);
        ivShareSdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });

        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        imageUrl = intent.getStringExtra("image");
        webView.loadUrl(url);
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle(title);
        oks.setTitleUrl(url);
        oks.setUrl(url);
        oks.setSite(getString(R.string.app_name));
        oks.setSiteUrl(url);
        oks.setImageUrl(imageUrl);
        oks.show(this);
    }


}
