package com.jixstreet.kolla.booking;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;
import com.jixstreet.kolla.utility.DateUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;

/**
 * Created by satryaway on 3/22/2017.
 * satryaway@gmail.com
 */

@EActivity(R.layout.activity_booking_detail)
public class BookingDetailActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.location_spinner)
    protected Spinner locationSpinner;

    @ViewById(R.id.booking_date_tv)
    protected TextView bookingDateTv;
    private Calendar certainDate;

    @AfterViews
    void onViewsCreated() {
        initCalendar();
        initView();
    }

    private void initView() {
        initToolbar();
        initSpinners();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initSpinners() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Seeder.getLocations());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(dataAdapter);
    }

    private void initCalendar() {
        certainDate = Calendar.getInstance();
        bookingDateTv.setText(DateUtils.getCurrentDate());
    }

    private void showDatePickerDialog(int year, int month, int day) {
        DatePickerDialog dpd = DatePickerDialog.newInstance(this, year, month, day);
        dpd.show(getFragmentManager(), BookingDetailActivity.class.getCanonicalName());
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        bookingDateTv.setText(DateUtils.getDateTimeStr(dayOfMonth, monthOfYear, year));
        certainDate.set(year, monthOfYear, dayOfMonth);
    }

    @Click(R.id.booking_date_tv)
    void showDatePicker() {
        showDatePickerDialog(certainDate.get(Calendar.YEAR),
                certainDate.get(Calendar.MONTH),
                certainDate.get(Calendar.DAY_OF_MONTH));
    }
}
