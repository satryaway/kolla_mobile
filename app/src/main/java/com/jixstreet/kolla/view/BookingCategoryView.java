package com.jixstreet.kolla.view;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.room.RoomListActivity_;
import com.jixstreet.kolla.booking.category.BookingCategory;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.ImageUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 3/6/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.item_category)
public class BookingCategoryView extends RelativeLayout {

    private Context context;

    @ViewById(R.id.category_image_iv)
    protected ImageView categoryImageIv;

    @ViewById(R.id.category_label_tv)
    protected TextView categoryLabelTv;

    private BookingCategory bookingCategory;

    public BookingCategoryView(Context context) {
        super(context);
        this.context = context;
    }

    public void setBookingCategory(BookingCategory bookingCategory) {
        this.bookingCategory = bookingCategory;
        setValue();
    }

    private void setValue() {
        ImageUtils.loadLocalImage(context, categoryImageIv, bookingCategory.src);
        categoryLabelTv.setText(bookingCategory.label);
    }

    @Click(R.id.item_wrapper)
    void callRoomList() {
        ActivityUtils.startActivity((Activity) context, RoomListActivity_.class);
    }
}
