package com.jixstreet.kolla.booking;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.room.OnRoomSelected;
import com.jixstreet.kolla.booking.room.Room;
import com.jixstreet.kolla.booking.room.RoomView;
import com.jixstreet.kolla.booking.room.detail.RoomDetailActivity;
import com.jixstreet.kolla.booking.room.detail.RoomDetailActivity_;
import com.jixstreet.kolla.booking.room.payment.PaymentType;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DateUtils;
import com.jixstreet.kolla.utility.FormatUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_booking_confirmation)
public class BookingConfirmationActivity extends AppCompatActivity implements OnRoomSelected {
    @ViewById(R.id.room_wrapper)
    protected RoomView roomView;

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.points_tv)
    protected TextView pointsTv;

    @ViewById(R.id.booking_date_tv)
    protected TextView bookingDateTv;

    @ViewById(R.id.booking_time_tv)
    protected TextView bookingTimeTv;

    @ViewById(R.id.duration_tv)
    protected TextView durationTv;

    @ViewById(R.id.guest_tv)
    protected TextView guestTv;

    @ViewById(R.id.payment_type_tv)
    protected TextView paymentTypeTv;

    public static String paramKey = BookingConfirmationActivity.class.getName().concat("1");
    private Booking booking;

    @AfterViews
    protected void onViewsCreated() {
        ViewUtils.setToolbar(this, toolbar);
        booking = ActivityUtils.getParam(this, paramKey, Booking.class);
        if (booking != null) {
            setValue();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setValue() {
        if (booking.room != null && booking.roomRequest != null) {
            roomView.setRoom(booking.room);
            roomView.setOnRoomSelected(this);
            roomView.setIsOnlyView(true);
            ViewUtils.setTextView(bookingTimeTv, booking.roomRequest.time);
            ViewUtils.setTextView(guestTv, getString(R.string.s_guest, booking.roomRequest.guest));
            ViewUtils.setTextView(bookingDateTv, DateUtils.getDateTimeStr(booking.roomRequest.date,
                    getString(R.string.default_web_date_format), getString(R.string.default_date_format)));
            ViewUtils.setTextView(durationTv,
                    getString((Integer.valueOf(booking.roomRequest.duration) > 1 ?
                            R.string.s_durations : R.string.s_duration), booking.roomRequest.duration));
            if (booking.room.price_type.equals(PaymentType.CASH)) {
                ViewUtils.setTextView(paymentTypeTv, getString(R.string.price));
                ViewUtils.setTextView(pointsTv, FormatUtils.formatCurrency(booking.room.price));
            } else {
                ViewUtils.setTextView(paymentTypeTv, getString(R.string.kollacredits));
                ViewUtils.setTextView(pointsTv, booking.room.price);
            }
        }
    }

    @Override
    public void onSelect(Room room) {
        booking.room = room;
        ActivityUtils.startActivityWParam(this, RoomDetailActivity_.class,
                RoomDetailActivity.paramKey, booking);
    }
}
