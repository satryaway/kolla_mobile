package com.jixstreet.kolla.booking.category;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.prefs.CPrefs;
import com.jixstreet.kolla.utility.DialogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by satryaway on 3/2/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_booking)
public class BookingFragment extends Fragment {
    @ViewById(R.id.booking_category_rv)
    protected RecyclerView bookingCategoryRv;
    private BookingCategoryAdapter bookingCategoryAdapter;

    public static BookingFragment newInstance() {
        return new BookingFragment_();
    }

    @AfterViews
    void onViewsCreated() {
        initAdapter();
        getCategories();
    }

    private void initAdapter() {
        bookingCategoryAdapter = new BookingCategoryAdapter(getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        bookingCategoryRv.setNestedScrollingEnabled(false);
        bookingCategoryRv.setClipToPadding(false);
        bookingCategoryRv.setLayoutManager(layoutManager);
        bookingCategoryRv.setHasFixedSize(true);
        bookingCategoryRv.setItemAnimator(new DefaultItemAnimator());
        bookingCategoryRv.setAdapter(bookingCategoryAdapter);
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
}
