package com.jixstreet.kolla.booking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;
import com.jixstreet.kolla.booking.room.Room;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DateUtils;
import com.jixstreet.kolla.utility.ViewUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.List;

@EActivity(R.layout.activity_booking_size)
public class BookingSizeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    public static final int requestCode = ActivityUtils.getRequestCode(BookingSizeActivity.class, "1");

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.event_name_et)
    protected EditText eventNameEt;

    @ViewById(R.id.event_date_tv)
    protected TextView eventDateTv;

    @ViewById(R.id.event_time_tv)
    protected TextView eventTimeTv;

    @ViewById(R.id.duration_spinner)
    protected Spinner durationSp;

    @ViewById(R.id.booking_type_spinner)
    protected Spinner bookingTypeSp;

    @ViewById(R.id.payment_type_spinner)
    protected Spinner paymentTypeSp;

    @ViewById(R.id.pax_et)
    protected EditText paxEt;

    @ViewById(R.id.pax_tv)
    protected TextView paxTv;

    private Calendar certainDate;
    private Booking booking;

    @AfterViews
    protected void onViewsCreated() {
        booking = ActivityUtils.getParam(this, Booking.paramKey, Booking.class);
        if (booking == null) {
            finish();
            return;
        }

        booking.bookingRequest = new BookingJson.Request();
        ViewUtils.setToolbar(this, toolbar);
        setValue();
        initCalendar();
        initSpinners();
    }

    private void setValue() {
        ViewUtils.setTextView(paxTv, getString(R.string.number_of_pax_s, booking.room.size));
        ViewUtils.setEditTextHint(paxEt, booking.room.size);
    }

    private void initCalendar() {
        certainDate = Calendar.getInstance();
        eventDateTv.setText(DateUtils.getCurrentDate());

        certainDate.set(Calendar.HOUR_OF_DAY, DateUtils.getCurrentHour());
        certainDate.set(Calendar.MINUTE, 0);
        eventTimeTv.setText(DateUtils.getTimeStr(certainDate.get(Calendar.HOUR_OF_DAY),
                certainDate.get(Calendar.MINUTE), 0));
    }

    private void initSpinners() {
        initSpinner(Seeder.getDurations(6), durationSp, null);
        initSpinner(Seeder.getBookingType(), bookingTypeSp, null);
        initSpinner(Seeder.getPaymentType(), paymentTypeSp, null);
    }

    private void initSpinner(List<String> collections, Spinner spinner,
                             AdapterView.OnItemSelectedListener itemListener) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, collections);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(itemListener);
        spinner.setAdapter(dataAdapter);
    }

    private void showDatePickerDialog(int year, int month, int day) {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, year, month, day);
        datePickerDialog.show(getFragmentManager(), "Date Picker");
    }

    private void showTimePickerDialog(int hour) {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, hour, 0, true);
        timePickerDialog.enableMinutes(false);
        timePickerDialog.enableSeconds(false);
        timePickerDialog.show(getFragmentManager(), "Time Picker");
    }

    private boolean isComplete() {
        int minPax = Integer.valueOf(booking.room.size);

        if (ViewUtils.getTextFromEditText(eventNameEt).isEmpty()) {
            ViewUtils.setError(eventNameEt, getString(R.string.field_required));
            return false;
        }

        if (ViewUtils.getTextFromEditText(paxEt).isEmpty()) {
            ViewUtils.setError(paxEt, getString(R.string.field_required));
            return false;
        } else {
            int inputtedPax = Integer.valueOf(ViewUtils.getTextFromEditText(paxEt));
            if (inputtedPax < minPax) {
                ViewUtils.setError(paxEt, getString(R.string.minimum_pax_is_s, booking.room.size));
                return false;
            }
        }

        booking.bookingRequest.event_name = ViewUtils.getTextFromEditText(eventNameEt);
        booking.bookingRequest.date = getString(R.string.date_builder,
                certainDate.get(Calendar.DAY_OF_MONTH),
                certainDate.get(Calendar.MONTH) + 1,
                certainDate.get(Calendar.YEAR));
        booking.bookingRequest.time = ViewUtils.getTextFromTextView(eventTimeTv);
        booking.bookingRequest.duration = String.valueOf(durationSp.getSelectedItemPosition() + 1);
        booking.bookingRequest.booking_type = Seeder.getBookingType().get(bookingTypeSp.getSelectedItemPosition());
        booking.bookingRequest.guest = ViewUtils.getTextFromEditText(paxEt);
        booking.bookingRequest.payment_type = Seeder.getPaymentType().get(paymentTypeSp.getSelectedItemPosition());

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BookingConfirmationActivity.requestCode) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        eventDateTv.setText(DateUtils.getDateTimeStr(dayOfMonth, monthOfYear, year));
        certainDate.set(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        eventTimeTv.setText(DateUtils.getTimeStr(hourOfDay, minute, second));
        certainDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        certainDate.set(Calendar.MINUTE, 0);
    }

    @Click(R.id.event_date_tv)
    void showDatePicker() {
        showDatePickerDialog(certainDate.get(Calendar.YEAR),
                certainDate.get(Calendar.MONTH),
                certainDate.get(Calendar.DAY_OF_MONTH));
    }

    @Click(R.id.event_time_tv)
    void showTimePicker() {
        showTimePickerDialog(certainDate.get(Calendar.HOUR));
    }

    @Click(R.id.next_tv)
    void next() {
        if (isComplete()) {
            ActivityUtils.startActivityWParamAndWait(this, BookingConfirmationActivity_.class,
                    Booking.paramKey, booking, BookingConfirmationActivity.requestCode);
        }
    }
}
