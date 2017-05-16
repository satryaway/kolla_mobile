package com.jixstreet.kolla.booking.category;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.room.RoomListActivity;
import com.jixstreet.kolla.booking.room.RoomListActivity_;
import com.jixstreet.kolla.credit.GetBalanceJson;
import com.jixstreet.kolla.credit.OnGetBalance;
import com.jixstreet.kolla.prefs.CPrefs;
import com.jixstreet.kolla.topup.TopUpListActivity;
import com.jixstreet.kolla.topup.TopUpListActivity_;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DateUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.FormatUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by satryaway on 3/2/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_booking)
public class BookingFragment extends Fragment implements OnCategorySelected {
    @ViewById(R.id.booking_category_rv)
    protected RecyclerView bookingCategoryRv;

    @ViewById(R.id.balance_tv)
    protected TextView balanceTv;

    @ViewById(R.id.last_update_tv)
    protected TextView lastUpdateTv;

    private BookingCategoryAdapter bookingCategoryAdapter;
    private GetBalanceJson getBalanceJson;

    public static BookingFragment newInstance() {
        return new BookingFragment_();
    }

    @AfterViews
    void onViewsCreated() {
        getBalanceJson = new GetBalanceJson(getContext());
        initAdapter();
        getBalance();
        getCategories();
    }

    private void initAdapter() {
        bookingCategoryAdapter = new BookingCategoryAdapter(getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        bookingCategoryAdapter.setOnCategorySelected(this);
        bookingCategoryRv.setNestedScrollingEnabled(false);
        bookingCategoryRv.setClipToPadding(false);
        bookingCategoryRv.setLayoutManager(layoutManager);
        bookingCategoryRv.setHasFixedSize(true);
        bookingCategoryRv.setItemAnimator(new DefaultItemAnimator());
        bookingCategoryRv.setAdapter(bookingCategoryAdapter);
    }

    private void getBalance() {
        if (getBalanceJson == null) return;
        getBalanceJson.get(new OnGetBalance() {
            @Override
            public void onSuccess(GetBalanceJson.Response response) {
                ViewUtils.setTextView(balanceTv, FormatUtils.formatDecimal(response.data.main_credit));
                ViewUtils.setTextView(lastUpdateTv,
                        getString(R.string.last_update_s,
                                DateUtils.getDateTimeStrFromMillis(response.data.updated_at, "")));
            }

            @Override
            public void onFailure(String message) {
                DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(), message);
            }
        });
    }

    private void getCategories() {
        List<BookingCategory> responses = CPrefs.readList(getActivity(),
                BookingCategoryJson.prefKey, BookingCategory[].class);

        if (responses != null && responses.size() > 0)
            bookingCategoryAdapter.setBookingCategories(responses);

        getCategoriesFromServer();
    }

    private void getCategoriesFromServer() {
        BookingCategoryJson bookingCategoryJson = new BookingCategoryJson(getActivity());
        bookingCategoryJson.get(new OnGetCategories() {
            @Override
            public void onSuccess(BookingCategoryJson.Response response) {
                if (response.data.data != null) {
                    bookingCategoryAdapter.setBookingCategories(response.data.data);
                    CPrefs.writeList(getActivity(), BookingCategoryJson.prefKey, response.data.data);
                }
            }

            @Override
            public void onFailure(String message) {
                DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(),
                        getActivity().getWindow().getDecorView(), message);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RoomListActivity.requestCode ||
                    requestCode == TopUpListActivity.requestCode) {
                getBalance();
            }
        }
    }

    @Override
    public void onSelect(BookingCategory bookingCategory) {
        ActivityUtils.startActivityWParamAndWait(this, RoomListActivity_.class,
                BookingCategory.paramKey, bookingCategory, RoomListActivity.requestCode);
    }

    @Click(R.id.add_credit_tv)
    protected void goToAddCreditPage() {
        ActivityUtils.startActivityAndWait(this, TopUpListActivity_.class,
                TopUpListActivity.requestCode);
    }
}
