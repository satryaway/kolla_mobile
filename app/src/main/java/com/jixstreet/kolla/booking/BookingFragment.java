package com.jixstreet.kolla.booking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @AfterViews
    void onViewsCreated() {
        bookingCategoryAdapter = new BookingCategoryAdapter(getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        bookingCategoryRv.setNestedScrollingEnabled(true);
        bookingCategoryRv.setClipToPadding(true);
        bookingCategoryRv.setLayoutManager(layoutManager);
        bookingCategoryRv.setItemAnimator(new DefaultItemAnimator());
        bookingCategoryRv.setAdapter(bookingCategoryAdapter);

        bookingCategoryAdapter.setBookingCategories(Seeder.getBookingCategories(getActivity()));
    }
}
