package com.jixstreet.kolla.booking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jixstreet.kolla.R;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 3/2/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_booking)
public class BookingFragment extends Fragment {
    @ViewById(R.id.booking_header_wrapper)
    protected ViewGroup bookingHeaderWrapper;

    @ViewById(R.id.item_1_wrapper)
    protected ViewGroup item1Wrapper;

    @ViewById(R.id.item_2_wrapper)
    protected ViewGroup item2Wrapper;

    @ViewById(R.id.item_3_wrapper)
    protected ViewGroup item3Wrapper;

    @ViewById(R.id.item_4_wrapper)
    protected ViewGroup item4Wrapper;

    public static BookingFragment newInstance() {
        return new BookingFragment_();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
