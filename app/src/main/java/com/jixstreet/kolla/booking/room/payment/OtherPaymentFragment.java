package com.jixstreet.kolla.booking.room.payment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jixstreet.kolla.R;

import org.androidannotations.annotations.EFragment;

/**
 * Created by satryaway on 3/28/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_other_payment)
public class OtherPaymentFragment extends Fragment {

    public static OtherPaymentFragment newInstance() {

        Bundle args = new Bundle();
        OtherPaymentFragment fragment = new OtherPaymentFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
