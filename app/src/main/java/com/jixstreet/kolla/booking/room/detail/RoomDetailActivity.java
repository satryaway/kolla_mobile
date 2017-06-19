package com.jixstreet.kolla.booking.room.detail;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;
import com.jixstreet.kolla.booking.Booking;
import com.jixstreet.kolla.booking.BookingConfirmationActivity;
import com.jixstreet.kolla.booking.BookingConfirmationActivity_;
import com.jixstreet.kolla.booking.BookingJson;
import com.jixstreet.kolla.booking.BookingSizeActivity;
import com.jixstreet.kolla.booking.BookingSizeActivity_;
import com.jixstreet.kolla.booking.category.BookingEntity;
import com.jixstreet.kolla.booking.office.SurveyRequestOptionActivity;
import com.jixstreet.kolla.booking.office.SurveyRequestOptionActivity_;
import com.jixstreet.kolla.booking.room.Room;
import com.jixstreet.kolla.booking.room.RoomDetailJson;
import com.jixstreet.kolla.booking.room.detail.description.RoomDetailFragment;
import com.jixstreet.kolla.booking.room.detail.facility.RoomFacilityFragment;
import com.jixstreet.kolla.booking.room.detail.map.RoomMapFragment;
import com.jixstreet.kolla.payment.OnGetRoomDetail;
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
    public static int requestCode = ActivityUtils.getRequestCode(RoomDetailActivity.class, "1");

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

    @ViewById(R.id.collapsing_toolbar)
    protected CollapsingToolbarLayout collapsingToolbarLayout;

    @ViewById(R.id.confirm_tv)
    protected TextView bookTv;

    private Booking booking;
    private RoomDetailFragment roomDetailFragment;
    private RoomFacilityFragment roomFacilityFragment;
    private RoomMapFragment roomMapFragment;
    private RoomDetailJson roomDetailJson;

    @AfterViews
    protected void onViewsCreated() {
        booking = ActivityUtils.getParam(this, Booking.paramKey, Booking.class);
        if (booking == null) {
            finish();
            return;
        }

        ViewUtils.setToolbar(this, toolbar);
        roomDetailJson = new RoomDetailJson(this, booking.room.id);

        setValue();
        initFragments();
        setupViewPager();
        getRoomDetail();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setValue() {
        toolbar.setTitle(booking.room.name);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        ViewUtils.setTextView(toolbarTitleTv, booking.room.name);
        ViewUtils.setVisibility(bookTv, booking.room.isOnlyView ? View.GONE : View.VISIBLE);

        if (booking.roomRequest.category.equals(BookingEntity.OFFICE))
            bookTv.setText(R.string.request_a_survey);
        else if (booking.roomRequest.category.equals(BookingEntity.HALL))
            bookTv.setText(R.string.booking_this_hall);
    }

    private void initFragments() {
        roomDetailFragment = RoomDetailFragment.newInstance(booking.room);
        roomFacilityFragment = RoomFacilityFragment.newInstance(booking.room);
        roomMapFragment = RoomMapFragment.newInstance(booking.room);
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
                = new RoomDetailHeaderPagerManager(getSupportFragmentManager());
        imageVp.setAdapter(roomDetailHeaderPagerManager);
    }

    private void getRoomDetail() {
        roomDetailJson.getRoomDetail(new OnGetRoomDetail() {
            @Override
            public void onSuccess(RoomDetailJson.Response response) {
                booking.room = response.data;
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
        roomDetailFragment.setValue(booking.room);
        roomFacilityFragment.setValue(booking.room);
        roomMapFragment.setValue(booking.room);
    }

    @Click(R.id.confirm_tv)
    void bookThisSpace() {
        Class destinationClass;
        int requestCode;

        switch (booking.bookingCategory.id) {
            case BookingEntity.HALL:
                destinationClass = BookingSizeActivity_.class;
                requestCode = BookingSizeActivity.requestCode;
                break;
            case BookingEntity.OFFICE:
                destinationClass = SurveyRequestOptionActivity_.class;
                requestCode = SurveyRequestOptionActivity.requestCode;
                break;
            default:
                collectBookingRequest();
                destinationClass = BookingConfirmationActivity_.class;
                requestCode = BookingConfirmationActivity.requestCode;
                break;
        }

        ActivityUtils.startActivityWParamAndWait(RoomDetailActivity.this,
                destinationClass,
                Booking.paramKey, booking,
                requestCode);
    }

    private void collectBookingRequest() {
        BookingJson.Request request = new BookingJson.Request();
        request.location = booking.roomRequest.location;
        request.date = booking.roomRequest.date;
        request.time = booking.roomRequest.time;
        request.duration = booking.roomRequest.duration;
        request.guest = booking.roomRequest.guest;

        booking.bookingRequest = request;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BookingConfirmationActivity.requestCode
                    || requestCode == SurveyRequestOptionActivity.requestCode
                    || requestCode == BookingSizeActivity.requestCode) {
                setResult(RESULT_OK);
                finish();
            }
        }
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

        public RoomDetailHeaderPagerManager(FragmentManager fm) {
            super(fm);
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
