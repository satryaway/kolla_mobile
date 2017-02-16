package com.jixstreet.kolla.news;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by satryaway on 2/16/2017.
 * satryaway@gmail.com
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {
    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewsItemView newsItemView = NewsItemView_.build(parent.getContext());

        return new NewsViewHolder(newsItemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 16;
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
}
