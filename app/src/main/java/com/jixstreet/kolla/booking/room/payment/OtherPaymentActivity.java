package com.jixstreet.kolla.booking.room.payment;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.Booking;
import com.jixstreet.kolla.booking.BookingConfirmationActivity;
import com.jixstreet.kolla.booking.BookingConfirmationActivity_;
import com.jixstreet.kolla.booking.room.detail.description.RoomDetailFragment;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_other_payment)
public class OtherPaymentActivity extends AppCompatActivity implements OnPayOtherPayment {
    public static String paramKey = OtherPaymentActivity.class.getName().concat("1");

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.tabs)
    protected TabLayout tabs;

    @ViewById(R.id.content_viewpager)
    protected ViewPager contentVp;

    @Nullable
    private Booking booking;

    @AfterViews
    protected void onViewsCreated() {
        ViewUtils.setToolbar(this, toolbar);
        collectIntent();
        initPager();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void collectIntent() {
        booking = ActivityUtils.getParam(this, Booking.paramKey, Booking.class);
    }

    private void initPager() {
        OtherPaymentPagerAdapter adapter = new OtherPaymentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(BankTransferPaymentFragment.newInstance(booking), getString(R.string.bank_transfer));
        adapter.addFragment(BankTransferPaymentFragment.newInstance(booking), getString(R.string.internet_banking));
        adapter.addFragment(BankTransferPaymentFragment.newInstance(booking), getString(R.string.credit_card));

        contentVp.setOffscreenPageLimit(adapter.mFragmentList.size());
        contentVp.setAdapter(adapter);
        tabs.setupWithViewPager(contentVp);
    }

    @Override
    public void onPay(Booking booking) {
        ActivityUtils.startActivityWParam(this, BookingConfirmationActivity_.class,
                BookingConfirmationActivity.paramKey, booking);
    }

    public class OtherPaymentPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public OtherPaymentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
