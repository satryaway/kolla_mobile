package com.jixstreet.kolla.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.model.UserData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 6/21/2017.
 * satryaway@gmail.com
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    private final Context context;
    private OnUserSelectedListener onUserSelectedListener;
    private List<UserData> itemList = new ArrayList<>();
    private int lastPosition = -1;

    public UserListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserListView userListView = UserListView_.build(parent.getContext());
        userListView.setOnUserSelectedListener(onUserSelectedListener);
        return new UserViewHolder(userListView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.getView().setUserData(itemList.get(position));
        setAnimation(holder.getView(), position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItems(List<UserData> userDataList) {
        this.itemList.addAll(userDataList);
        notifyDataSetChanged();
    }

    public void clearList() {
        this.itemList = new ArrayList<>();
        lastPosition = -1;
        notifyDataSetChanged();
    }

    public void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_top);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    public void setOnUserSelectedListener(OnUserSelectedListener onUserSelectedListener) {
        this.onUserSelectedListener = onUserSelectedListener;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private UserListView userListView;
        public UserViewHolder(UserListView itemView) {
            super(itemView);
            this.userListView = itemView;
        }

        public UserListView getView() {
            return userListView;
        }
    }
}
