package com.jixstreet.kolla.news;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by satryaway on 2/16/2017.
 * satryaway@gmail.com
 */

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new NewsViewHolder(NewsItemView_.build(parent.getContext()));
        } else{
            return new NewsHeaderViewHolder(NewsHeaderView_.build(parent.getContext()));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsViewHolder) {
            ((NewsViewHolder) holder).getView();
        } else if (holder instanceof NewsHeaderViewHolder) {
            ((NewsHeaderViewHolder) holder).getView().setView();
        }
    }

    @Override
    public int getItemCount() {
        return 16 + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
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
