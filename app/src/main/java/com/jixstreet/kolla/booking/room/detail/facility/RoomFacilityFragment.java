package com.jixstreet.kolla.booking.room.detail.facility;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 3/23/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_room_facility)
public class RoomFacilityFragment extends Fragment {

    @ViewById(R.id.facility_rv)
    protected RecyclerView facilityRv;

    private RoomFacilityAdapter roomFacilityAdapter;

    public static RoomFacilityFragment newInstance() {
        Bundle args = new Bundle();
        RoomFacilityFragment fragment = new RoomFacilityFragment_();
        fragment.setArguments(args);

        return fragment;
    }

    @AfterViews
    protected void onViewsCreated() {
        roomFacilityAdapter = new RoomFacilityAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        facilityRv.setClipToPadding(false);
        facilityRv.setLayoutManager(layoutManager);
        facilityRv.setHasFixedSize(true);
        facilityRv.setItemAnimator(new DefaultItemAnimator());
        facilityRv.setAdapter(roomFacilityAdapter);

        //dummy data
        roomFacilityAdapter.setRoomFacilityList(Seeder.getRoomFacilities(25));
    }
}
