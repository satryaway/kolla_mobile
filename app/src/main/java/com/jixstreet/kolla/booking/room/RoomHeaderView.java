package com.jixstreet.kolla.booking.room;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.category.BookingCategory;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 3/7/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.view_room_header)
public class RoomHeaderView extends RelativeLayout {
    @ViewById(R.id.room_header_title_tv)
    protected TextView roomHeaderTitleTv;

    @ViewById(R.id.room_header_description_tv)
    protected TextView roomHeaderDescriptionTv;

    private BookingCategory bookingCategory;

    public RoomHeaderView(Context context) {
        super(context);
    }

    public void setBookingCategory(BookingCategory bookingCategory) {
        this.bookingCategory = bookingCategory;
        setValue();
    }

    private void setValue() {
        roomHeaderTitleTv.setText(bookingCategory.name);
        roomHeaderDescriptionTv.setText(bookingCategory.description);
    }
}
