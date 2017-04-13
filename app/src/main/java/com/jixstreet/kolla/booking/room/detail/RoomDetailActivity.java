package com.jixstreet.kolla.booking.room.detail;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;
import com.jixstreet.kolla.booking.room.Room;
import com.jixstreet.kolla.booking.room.RoomDetailJson;
import com.jixstreet.kolla.booking.room.detail.description.RoomDetailFragment;
import com.jixstreet.kolla.booking.room.detail.facility.RoomFacilityFragment;
import com.jixstreet.kolla.booking.room.detail.map.RoomMapFragment;
import com.jixstreet.kolla.booking.room.payment.OnGetRoomDetail;
import com.jixstreet.kolla.booking.room.payment.OtherPaymentActivity_;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_room_detail)
public class RoomDetailActivity extends AppCompatActivity {

    public static String paramKey = RoomDetailActivity.class.getName().concat("1");

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.image_viewpager)
    protected ViewPager imageVp;

    @ViewById(R.id.content_viewpager)
    protected ViewPager contentVp;

    @ViewById(R.id.tabs)
    protected TabLayout tabs;

    @ViewById(R.id.toolbar_title_tv)
    protected TextView toolbarTitleTv;

    private Room room;
    private RoomDetailFragment roomDetailFragment;
    private RoomFacilityFragment roomFacilityFragment;
    private RoomMapFragment roomMapFragment;

    private RoomDetailJson roomDetailJson;

    @AfterViews
    protected void onViewsCreated() {
        ViewUtils.setToolbar(this, toolbar);

        room = ActivityUtils.getParam(this, paramKey, Room.class);
        if (room != null) {
            roomDetailJson = new RoomDetailJson(this, room.id);
            setValue();
            initFragments();
            setupViewPager();
            getRoomDetail();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setValue() {
        ViewUtils.setTextView(toolbarTitleTv, room.name);
    }

    private void initFragments() {
        roomDetailFragment = RoomDetailFragment.newInstance(room);
        roomFacilityFragment = RoomFacilityFragment.newInstance(room);
        roomMapFragment = RoomMapFragment.newInstance(room);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(roomDetailFragment, getString(R.string.detail));
        adapter.addFragment(roomFacilityFragment, getString(R.string.facility));
        adapter.addFragment(roomMapFragment, getString(R.string.map));

        contentVp.setOffscreenPageLimit(adapter.mFragmentList.size());
        contentVp.setAdapter(adapter);
        tabs.setupWithViewPager(contentVp);

        RoomDetailHeaderPagerManager roomDetailHeaderPagerManager
                = new RoomDetailHeaderPagerManager(this, getSupportFragmentManager());
        imageVp.setAdapter(roomDetailHeaderPagerManager);
    }

    private void getRoomDetail() {
        roomDetailJson.getRoomDetail(new OnGetRoomDetail() {
            @Override
            public void onSuccess(RoomDetailJson.Response response) {
                room = response.data;
                refreshFragments();
            }

            @Override
            public void onFailure(String message) {
                DialogUtils.makeSnackBar(CommonConstant.failed, RoomDetailActivity.this,
                        getWindow().getDecorView(), message);
            }
        });
    }

    private void refreshFragments() {
        roomDetailFragment.setValue(room);
        roomFacilityFragment.setValue(room);
        roomMapFragment.setValue(room);
    }

    @Click(R.id.booking_this_space_tv)
    void bookThisSpace() {
        ActivityUtils.startActivity(this, OtherPaymentActivity_.class);
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
