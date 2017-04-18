package com.jixstreet.kolla.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.category.BookingCategory;
import com.jixstreet.kolla.booking.category.OnCategorySelected;
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
    private OnCategorySelected onCategorySelected;

    public BookingCategoryView(Context context) {
        super(context);
        this.context = context;
    }

    public void setBookingCategory(BookingCategory bookingCategory) {
        this.bookingCategory = bookingCategory;
        setValue();
    }

    private void setValue() {
        ImageUtils.loadImageWithPlaceHolder(context, bookingCategory.image, categoryImageIv, R.drawable.dummy_bg);
        categoryLabelTv.setText(bookingCategory.name);
    }

    public void setOnCategorySelected(OnCategorySelected onCategorySelected) {
        this.onCategorySelected = onCategorySelected;
    }

    @Click(R.id.item_wrapper)
    void callRoomList() {
        if (onCategorySelected == null) return;
        onCategorySelected.onSelect(bookingCategory);
    }
}
