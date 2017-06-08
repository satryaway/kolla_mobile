package com.jixstreet.kolla.friend;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jixstreet.kolla.model.UserData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 5/19/2017.
 * satryaway@gmail.com
 */

public class FriendThumbListAdapter extends RecyclerView.Adapter<FriendThumbListAdapter.FriendThumbViewHolder> {
    private List<UserData> itemList = new ArrayList<>();
    private FriendThumbView.OnThumbClickListener onThumbClickListener;

    @Override
    public FriendThumbViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FriendThumbView friendThumbView = FriendThumbView_.build(parent.getContext());
        friendThumbView.setOnThumbClickListener(onThumbClickListener);
        return new FriendThumbViewHolder(friendThumbView);
    }

    @Override
    public void onBindViewHolder(FriendThumbViewHolder holder, int position) {
        holder.getView().setUserData(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setOnThumbClickListener(FriendThumbView.OnThumbClickListener onThumbClickListener) {
        this.onThumbClickListener = onThumbClickListener;
    }

    public void setItemList(List<UserData> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public class FriendThumbViewHolder extends RecyclerView.ViewHolder {
        private FriendThumbView itemView;
        public FriendThumbViewHolder(FriendThumbView itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public FriendThumbView getView() {
            return itemView;
        }
    }
}
