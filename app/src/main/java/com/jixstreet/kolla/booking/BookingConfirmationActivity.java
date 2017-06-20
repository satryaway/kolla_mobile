package com.jixstreet.kolla.booking;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.category.BookingEntity;
import com.jixstreet.kolla.booking.room.OnRoomSelected;
import com.jixstreet.kolla.booking.room.Room;
import com.jixstreet.kolla.booking.room.RoomView;
import com.jixstreet.kolla.booking.room.detail.RoomDetailActivity_;
import com.jixstreet.kolla.credit.CreditSufficientStatus;
import com.jixstreet.kolla.library.Callback;
import com.jixstreet.kolla.payment.PaymentType;
import com.jixstreet.kolla.topup.TopUpListActivity_;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DateUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.FormatUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_booking_confirmation)
public class BookingConfirmationActivity extends AppCompatActivity implements OnRoomSelected {
    public static int requestCode = ActivityUtils.getRequestCode(BookingConfirmationActivity.class, "1");

    @ViewById(R.id.room_wrapper)
    protected RoomView roomView;

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.params_wrapper)
    protected LinearLayout paramsWrapper;

    public static String paramKey = BookingConfirmationActivity.class.getName().concat("1");
    private Booking booking;
    private BookingJson bookingJson;

    @AfterViews
    protected void onViewsCreated() {
        booking = ActivityUtils.getParam(this, Booking.paramKey, Booking.class);
        if (booking == null) {
            finish();
            return;
        }

        ViewUtils.setToolbar(this, toolbar);
        bookingJson = new BookingJson(this, booking.room.id);
        setValue();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setValue() {
        if (booking != null && booking.roomRequest != null) {
            roomView.setRoom(booking.room);
            roomView.setOnRoomSelected(this);
            roomView.setIsOnlyView(true);

            switch (booking.bookingCategory.id) {
                case BookingEntity.HALL:
                    makeViews(buildHallParams());
                    break;
                case BookingEntity.OFFICE:
                    makeViews(buildOfficeParams());
                    break;
                default:
                    makeViews(buildRoomParams());
                    break;
            }
        }
    }

    private void makeViews(ArrayList<Pair<String, String>> pairs) {
        paramsWrapper.removeAllViews();
        for (Pair<String, String> pair : pairs) {
            BookingParameterView bookingParameterView = BookingParameterView_.build(this);
            bookingParameterView.addValue(pair.first, pair.second);
            paramsWrapper.addView(bookingParameterView);
        }
    }

    private ArrayList<Pair<String, String>> buildRoomParams() {
        ArrayList<Pair<String, String>> params = new ArrayList<>();
        if (booking.room.price_type.equals(PaymentType.CASH))
            params.add(new Pair<>(getString(R.string.price), FormatUtils.formatCurrency(booking.room.price)));
        else
            params.add(new Pair<>(getString(R.string.kollacredits), booking.room.price));
        params.add(new Pair<>(getString(R.string.booking_date), DateUtils.getDateTimeStr(booking.bookingRequest.date,
                getString(R.string.default_web_date_format), getString(R.string.default_date_format))));
        params.add(new Pair<>(getString(R.string.booking_time), booking.bookingRequest.time));
        params.add(new Pair<>(getString(R.string.duration), getString((Integer.valueOf(booking.bookingRequest.duration) > 1 ?
                R.string.s_durations : R.string.s_duration), booking.bookingRequest.duration)));
        params.add(new Pair<>(getString(R.string.guest), getString(R.string.s_guest, booking.bookingRequest.guest)));

        return params;
    }

    private ArrayList<Pair<String, String>> buildOfficeParams() {
        ArrayList<Pair<String, String>> params = new ArrayList<>();
        params.add(new Pair<>(getString(R.string.full_name), booking.bookingRequest.full_name));
        params.add(new Pair<>(getString(R.string.survey_date), DateUtils.getDateTimeStr(booking.bookingRequest.date,
                getString(R.string.default_web_date_format), getString(R.string.default_date_format))));
        params.add(new Pair<>(getString(R.string.survey_time), booking.bookingRequest.time));
        params.add(new Pair<>(getString(R.string.office_size), getString(R.string.s_guest, booking.bookingRequest.guest)));

        return params;
    }

    private ArrayList<Pair<String, String>> buildHallParams() {
        ArrayList<Pair<String, String>> params = new ArrayList<>();
        params.add(new Pair<>(getString(R.string.name), booking.bookingRequest.event_name));
        params.add(new Pair<>(getString(R.string.date), DateUtils.getDateTimeStr(booking.bookingRequest.date,
                getString(R.string.default_web_date_format), getString(R.string.default_date_format))));
        params.add(new Pair<>(getString(R.string.time), booking.bookingRequest.time));
        params.add(new Pair<>(getString(R.string.duration), getString((Integer.valueOf(booking.bookingRequest.duration) > 1 ?
                R.string.s_durations : R.string.s_duration), booking.bookingRequest.duration)));
        params.add(new Pair<>(getString(R.string.type), booking.bookingRequest.booking_type));
        params.add(new Pair<>(getString(R.string.pax), booking.bookingRequest.guest));
        params.add(new Pair<>(getString(R.string.payment), booking.bookingRequest.payment_type));

        return params;
    }

    private void doBooking() {
        bookingJson.setBooking(booking.bookingRequest, new OnBooking() {
            @Override
            public void onSuccess(BookingJson.Response response) {
                if (response.data.status.equals(CreditSufficientStatus.NOT_ENOUGH)) {
                    showTopUpDialog(response.message);
                } else {
                    booking.bookingResponse = response;
                    ActivityUtils.startActivityWParamAndWait(BookingConfirmationActivity.this,
                            BookingSuccessActivity_.class, Booking.paramKey, booking,
                            BookingSuccessActivity.requestCode);
                }
            }

            @Override
            public void onFailure(String message) {
                DialogUtils.makeSnackBar(CommonConstant.failed,
                        BookingConfirmationActivity.this, message);
            }
        });
    }

    private void showTopUpDialog(String message) {
        DialogUtils.makeTwoButtonDialog(this, getString(R.string.insufficient_balance), message,
                getString(R.string.top_up_kolla_credit), getString(R.string.cancel), new Callback<Boolean>() {
                    @Override
                    public void run(@Nullable DialogInterface dialog, @Nullable Boolean param) {
                        if (param)
                            ActivityUtils.startActivity(BookingConfirmationActivity.this, TopUpListActivity_.class);
                        else dialog.dismiss();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BookingSuccessActivity.requestCode) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void onSelect(Room room) {
        Booking booking = new Booking();
        booking.room = room;
        ActivityUtils.startActivityWParam(this, RoomDetailActivity_.class,
                Booking.paramKey, booking);
    }

    @Click(R.id.submit_tv)
    protected void submitBooking() {
        doBooking();
    }
}
