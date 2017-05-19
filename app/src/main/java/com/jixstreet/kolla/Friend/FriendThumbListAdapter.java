package com.jixstreet.kolla.Friend;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 5/19/2017.
 * satryaway@gmail.com
 */

public class FriendThumbListAdapter extends RecyclerView.Adapter<FriendThumbListAdapter.FriendThumbViewHolder> {
    private List<Friend> itemList = new ArrayList<>();
    private FriendThumbView.OnThumbClickListener onThumbClickListener;

    @Override
    public FriendThumbViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FriendThumbView friendThumbView = FriendThumbView_.build(parent.getContext());
        friendThumbView.setOnThumbClickListener(onThumbClickListener);
        return new FriendThumbViewHolder(friendThumbView);
    }

    @Override
    public void onBindViewHolder(FriendThumbViewHolder holder, int position) {
//        holder.getView().setFriend(itemList.get(position));
        holder.getView().setFriend(null);
    }

    @Override
    public int getItemCount() {
//        return itemList.size();
        return 20;
    }

    public void setOnThumbClickListener(FriendThumbView.OnThumbClickListener onThumbClickListener) {
        this.onThumbClickListener = onThumbClickListener;
    }

    public void setItemList(List<Friend> itemList) {
        this.itemList.addAll(itemList);
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
