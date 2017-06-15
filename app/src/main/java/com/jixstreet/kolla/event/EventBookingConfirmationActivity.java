package com.jixstreet.kolla.event;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.BookingParameterView;
import com.jixstreet.kolla.booking.BookingParameterView_;
import com.jixstreet.kolla.credit.CreditSufficientStatus;
import com.jixstreet.kolla.library.Callback;
import com.jixstreet.kolla.topup.TopUpListActivity_;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_event_booking_confirmation)
public class EventBookingConfirmationActivity extends AppCompatActivity implements EventView.OnEventSelectedListener {
    public static int requestCode = ActivityUtils.getRequestCode(EventBookingConfirmationActivity.class, "1");

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.params_wrapper)
    protected LinearLayout paramsWrapper;

    @ViewById(R.id.event_view)
    protected EventView eventView;

    private Event event;
    private EventBookingJson eventBookingJson;

    @AfterViews
    protected void onViewsChanged() {
        ViewUtils.setToolbar(this, toolbar);
        event = ActivityUtils.getParam(this, Event.paramKey, Event.class);
        if (event == null) {
            finish();
            return;
        }

        eventBookingJson = new EventBookingJson(this, event.id);
        setValue();
    }

    private void setValue() {
        eventView.setItem(event);
        eventView.setOnEventSelectedListener(this);
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

    private void requestBooking() {
        EventBookingJson.Request request = new EventBookingJson.Request();
        request.guests = event.guestArray;
        eventBookingJson.post(request, new OnEventBooking() {
            @Override
            public void onSuccess(EventBookingJson.Response response) {
                if (response.data.status.equals(CreditSufficientStatus.ENOUGH)) {
                    event.message = response.message;
                    ActivityUtils.startActivityWParamAndWait(EventBookingConfirmationActivity.this,
                            EventBookingSuccessActivity_.class, Event.paramKey, event, EventBookingSuccessActivity.requestCode);
                } else {
                    showTopupDialog(response.message);
                }
            }

            @Override
            public void onFailure(String message) {
                DialogUtils.makeSnackBar(CommonConstant.failed,
                        EventBookingConfirmationActivity.this, message);
            }
        });
    }

    private void showTopupDialog(String message) {
        DialogUtils.makeTwoButtonDialog(this, getString(R.string.insufficient_balance), message,
                getString(R.string.top_up_kolla_credit), getString(R.string.cancel), new Callback<Boolean>() {
                    @Override
                    public void run(@Nullable DialogInterface dialog, @Nullable Boolean param) {
                        if (param)
                            ActivityUtils.startActivity(EventBookingConfirmationActivity.this, TopUpListActivity_.class);
                        else dialog.dismiss();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EventBookingSuccessActivity.requestCode) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Click(R.id.submit_tv)
    protected void submitBooking() {
        requestBooking();
    }

    @Override
    public void onClick(Event event) {
        event.isActive = false;
        ActivityUtils.startActivityWParamAndWait(this, EventDetailActivity_.class,
                Event.paramKey, event, EventDetailActivity.requestCode);
    }
}
