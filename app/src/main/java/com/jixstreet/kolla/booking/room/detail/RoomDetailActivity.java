package com.jixstreet.kolla.booking.room.detail;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;
import com.jixstreet.kolla.booking.room.detail.description.RoomDetailFragment;
import com.jixstreet.kolla.booking.room.detail.facility.RoomFacilityFragment;
import com.jixstreet.kolla.booking.room.detail.map.RoomMapFragment;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_room_detail)
public class RoomDetailActivity extends AppCompatActivity {

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.image_viewpager)
    protected ViewPager imageVp;

    @ViewById(R.id.content_viewpager)
    protected ViewPager contentVp;

    @ViewById(R.id.tabs)
    protected TabLayout tabs;

    @AfterViews
    protected void onViewsCreated() {
        ViewUtils.setToolbar(this, toolbar);
        setupViewPager();
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(RoomDetailFragment.newInstance(), getString(R.string.detail));
        adapter.addFragment(RoomFacilityFragment.newInstance(), getString(R.string.facility));
        adapter.addFragment(RoomMapFragment.newInstance(), getString(R.string.map));
        contentVp.setOffscreenPageLimit(adapter.mFragmentList.size());
        contentVp.setAdapter(adapter);
        tabs.setupWithViewPager(contentVp);

        RoomDetailHeaderPagerManager roomDetailHeaderPagerManager
                = new RoomDetailHeaderPagerManager(this, getSupportFragmentManager());
        imageVp.setAdapter(roomDetailHeaderPagerManager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
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

    public class RoomDetailHeaderPagerManager extends FragmentPagerAdapter {
        private final Context context;

        public RoomDetailHeaderPagerManager(Context context, FragmentManager fm) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return RoomDetailHeaderItemFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return Seeder.getNewsHeaderList().size();
        }
    }

}
