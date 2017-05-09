package com.jixstreet.kolla.payment;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.Booking;
import com.jixstreet.kolla.topup.CreditAmount;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.FormatUtils;
import com.jixstreet.kolla.utility.PermissionUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_other_payment)
public class OtherPaymentActivity extends AppCompatActivity implements OnPayOtherPayment {
    public static String paramKey = OtherPaymentActivity.class.getName().concat("1");
    public static int requestCode = ActivityUtils.getRequestCode(OtherPaymentActivity.class, "1");

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.tabs)
    protected TabLayout tabs;

    @ViewById(R.id.content_viewpager)
    protected ViewPager contentVp;

    @Nullable
    private Booking booking;

    private CreditAmount creditAmount;

    @ViewById(R.id.credit_amount_tv)
    protected TextView creditAmountTv;

    @ViewById(R.id.credit_price_tv)
    protected TextView creditPriceTv;

    @ViewById(R.id.top_up_wrapper)
    protected LinearLayout topUpWrapper;

    @AfterViews
    protected void onViewsCreated() {
        ViewUtils.setToolbar(this, toolbar);
        PermissionUtils.requestPermissions(this, PermissionUtils.PERMISSIONS_PHONE_STATE,
                PermissionUtils.REQUEST_PHONE_STATE, "Read Phone State");

        booking = ActivityUtils.getParam(this, Booking.paramKey, Booking.class);
        if (booking == null) {
            creditAmount = ActivityUtils.getParam(this, CreditAmount.paramKey, CreditAmount.class);
            if (creditAmount != null) {
                ViewUtils.setVisibility(topUpWrapper, View.VISIBLE);
                setTopUpValue(creditAmount);
            }
        }
        initPager();

    }

    private void setTopUpValue(CreditAmount creditAmount) {
        ViewUtils.setTextView(creditAmountTv, creditAmount.kolla_credit +
                (!creditAmount.given_bonus.equals("0") ? "+" + creditAmount.given_bonus : ""));
        ViewUtils.setTextView(creditPriceTv, FormatUtils.formatCurrency(creditAmount.nominal));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initPager() {
        OtherPaymentPagerAdapter adapter = new OtherPaymentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(BankTransferPaymentFragment.newInstance(booking), getString(R.string.bank_transfer));
        adapter.addFragment(BankTransferPaymentFragment.newInstance(booking), getString(R.string.internet_banking));
        adapter.addFragment(CreditCardPaymentFragment.newInstance(creditAmount), getString(R.string.credit_card));

        contentVp.setOffscreenPageLimit(adapter.mFragmentList.size());
        contentVp.setAdapter(adapter);
        tabs.setupWithViewPager(contentVp);
    }

    @Override
    public void onPay(Booking booking) {
        /*ActivityUtils.startActivityWParam(this, BookingConfirmationActivity_.class,
                BookingConfirmationActivity.paramKey, booking);*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.REQUEST_PHONE_STATE: {
                if (grantResults.length < 1
                        && grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    DialogUtils.makeToast(this, "Permission denied");
                    finish();
                }
            }
        }
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
