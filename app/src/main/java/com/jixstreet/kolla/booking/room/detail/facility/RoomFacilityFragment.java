package com.jixstreet.kolla.booking.room.detail.facility;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.room.Room;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 3/23/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_room_facility)
public class RoomFacilityFragment extends Fragment {

    private static final String ROOM_FACILITY = "Room Facility";
    private static final int COLUMN_COUNT = 3;

    @ViewById(R.id.facility_rv)
    protected RecyclerView facilityRv;

    private RoomFacilityAdapter roomFacilityAdapter;
    private Room room;

    public static RoomFacilityFragment newInstance(Room room) {
        Bundle args = new Bundle();
        args.putString(ROOM_FACILITY, new Gson().toJson(room));

        RoomFacilityFragment fragment = new RoomFacilityFragment_();
        fragment.setArguments(args);

        return fragment;
    }

    @AfterViews
    protected void onViewsCreated() {
        room = new Gson().fromJson(getArguments().getString(ROOM_FACILITY, ""), Room.class);
        if (room != null) {
            initAdapter();
            setValue(room);
        }
    }

    private void initAdapter() {
        roomFacilityAdapter = new RoomFacilityAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), COLUMN_COUNT);
        facilityRv.setClipToPadding(false);
        facilityRv.setLayoutManager(layoutManager);
        facilityRv.setHasFixedSize(true);
        facilityRv.setItemAnimator(new DefaultItemAnimator());
        facilityRv.setAdapter(roomFacilityAdapter);
    }

    public void setValue(Room room) {
        this.room = room;
        if (getContext() != null && room != null) {
            if (room.facilities != null)
                roomFacilityAdapter.setRoomFacilityList(room.facilities);
        }
    }
}
