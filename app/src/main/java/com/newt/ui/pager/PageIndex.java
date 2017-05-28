package com.newt.ui.pager;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.newt.R;
import com.newt.ui.pager.pagerindex.BaseIndexPage;
import com.newt.ui.pager.pagerindex.PageCaijing;
import com.newt.ui.pager.pagerindex.PageGuoji;
import com.newt.ui.pager.pagerindex.PageGuonei;
import com.newt.ui.pager.pagerindex.PageJunshi;
import com.newt.ui.pager.pagerindex.PageKeji;
import com.newt.ui.pager.pagerindex.PageShehui;
import com.newt.ui.pager.pagerindex.PageShishang;
import com.newt.ui.pager.pagerindex.PageTiyu;
import com.newt.ui.pager.pagerindex.PageTop;
import com.newt.ui.pager.pagerindex.PageYule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thinking on 2017/5/23.
 */

public class PageIndex {

    private static final String TAG = "PageIndex";

    private View rootView;
    private Context context;
    private TabLayout tabLayout;
    private ViewPager vpPIndex;
    private String[] titles = {"头条", "社会", "国内", "国际", "娱乐", "体育", "军事", "科技", "财经", "时尚"};

    public PageIndex(Context context) {
        this.context = context;
        rootView = initView();
    }

    private View initView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.page_index, null);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tb_p_index);
        vpPIndex = (ViewPager) rootView.findViewById(R.id.vp_p_index);
        vpPIndex.setAdapter(new VpIndexAdapter());
        tabLayout.setupWithViewPager(vpPIndex);
        return rootView;
    }

    public View getRootView() {
        return rootView;
    }


    private class VpIndexAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem: " + position);
            BaseIndexPage page = null;
            switch (position) {
                case 0:
                    page = new PageTop(context);
                    break;
                case 1:
                    page = new PageShehui(context);
                    break;
                case 2:
                    page = new PageGuonei(context);
                    break;
                case 3:
                    page = new PageGuoji(context);
                    break;
                case 4:
                    page = new PageYule(context);
                    break;
                case 5:
                    page = new PageTiyu(context);
                    break;
                case 6:
                    page = new PageJunshi(context);
                    break;
                case 7:
                    page = new PageKeji(context);
                    break;
                case 8:
                    page = new PageCaijing(context);
                    break;
                case 9:
                    page = new PageShishang(context);
                    break;
                default:
                    break;
            }
            View view = container.getChildAt(position);
            if (page != null) {
                view = page.getRootView();
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }


}
