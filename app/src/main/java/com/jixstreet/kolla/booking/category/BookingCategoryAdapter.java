package com.jixstreet.kolla.booking.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jixstreet.kolla.utility.ViewUtils;
import com.jixstreet.kolla.view.BookingCategoryView;
import com.jixstreet.kolla.view.BookingCategoryView_;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 3/6/2017.
 * satryaway@gmail.com
 */

public class BookingCategoryAdapter extends RecyclerView.Adapter<BookingCategoryAdapter.BookingCategoryViewHolder> {
    private final Context context;
    private List<BookingCategory> bookingCategories = new ArrayList<>();
    private OnCategorySelected onCategorySelected;

    public BookingCategoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public BookingCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BookingCategoryView bookingCategoryView = BookingCategoryView_.build(parent.getContext());
        return new BookingCategoryViewHolder(bookingCategoryView);
    }

    @Override
    public void onBindViewHolder(BookingCategoryViewHolder holder, int position) {
        holder.getView().setBookingCategory(bookingCategories.get(position));
        holder.getView().setOnCategorySelected(onCategorySelected);
    }

    @Override
    public int getItemCount() {
        return bookingCategories.size();
    }

    public void setOnCategorySelected(OnCategorySelected onCategorySelected) {
        this.onCategorySelected = onCategorySelected;
    }

    public void setBookingCategories(List<BookingCategory> bookingCategories) {
        this.bookingCategories = bookingCategories;
        notifyDataSetChanged();
    }

    public class BookingCategoryViewHolder extends RecyclerView.ViewHolder {
        public BookingCategoryView bookingCategoryView;

        public BookingCategoryViewHolder(BookingCategoryView itemView) {
            super(itemView);
            this.bookingCategoryView = itemView;
        }

        public BookingCategoryView getView() {
            return this.bookingCategoryView;
        }
    }
}
