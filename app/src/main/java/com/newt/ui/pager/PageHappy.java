package com.newt.ui.pager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.newt.R;

/**
 * Created by Thinking on 2017/5/23.
 */

public class PageHappy {

    private Context context;
    private View rootView;


    public PageHappy(Context context) {
        this.context = context;
        rootView = initView();
    }

    private View initView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.page_happy, null);
        return rootView;
    }

    private void initData() {

    }

    public View getRootView() {
        return rootView;
    }
}
