package com.jixstreet.kolla.login;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.jixstreet.kolla.Seeder;

/**
 * Created by satryaway on 2/15/2017.
 * satryaway@gmail.com
 */

public class LoginPagerAdapter extends FragmentPagerAdapter {
    private final Context context;
    private final FragmentManager fm;

    public LoginPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return LoginBackgroundFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return Seeder.getLoginBackgroundList().size();
    }
}
