package com.newt.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.newt.R;
import com.newt.domain.News;
import com.newt.listener.OnItemClickListener;
import com.newt.listener.OnLoadMoreListener;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by T on 2017/5/26.
 */

public class BaseNewsRecylerAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private static final String TAG = "ShehuiRecylerAdapter";

    private News news;
    private Context context;

    // 表示展示新闻
    private static final int SHOW_NEWS = 0;

    //展示上拉加载
    private static final int SHOW_LOAD_MOEW = 1;


    /**
     * 加载更多的回调接口
     */
    private OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    public void setNews(News news) {
        this.news = news;
    }

    public News getNews() {
        return news;
    }



    /*
    * 新闻点击监听
    * */

    private OnItemClickListener onItemClickListener;

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.itemClick(v, (Integer) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    /**
     * 列表视图的holder
     */
    private static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView date;
        TextView from;
        View url;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_list_item);
            title = (TextView) itemView.findViewById(R.id.tv_new_title);
            date = (TextView) itemView.findViewById(R.id.tv_date);
            from = (TextView) itemView.findViewById(R.id.tv_from);
            url = itemView.findViewById(R.id.url);
        }
    }

    /**
     * 上拉加载
     */

    private static class LoadMoreHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadMoreHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView;
        }
    }


    public BaseNewsRecylerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == SHOW_NEWS) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            view.setOnClickListener(this);
            viewHolder = new ViewHolder(view);
        } else {
            view = new ProgressBar(context);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewHolder = new LoadMoreHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (news != null) {

            int itemViewType = holder.getItemViewType();

            if (itemViewType == SHOW_NEWS) {
                List<News.ResultBean.DataBean> data = news.getResult().getData();
                News.ResultBean.DataBean dataBean = data.get(position);
                ViewHolder viewHolder = (ViewHolder) holder;

                ImageView imageView = viewHolder.imageView;
                Glide.with(context).load(dataBean.getThumbnail_pic_s()).into(imageView);

                viewHolder.title.setText(dataBean.getTitle());

                viewHolder.date.setText("时间：" + dataBean.getDate());

                viewHolder.from.setText("来自：" + dataBean.getAuthor_name());

                viewHolder.url.setTag(dataBean.getUrl());
                viewHolder.itemView.setTag(position);
            } else {
                if (onLoadMoreListener != null) {
                    onLoadMoreListener.loadMore();
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return news != null ? news.getResult().getData().size() + 1 : 0;
    }


    @Override
    public int getItemViewType(int position) {
        if (position >= news.getResult().getData().size()) {
            return SHOW_LOAD_MOEW;
        } else {
            return SHOW_NEWS;
        }
    }

}
