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

public class RoomListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final Context context;

    private List<Room> list = new ArrayList<>();
    private BookingCategory bookingCategory = new BookingCategory();
    private int lastPosition = -1;
    private OnRoomSelected onRoomSelected;

    public RoomListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new RoomViewHolder(RoomView_.build(parent.getContext()));
        } else {
            return new RoomHeaderViewHolder(RoomHeaderView_.build(parent.getContext()));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RoomViewHolder) {
            ((RoomViewHolder) holder).getView().setRoom(list.get(position - 1));
            ((RoomViewHolder) holder).getView().setOnRoomSelected(onRoomSelected);
            setAnimation(holder.itemView, position);
        } else if (holder instanceof RoomHeaderViewHolder) {
            ((RoomHeaderViewHolder) holder).getView().setBookingCategory(bookingCategory);
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof RoomViewHolder) {
            ((RoomViewHolder) holder).getView().clearAnimation();
        } else {
            ((RoomHeaderViewHolder) holder).getView().clearAnimation();
        }
    }

    public void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_top);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public void addList(List<Room> rooms, BookingCategory bookingCategory) {
        this.list.addAll(rooms);
        this.bookingCategory = bookingCategory;
        notifyDataSetChanged();
    }

    public void clearList() {
        this.list = new ArrayList<>();
        lastPosition = -1;
        notifyDataSetChanged();
    }

    public int getListSize() {
        return list.size();
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

    public class RoomHeaderViewHolder extends RecyclerView.ViewHolder {
        private RoomHeaderView roomHeaderView;

        public RoomHeaderViewHolder(RoomHeaderView itemView) {
            super(itemView);
            this.roomHeaderView = itemView;
        }

        public RoomHeaderView getView() {
            return this.roomHeaderView;
        }
    }
}
