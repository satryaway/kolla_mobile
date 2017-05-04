package com.jixstreet.kolla.booking.office;

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
import com.jixstreet.kolla.booking.Booking;
import com.jixstreet.kolla.booking.BookingConfirmationActivity;
import com.jixstreet.kolla.booking.BookingConfirmationActivity_;
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

@EActivity(R.layout.activity_survey_request_option)
public class SurveyRequestOptionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    public static int requestCode = ActivityUtils.getRequestCode(SurveyRequestOptionActivity.class, "1");

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.survey_date_tv)
    protected TextView surveyDateTv;

    @ViewById(R.id.survey_time_tv)
    protected TextView surveyTimeTv;

    @ViewById(R.id.office_size_sp)
    protected Spinner officeSizeSp;

    @ViewById(R.id.full_name_et)
    protected EditText fullNameEt;

    private Calendar certainDate;
    private Booking booking;

    @AfterViews
    protected void onViewsCreated() {
        booking = ActivityUtils.getParam(this, Booking.paramKey, Booking.class);

        ViewUtils.setToolbar(this, toolbar);
        initCalendar();
        initSpinners();
    }

    private void initCalendar() {
        certainDate = Calendar.getInstance();
        surveyDateTv.setText(DateUtils.getCurrentDate());

        certainDate.set(Calendar.HOUR_OF_DAY, DateUtils.getCurrentHour());
        certainDate.set(Calendar.MINUTE, 0);
        surveyTimeTv.setText(DateUtils.getTimeStr(certainDate.get(Calendar.HOUR_OF_DAY),
                certainDate.get(Calendar.MINUTE), 0));
    }

    private void initSpinners() {
        initSpinner(Seeder.getGuests(50), officeSizeSp, null);
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
        if (ViewUtils.getTextFromEditText(fullNameEt).isEmpty())
            return false;

        booking.bookingRequest.full_name = ViewUtils.getTextFromEditText(fullNameEt);
        booking.bookingRequest.date = getString(R.string.date_builder,
                certainDate.get(Calendar.DAY_OF_MONTH),
                certainDate.get(Calendar.MONTH) + 1,
                certainDate.get(Calendar.YEAR));
        booking.bookingRequest.time = ViewUtils.getTextFromTextView(surveyTimeTv);
        booking.bookingRequest.guest = String.valueOf(officeSizeSp.getSelectedItemPosition() + 1);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        surveyDateTv.setText(DateUtils.getDateTimeStr(dayOfMonth, monthOfYear, year));
        certainDate.set(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        surveyTimeTv.setText(DateUtils.getTimeStr(hourOfDay, minute, second));
        certainDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        certainDate.set(Calendar.MINUTE, 0);
    }

    @Click(R.id.survey_date_tv)
    void showDatePicker() {
        showDatePickerDialog(certainDate.get(Calendar.YEAR),
                certainDate.get(Calendar.MONTH),
                certainDate.get(Calendar.DAY_OF_MONTH));
    }

    @Click(R.id.survey_time_tv)
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
