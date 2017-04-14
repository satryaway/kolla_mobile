package com.jixstreet.kolla.booking.room.detail.description;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.room.Room;
import com.jixstreet.kolla.utility.CastUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 3/23/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_room_detail)
public class RoomDetailFragment extends Fragment {

    private static final String ROOM_DETAIL = "Room Detail";

    @ViewById(R.id.room_description_tv)
    protected TextView roomDescriptionTv;

    private Room room;

    public static RoomDetailFragment newInstance(Room room) {
        Bundle args = new Bundle();
        args.putString(ROOM_DETAIL, CastUtils.toString(room));
        RoomDetailFragment fragment = new RoomDetailFragment_();
        fragment.setArguments(args);

        return fragment;
    }

    @AfterViews
    protected void onViewsCreated() {
        room = CastUtils.fromString(getArguments().getString(ROOM_DETAIL, ""), Room.class);
        if (room != null) {
            setValue(room);
        }
    }

    public void setValue(Room room) {
        this.room = room;
        if (getContext() != null && room != null) {
            if (room.description != null)
                roomDescriptionTv.setText(room.description);
        }
    }
}
