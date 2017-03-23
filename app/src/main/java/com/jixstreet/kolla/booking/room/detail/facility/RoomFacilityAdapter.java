package com.jixstreet.kolla.booking.room.detail.facility;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 3/23/2017.
 * satryaway@gmail.com
 */

public class RoomFacilityAdapter extends RecyclerView.Adapter<RoomFacilityAdapter.RoomFacilityViewHolder> {

    private List<RoomFacility> roomFacilityList = new ArrayList<>();

    @Override
    public RoomFacilityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RoomFacilityItemView roomFacilityItemView = RoomFacilityItemView_.build(parent.getContext());
        return new RoomFacilityViewHolder(roomFacilityItemView);
    }

    @Override
    public void onBindViewHolder(RoomFacilityViewHolder holder, int position) {
        holder.getView().setRoomFacility(roomFacilityList.get(position));
    }

    @Override
    public int getItemCount() {
        return roomFacilityList.size();
    }

    public void setRoomFacilityList(List<RoomFacility> roomFacilityList) {
        this.roomFacilityList = roomFacilityList;
        notifyDataSetChanged();
    }

    public class RoomFacilityViewHolder extends RecyclerView.ViewHolder {

        private RoomFacilityItemView roomFacilityItemView;

        public RoomFacilityViewHolder(RoomFacilityItemView itemView) {
            super(itemView);
            this.roomFacilityItemView = itemView;
        }

        public RoomFacilityItemView getView() {
            return this.roomFacilityItemView;
        }
    }
}
