package com.jixstreet.kolla.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.model.NewsDetail;
import com.jixstreet.kolla.utility.ViewUtils;

import java.util.ArrayList;

/**
 * Created by satryaway on 2/16/2017.
 * satryaway@gmail.com
 */

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final Context context;

    private ArrayList<NewsDetail> news = new ArrayList<>();

    public NewsListAdapter(Context context) {
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new NewsViewHolder(NewsItemView_.build(parent.getContext()));
        } else {
            return new NewsHeaderViewHolder(NewsHeaderView_.build(parent.getContext()));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsViewHolder) {
            ((NewsViewHolder) holder).getView().setNewsDetail(news.get(position - 1));
        } else if (holder instanceof NewsHeaderViewHolder) {
            ((NewsHeaderViewHolder) holder).getView().setView();
        }
        ViewUtils.setAnimation(context, holder.itemView);
    }

    @Override
    public int getItemCount() {
        return news.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof NewsViewHolder) {
            ((NewsViewHolder) holder).getView().clearAnimation();
        } else {
            ((NewsHeaderViewHolder) holder).getView().clearAnimation();
        }
    }

    public void setNews(ArrayList<NewsDetail> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        private NewsItemView newsItemView;

        public NewsViewHolder(NewsItemView itemView) {
            super(itemView);
            this.newsItemView = itemView;
        }

        public NewsItemView getView() {
            return this.newsItemView;
        }
    }

    public class NewsHeaderViewHolder extends RecyclerView.ViewHolder {
        private NewsHeaderView newsHeaderView;

        public NewsHeaderViewHolder(NewsHeaderView itemView) {
            super(itemView);
            this.newsHeaderView = itemView;
        }

        public NewsHeaderView getView() {
            return this.newsHeaderView;
        }
    }
}
