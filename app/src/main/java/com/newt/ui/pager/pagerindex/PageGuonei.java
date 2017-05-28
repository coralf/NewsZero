package com.newt.ui.pager.pagerindex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.newt.NewsDetailActivity;
import com.newt.R;
import com.newt.adpter.BaseNewsRecylerAdapter;

import com.newt.domain.News;
import com.newt.listener.OnItemClickListener;
import com.newt.listener.OnLoadMoreListener;
import com.newt.utils.Constant;
import com.newt.utils.HttpUtil;
import com.newt.utils.ToastUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by T on 2017/5/24.
 */

public class PageGuonei extends BaseIndexPage {
    private static final String TAG = "PageShehui";


    private Context context;
    private SwipeRefreshLayout srl;
    private RecyclerView recyler;
    private BaseNewsRecylerAdapter baseNewsRecylerAdapter;


    public PageGuonei(Context context) {
        this.context = context;
        initView();
    }

    public void initView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.page_index_shehui, null);
        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        recyler = (RecyclerView) rootView.findViewById(R.id.recycler_view);


        srl.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyler.setLayoutManager(linearLayoutManager);
        /**
         * 初始化数据，装配数据
         * */
        baseNewsRecylerAdapter = new BaseNewsRecylerAdapter(context);
        srl.setRefreshing(true);
        initData();
        if (srl.isRefreshing()) {
            srl.setRefreshing(false);
        }
        recyler.setAdapter(baseNewsRecylerAdapter);
        baseNewsRecylerAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                initData();
            }
        });

        baseNewsRecylerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void itemClick(View v, int tag) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                View tvUrl = v.findViewById(R.id.url);
                intent.putExtra("url", (String) tvUrl.getTag());
                context.startActivity(intent);
            }
        });

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                if (srl.isRefreshing()) {
                    srl.setRefreshing(false);
                }
            }
        });
    }


    /*
    * 加载数据
    * */
    public void initData() {

        dataFromNet();

    }

    /*
    * 从网络加载数据
    * */
    public void dataFromNet() {


        HttpUtil.sendHttpRequest(Constant.urlGuonei, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Activity activity = (Activity) PageGuonei.this.context;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(PageGuonei.this.context, "可能没网了");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String jsonData = response.body().string();
                final News news = new Gson().fromJson(jsonData, News.class);


                Activity activity = (Activity) PageGuonei.this.context;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showData(news);
                    }
                });
            }
        });
    }

    private void showData(News news) {
        if (baseNewsRecylerAdapter != null) {
            List<News.ResultBean.DataBean> data = news.getResult().getData();
            News newsFa = baseNewsRecylerAdapter.getNews();
            if (newsFa != null) {
                newsFa.getResult().getData().addAll(data);
            } else {
                baseNewsRecylerAdapter.setNews(news);
            }
            baseNewsRecylerAdapter.notifyDataSetChanged();
        }
    }

    public View getRootView() {
        return rootView;
    }
}

