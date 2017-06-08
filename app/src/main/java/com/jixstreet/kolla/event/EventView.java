package com.jixstreet.kolla.event;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.utility.DateUtils;
import com.jixstreet.kolla.utility.ImageUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 5/17/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.item_event)
public class EventView extends RelativeLayout {

    @ViewById(R.id.event_image_iv)
    protected ImageView eventImageIv;
    @ViewById(R.id.sold_out_tv)
    protected TextView soldOutTv;

    @ViewById(R.id.notes_tv)
    protected TextView notesTv;

    @ViewById(R.id.time_tv)
    protected TextView timeTv;

    @ViewById(R.id.date_tv)
    protected TextView dateTv;

    @ViewById(R.id.title_tv)
    protected TextView titleTv;

    @ViewById(R.id.location_tv)
    protected TextView locationTv;

    private final Context context;
    private Event item;

    private static final String SOLD_OUT = "Sold Out";
    private OnEventSelectedListener onEventSelectedListener;

    public interface OnEventSelectedListener {
        void onClick(Event event);
    }

    public EventView(Context context) {
        super(context);
        this.context = context;
    }

    public void setItem(Event item) {
        this.item = item;
        setValue();
    }

    private void setValue() {
        if (item == null) return;

        ViewUtils.setTextView(titleTv, item.name);
        ViewUtils.setTextView(locationTv, item.location);
        ViewUtils.setTextView(timeTv, DateUtils.getDateTimeFromMillis(item.start_datetime, "hh:mm"));
        ViewUtils.setTextView(dateTv, DateUtils.getDateTimeFromMillis(item.start_datetime, "dd MMM"));
        ViewUtils.setTextViewByVisibility(notesTv, item.notes);
        ViewUtils.setVisibility(soldOutTv, item.notes.equals(SOLD_OUT) ? VISIBLE : GONE);

        if (item.images.size() > 0)
            ImageUtils.loadImage(context, item.images.get(0).file, eventImageIv);
    }

    //TODO : TO BE DELETED
    private void setDummy() {
        ViewUtils.setTextView(titleTv, "Google Conference 2017");
        ViewUtils.setTextView(locationTv, "Istana Plaza - Bandung");
        ViewUtils.setTextView(timeTv, "18:00");
        ViewUtils.setTextView(dateTv, "27 Apr");
        ViewUtils.setTextViewByVisibility(notesTv, "Popular");
        ViewUtils.setVisibility(soldOutTv, GONE);
        ImageUtils.loadImageWithPlaceHolder(context, "", eventImageIv, R.drawable.dummy_bg);
    }

    public void setOnEventSelectedListener(OnEventSelectedListener onEventSelectedListener) {
        this.onEventSelectedListener = onEventSelectedListener;
    }

    @Click(R.id.content_wrapper)
    void showDetail() {
        this.onEventSelectedListener.onClick(item);
    }
}
