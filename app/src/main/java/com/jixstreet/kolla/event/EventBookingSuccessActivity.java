package com.jixstreet.kolla.event;

import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.BookingParameterView;
import com.jixstreet.kolla.booking.BookingParameterView_;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_event_booking_success)
public class EventBookingSuccessActivity extends AppCompatActivity {
    public static int requestCode = ActivityUtils.getRequestCode(EventBookingSuccessActivity.class, "1");

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.params_wrapper)
    protected LinearLayout paramsWrapper;

    @ViewById(R.id.event_view)
    protected EventView eventView;

    private Event event;

    @AfterViews
    protected void onViewsCreated() {
        ViewUtils.setToolbar(this, toolbar);
        event = ActivityUtils.getParam(this, Event.paramKey, Event.class);
        if (event == null) {
            finish();
            return;
        }

        setValue();
    }

    private void setValue() {
        eventView.setItem(event);
        makeViews(buildParams());
    }

    private void makeViews(ArrayList<Pair<String, String>> pairs) {
        paramsWrapper.removeAllViews();
        for (Pair<String, String> pair : pairs) {
            BookingParameterView bookingParameterView = BookingParameterView_.build(this);
            bookingParameterView.addValue(pair.first, pair.second);
            paramsWrapper.addView(bookingParameterView);
        }
    }

    private ArrayList<Pair<String, String>> buildParams() {
        ArrayList<Pair<String, String>> params = new ArrayList<>();
        params.add(new Pair<>(getString(R.string.guest), String.valueOf(event.who_comes.size())));
        params.add(new Pair<>(getString(R.string.fee), event.guestsCount));
        params.add(new Pair<>(getString(R.string.payment), getString(R.string.kolla_credit)));

        return params;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        super.onBackPressed();
    }

    @Click(R.id.back_tv)
    protected void goBack() {
        onBackPressed();
    }
}
