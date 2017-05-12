package com.jixstreet.kolla.booking;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 4/18/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.view_booking_parameter)
public class BookingParameterView extends LinearLayout {
    @ViewById(R.id.title_tv)
    protected TextView titleTv;

    @ViewById(R.id.value_tv)
    protected TextView valueTv;

    public BookingParameterView(Context context) {
        super(context);
    }

    public BookingParameterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addValue(String title, String value) {
        ViewUtils.setTextView(titleTv, title);
        ViewUtils.setTextView(valueTv, value);
    }

    public void addMaxLines(int max) {
        valueTv.setMaxLines(max);
    }
}
