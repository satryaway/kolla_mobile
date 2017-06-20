package com.jixstreet.kolla.profile;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.login.LoginJson;
import com.jixstreet.kolla.model.UserData;
import com.jixstreet.kolla.utility.ImageUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends Fragment {
    @ViewById(R.id.tabs)
    protected TabLayout tabs;

    @ViewById(R.id.content_viewpager)
    protected ViewPager viewPager;

    @ViewById(R.id.space_count_tv)
    protected TextView spaceCountTv;

    @ViewById(R.id.event_count_tv)
    protected TextView eventCountTv;

    @ViewById(R.id.profile_image_iv)
    protected ImageView profileImageIv;

    @ViewById(R.id.name_tv)
    protected TextView nameTv;

    @ViewById(R.id.job_tv)
    protected TextView jobTv;

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment_();
        fragment.setArguments(args);
        return fragment;
    }

    @AfterViews
    protected void onViewsCreated() {
        setupViewPager();
        setValue();
    }

    private void setValue() {
        UserData userData = LoginJson.Response.getUserData(getActivity());
        ViewUtils.setTextView(nameTv, userData.name);
        ViewUtils.setTextView(jobTv, userData.job_title);
        ImageUtils.loadImageRound(getActivity(), userData.profile_picture, profileImageIv);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(ProfileDetailFragment.newInstance(), getString(R.string.detail));
        adapter.addFragment(ProfileDetailFragment.newInstance(), getString(R.string.detail));
        adapter.addFragment(ProfileDetailFragment.newInstance(), getString(R.string.detail));
        adapter.addFragment(ProfileDetailFragment.newInstance(), getString(R.string.detail));
        adapter.addFragment(ProfileDetailFragment.newInstance(), getString(R.string.detail));

        viewPager.setOffscreenPageLimit(adapter.mFragmentList.size());
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
