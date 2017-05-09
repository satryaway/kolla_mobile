package com.jixstreet.kolla.topup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 5/9/2017.
 * satryaway@gmail.com
 */

public class TopUpCreditListAdapter extends RecyclerView.Adapter<TopUpCreditListAdapter.TopUpCreditViewHolder> {
    private List<CreditAmount> itemList = new ArrayList<>();

    @Override
    public TopUpCreditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TopUpCreditView topUpCreditView = TopUpCreditView_.build(parent.getContext());
        return new TopUpCreditViewHolder(topUpCreditView);
    }

    @Override
    public void onBindViewHolder(TopUpCreditViewHolder holder, int position) {
        holder.getView().setItem(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItemList(List<CreditAmount> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public void clearItems() {
        this.itemList = new ArrayList<>();
    }

    public class TopUpCreditViewHolder extends RecyclerView.ViewHolder {
        private TopUpCreditView topUpCreditView;

        public TopUpCreditViewHolder(TopUpCreditView itemView) {
            super(itemView);
            topUpCreditView = itemView;
        }

        public TopUpCreditView getView() {
            return topUpCreditView;
        }
    }
}
