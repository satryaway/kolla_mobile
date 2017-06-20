package com.jixstreet.kolla.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jixstreet.kolla.R;

import org.androidannotations.annotations.EFragment;

/**
 * Created by satryaway on 6/20/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_profile_detail)
public class ProfileDetailFragment extends Fragment {

    public static ProfileDetailFragment newInstance() {
        Bundle args = new Bundle();
        ProfileDetailFragment fragment = new ProfileDetailFragment_();
        fragment.setArguments(args);
        return fragment;
    }
}
