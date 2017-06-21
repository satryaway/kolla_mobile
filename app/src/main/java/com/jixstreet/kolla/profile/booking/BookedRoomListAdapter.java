package com.jixstreet.kolla.booking.room;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.category.BookingCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 3/7/2017.
 * satryaway@gmail.com
 */

public class BookedRoomListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;

    private List<Room> list = new ArrayList<>();
    private int lastPosition = -1;
    private OnRoomSelected onRoomSelected;
    private String size;

    public BookedRoomListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RoomViewHolder(RoomView_.build(parent.getContext()));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RoomViewHolder) holder).getView().setRoom(list.get(position));
        ((RoomViewHolder) holder).getView().setOnRoomSelected(onRoomSelected);
        if (size != null)
            ((RoomViewHolder) holder).getView().setColorCount(size);

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        ((RoomViewHolder) holder).getView().clearAnimation();
    }

    public void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_top);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void addList(List<Room> rooms) {
        this.list.addAll(rooms);
        notifyDataSetChanged();
    }

    public void clearList() {
        this.list = new ArrayList<>();
        lastPosition = -1;
        notifyDataSetChanged();
    }

    public void setOnRoomSelected(OnRoomSelected onRoomSelected) {
        this.onRoomSelected = onRoomSelected;
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {
        private RoomView roomView;

        public RoomViewHolder(RoomView itemView) {
            super(itemView);
            this.roomView = itemView;
        }

        public RoomView getView() {
            return this.roomView;
        }
    }
}
