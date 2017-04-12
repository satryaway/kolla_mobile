package com.jixstreet.kolla.booking;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;
import com.jixstreet.kolla.booking.room.RoomJson;
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
public class BookingDetailActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final int requestCode = ActivityUtils.getRequestCode(BookingDetailActivity.class, "1");
    public static final String resultKey = BookingDetailActivity.class.getName().concat("1");

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

    private Calendar certainDate;
    private RoomJson.Request roomParam;
    private List<String> locations, durations, guests;

    @AfterViews
    void onViewsCreated() {
        roomParam = ActivityUtils.getParam(this, RoomJson.paramKey, RoomJson.Request.class);
        if (roomParam == null)
            roomParam = new RoomJson.Request();

        collectDummyData();
        initCalendar();
        initView();
    }

    private void collectDummyData() {
        locations = Seeder.getLocations();
        durations = Seeder.getDurations(6);
        guests = Seeder.getGuests(10);
    }

    private void initView() {
        ViewUtils.setToolbar(this, toolbar);
        initSpinners();
    }

    private void initSpinners() {
        initSpinner(Seeder.getLocations(), locationSpinner, selectedLocationListener);
        initSpinner(Seeder.getDurations(6), durationSpinner, selectedDurationListener);
        initSpinner(Seeder.getGuests(10), guestCountSpinner, selectedGuestListener);
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
        bookingTimeTv.setText(DateUtils.getCurrentTime());
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

    private AdapterView.OnItemSelectedListener selectedLocationListener
            = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener selectedDurationListener
            = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener selectedGuestListener
            = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void collectInformation() {
        roomParam.location = locations.get(locationSpinner.getSelectedItemPosition());
        roomParam.date = getString(R.string.date_builder,
                certainDate.get(Calendar.DAY_OF_MONTH),
                certainDate.get(Calendar.MONTH) + 1,
                certainDate.get(Calendar.YEAR));
        roomParam.time = getString(R.string.time_builder,
                certainDate.get(Calendar.HOUR_OF_DAY),
                "00");
        roomParam.duration = String.valueOf(durationSpinner.getSelectedItemPosition()+1);
        roomParam.guest = String.valueOf(guestCountSpinner.getSelectedItemPosition()+1);
        roomParam.isInitial = false;

        ActivityUtils.returnWithResult(this, resultKey, roomParam);
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

    @Override
    public void onBackPressed() {
        if (roomParam.isInitial) {
            setResult(RESULT_CANCELED);
            super.onBackPressed();
        } else {
            collectInformation();
        }
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
