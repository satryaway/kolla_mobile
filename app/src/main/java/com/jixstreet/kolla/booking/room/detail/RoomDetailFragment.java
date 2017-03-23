package com.jixstreet.kolla.booking.room.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jixstreet.kolla.R;

import org.androidannotations.annotations.EFragment;

/**
 * Created by satryaway on 3/23/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_room_detail)
public class RoomDetailFragment extends Fragment {

    public static RoomDetailFragment newInstance() {
        Bundle args = new Bundle();
        RoomDetailFragment fragment = new RoomDetailFragment_();
        fragment.setArguments(args);
        return fragment;
    }
}
