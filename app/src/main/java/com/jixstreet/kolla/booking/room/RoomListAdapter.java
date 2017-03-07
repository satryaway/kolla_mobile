package com.jixstreet.kolla.booking.room;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jixstreet.kolla.Seeder;
import com.jixstreet.kolla.news.NewsListAdapter;
import com.jixstreet.kolla.utility.ViewUtils;

import java.util.ArrayList;

/**
 * Created by satryaway on 3/7/2017.
 * satryaway@gmail.com
 */

public class RoomListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final Context context;

    private ArrayList<Room> rooms = new ArrayList<>();

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
            ((RoomViewHolder) holder).getView().setRoom(rooms.get(position - 1));
            ViewUtils.setAnimation(context, holder.itemView);
        } else if (holder instanceof RoomHeaderViewHolder) {
            ((RoomHeaderViewHolder) holder).getView().setRoomHeader(Seeder.getRoomHeader());
        }
    }

    @Override
    public int getItemCount() {
        return rooms.size() + 1;
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

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
        notifyDataSetChanged();
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
