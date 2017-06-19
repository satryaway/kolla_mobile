package com.jixstreet.kolla.booking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;
import com.jixstreet.kolla.booking.category.BookingCategory;
import com.jixstreet.kolla.booking.category.BookingEntity;
import com.jixstreet.kolla.booking.room.RoomJson;
import com.jixstreet.kolla.booking.room.RoomListActivity;
import com.jixstreet.kolla.booking.room.RoomListActivity_;
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

/**
 * Created by satryaway on 3/22/2017.
 * satryaway@gmail.com
 */

@EActivity(R.layout.activity_booking_detail)
public class BookingOptionActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final int requestCode = ActivityUtils.getRequestCode(BookingOptionActivity.class, "1");

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.location_spinner)
    protected Spinner locationSpinner;

    @ViewById(R.id.duration_spinner)
    protected Spinner durationSpinner;

    @ViewById(R.id.guest_count_spinner)
    protected Spinner guestCountSpinner;

    @ViewById(R.id.booking_date_tv)
    protected TextView bookingDateTv;

    @ViewById(R.id.booking_time_tv)
    protected TextView bookingTimeTv;

    @ViewById(R.id.more_wrapper)
    protected LinearLayout moreWrapper;

    private Calendar certainDate;
    private BookingCategory bookingCategory;
    private Booking booking = new Booking();

    @AfterViews
    void onViewsCreated() {
        bookingCategory = ActivityUtils.getParam(this, BookingCategory.paramKey, BookingCategory.class);
        if (bookingCategory == null) {
            finish();
            return;
        }

        booking.bookingCategory = bookingCategory;
        setLayout();
        initCalendar();
        initView();
    }

    private void setLayout() {
        if (bookingCategory.id != null &&
                (bookingCategory.id.equals(BookingEntity.OFFICE) ||
                        bookingCategory.id.equals(BookingEntity.HALL)))
            moreWrapper.setVisibility(View.GONE);
    }

    private void initView() {
        ViewUtils.setToolbar(this, toolbar);
        initSpinners();
    }

    private void initSpinners() {
        initSpinner(Seeder.getLocations(), locationSpinner, null);
        initSpinner(Seeder.getDurations(6), durationSpinner, null);
        initSpinner(Seeder.getGuests(10), guestCountSpinner, null);
    }

    private void initSpinner(List<String> collections, Spinner spinner,
                             AdapterView.OnItemSelectedListener itemListener) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, collections);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(itemListener);
        spinner.setAdapter(dataAdapter);
    }

    private void initCalendar() {
        certainDate = Calendar.getInstance();
        bookingDateTv.setText(DateUtils.getCurrentDate());

        certainDate.set(Calendar.HOUR_OF_DAY, DateUtils.getCurrentHour());
        certainDate.set(Calendar.MINUTE, 0);
        bookingTimeTv.setText(DateUtils.getTimeStr(certainDate.get(Calendar.HOUR_OF_DAY),
                certainDate.get(Calendar.MINUTE), 0));
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

    private void collectInformation() {
        RoomJson.Request request = new RoomJson.Request();
        request.location = Seeder.getLocations().get(locationSpinner.getSelectedItemPosition());
        request.date = getString(R.string.date_builder,
                certainDate.get(Calendar.DAY_OF_MONTH),
                certainDate.get(Calendar.MONTH) + 1,
                certainDate.get(Calendar.YEAR));
        request.time = ViewUtils.getTextFromTextView(bookingTimeTv);
        request.duration = String.valueOf(durationSpinner.getSelectedItemPosition() + 1);
        request.guest = String.valueOf(guestCountSpinner.getSelectedItemPosition() + 1);
        request.category = bookingCategory.id;
        request.bookingCategory = bookingCategory;
        request.isInitial = false;

        booking.roomRequest = request;
        ActivityUtils.startActivityWParamAndWait(this, RoomListActivity_.class,
                Booking.paramKey, booking, RoomListActivity.requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RoomListActivity.requestCode) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        bookingDateTv.setText(DateUtils.getDateTimeStr(dayOfMonth, monthOfYear, year));
        certainDate.set(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        bookingTimeTv.setText(DateUtils.getTimeStr(hourOfDay, minute, second));
        certainDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        certainDate.set(Calendar.MINUTE, 0);
    }

    @Click(R.id.booking_date_tv)
    void showDatePicker() {
        showDatePickerDialog(certainDate.get(Calendar.YEAR),
                certainDate.get(Calendar.MONTH),
                certainDate.get(Calendar.DAY_OF_MONTH));
    }

    @Click(R.id.booking_time_tv)
    void showTimePicker() {
        showTimePickerDialog(certainDate.get(Calendar.HOUR));
    }

    @Click(R.id.search_tv)
    void searchRooms() {
        collectInformation();
    }
}
