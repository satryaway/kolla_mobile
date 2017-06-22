package com.jixstreet.kolla.profile;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.model.UserData;
import com.jixstreet.kolla.profile.booking.BookedRoomFragment;
import com.jixstreet.kolla.profile.detail.ProfileDetailFragment;
import com.jixstreet.kolla.profile.event.BookedEventFragment;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.ImageUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_profile)
public class ProfileActivity extends AppCompatActivity implements BookedEventFragment.OnGetBookedEventListener, BookedRoomFragment.OnGetBookedRoomDoneListener {
    private static final int BOOKING_SECTION = 1;
    private static final int EVENT_SECTION = 2;
    public static int requestCode = ActivityUtils.getRequestCode(ProfileActivity.class, "1");

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

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    private UserData userData;
    private BookedRoomFragment bookedRoomFragment;
    private BookedEventFragment bookedEventFragment;

    @AfterViews
    protected void onViewsCreated() {
        userData = ActivityUtils.getParam(this, UserData.paramKey, UserData.class);
        if (userData == null) {
            finish();
            return;
        }

        ViewUtils.setToolbar(this, toolbar);
        setValue();
        initFragments();
        setupViewPager();
    }

    private void setValue() {
        ViewUtils.setTextView(nameTv, userData.name);
        ViewUtils.setTextView(jobTv, userData.job_title);
        ImageUtils.loadImageRound(this, userData.profile_picture, profileImageIv);
    }

    private void initFragments() {
        bookedRoomFragment = BookedRoomFragment.newInstance(userData);
        bookedRoomFragment.setOnGetBookedRoomDoneListener(this);

        bookedEventFragment = BookedEventFragment.newInstance(userData);
        bookedEventFragment.setOnGetBookedEventListener(this);
    }

    private void setupViewPager() {
        ProfileViewPagerAdapter adapter = new ProfileViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ProfileDetailFragment.newInstance(userData), getString(R.string.detail));
        adapter.addFragment(bookedRoomFragment, getString(R.string.booked_space));
        adapter.addFragment(bookedEventFragment, getString(R.string.booked_event));

        viewPager.setOffscreenPageLimit(adapter.mFragmentList.size());
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onGetBookedEvent(String total) {
        if (tabs != null) {
            tabs.getTabAt(EVENT_SECTION).setText(getString(R.string.booked_event_s, total));
        }
        ViewUtils.setTextView(eventCountTv, total);
    }

    @Override
    public void onGetBookedRoom(String total) {
        if (tabs != null) {
            tabs.getTabAt(BOOKING_SECTION).setText(getString(R.string.booked_space_s, total));
        }
        ViewUtils.setTextView(spaceCountTv, total);
    }
}
