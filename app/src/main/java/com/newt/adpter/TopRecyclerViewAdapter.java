package com.newt.adpter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.newt.R;
import com.newt.domain.News;
import com.newt.listener.OnItemClickListener;
import com.newt.listener.OnLoadMoreListener;
import com.newt.ui.view.Banner;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by T on 2017/5/24.
 */

public class TopRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private News news;
    private Context context;
    private OnLoadMoreListener onLoadMoreListener;
    private OnItemClickListener onItemClickListener;

    private static final int TYPE_BANNER = 0;
    private static final int TYPE_NEWS = 1;
    private static final int TYPE_LOADMORE = 2;


    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    private static final String TAG = "TopRecyclerViewAdapter";

    /**
     * 主要布局
     */
    static class ViewHoder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        View url;

        public ViewHoder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_list_item);
            textView = (TextView) itemView.findViewById(R.id.tv_new_title);
            url = itemView.findViewById(R.id.url);
        }
    }


    /*
    *底部
    * */
    static class FootViewHoder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public FootViewHoder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView;
        }
    }


    /*
    * bannner
    * */
    static class BannerViewHolder extends RecyclerView.ViewHolder {

        private Banner banner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            banner = (Banner) itemView;
        }
    }


    public TopRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHoder = null;
        if (viewType == TYPE_BANNER) {
            ArrayList<String> urls = new ArrayList<>();
            List<News.ResultBean.DataBean> data = news.getResult().getData();
            urls.add(data.get(data.size() - 1).getThumbnail_pic_s03() != null ? data.get(data.size() - 1).getThumbnail_pic_s03() : data.get(data.size() - 1).getThumbnail_pic_s());
            urls.add(data.get(data.size() - 2).getThumbnail_pic_s03() != null ? data.get(data.size() - 2).getThumbnail_pic_s03() : data.get(data.size() - 2).getThumbnail_pic_s());
            urls.add(data.get(data.size() - 3).getThumbnail_pic_s03() != null ? data.get(data.size() - 3).getThumbnail_pic_s03() : data.get(data.size() - 3).getThumbnail_pic_s());
            urls.add(data.get(data.size() - 4).getThumbnail_pic_s03() != null ? data.get(data.size() - 4).getThumbnail_pic_s03() : data.get(data.size() - 4).getThumbnail_pic_s());
            view = new Banner(context, urls);
            view.setLayoutParams(new Banner.LayoutParams(Banner.LayoutParams.MATCH_PARENT, 500));
            viewHoder = new BannerViewHolder(view);
        } else if (viewType == TYPE_NEWS) {
            //新闻
            view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            viewHoder = new ViewHoder(view);
            view.setOnClickListener(this);
        } else if (viewType == TYPE_LOADMORE) {
            //进度条
            view = new ProgressBar(context);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewHoder = new FootViewHoder(view);
        }
        return viewHoder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_NEWS) {
            List<News.ResultBean.DataBean> data = news.getResult().getData();
            News.ResultBean.DataBean dataBean = data.get(position);
            ViewHoder viewHolder = (ViewHoder) holder;
            ImageView imageView = viewHolder.imageView;
            TextView textView = viewHolder.textView;
            String title = dataBean.getTitle();
            String thumbnail_pic_s = dataBean.getThumbnail_pic_s();
            Glide.with(context).load(thumbnail_pic_s).into(imageView);
            textView.setText(title);
            viewHolder.url.setTag(data.get(position).getUrl());
            holder.itemView.setTag(position);
        } else if (holder.getItemViewType() == TYPE_LOADMORE) {
            onLoadMoreListener.loadMore();
        }
    }

    @Override
    public int getItemCount() {
        return news == null ? 0 : news.getResult().getData().size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        if (position >= news.getResult().getData().size()) {
            return TYPE_LOADMORE;
        } else if (position == TYPE_BANNER) {
            return TYPE_BANNER;
        } else {
            return TYPE_NEWS;
        }
    }


    /**
     * 上拉加载设置监听，
     */
    public void setOnLoadMoreListenerMore(OnLoadMoreListener onLoadMoreListenerMore) {
        this.onLoadMoreListener = onLoadMoreListenerMore;
    }


    /**
     * item的点击
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.itemClick(v, (Integer) v.getTag());
        }
    }
}
