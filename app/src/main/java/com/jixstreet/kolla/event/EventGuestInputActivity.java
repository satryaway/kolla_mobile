package com.jixstreet.kolla.event;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.Log;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_event_guest_input)
public class EventGuestInputActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final int requestCode = ActivityUtils.getRequestCode(EventGuestInputActivity.class, "1");
    private static final int GUEST_COUNT = 5;

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.guest_wrapper)
    protected LinearLayout guestWrapper;

    @ViewById(R.id.guest_count_spinner)
    protected Spinner guestSpinner;

    private Event event;
    private List<EventGuestInputView> guestWrapperList;

    @AfterViews
    protected void onViewsCreated() {
        ViewUtils.setToolbar(this, toolbar);
        event = ActivityUtils.getParam(this, Event.paramKey, Event.class);
        if (event == null) {
            finish();
            return;
        }

        initSpinner(Seeder.getGuestWithPrice(this, event.booking_fee, GUEST_COUNT), guestSpinner, this);
    }

    private void initSpinner(List<String> collections, Spinner spinner,
                             AdapterView.OnItemSelectedListener itemListener) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, collections);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(itemListener);
        spinner.setAdapter(dataAdapter);
    }

    private void setGuestsWrapper(int count) {
        guestWrapper.removeAllViews();
        guestWrapperList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            EventGuestInputView guestInputView = EventGuestInputView_.build(this);
            guestInputView.setForm(i + 1);
            guestWrapperList.add(guestInputView);
            guestWrapper.addView(guestInputView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EventBookingConfirmationActivity.requestCode) {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setGuestsWrapper(position + 1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Click(R.id.next_tv)
    protected void next() {
        ViewUtils.hideSoftKeyboard(this);
        List<Guest> guests = new ArrayList<>();
        boolean isCompleted = true;

        for (EventGuestInputView view : guestWrapperList) {
            Guest guest = view.getGuestDetail();
            if (guest == null)
                isCompleted = false;

            guests.add(guest);
        }

        if (!isCompleted) return;
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(guests, new TypeToken<List<Guest>>() {
        }.getType());

        String guestValue = element.getAsJsonArray().toString();
        Log.d("Guest Array", guestValue);

        event.guests = guests;
        event.guestsCount = String.valueOf(guests.size()*Integer.valueOf(event.booking_fee));
        event.guestArray = guestValue;
        ActivityUtils.startActivityWParamAndWait(this, EventBookingConfirmationActivity_.class,
                Event.paramKey, event, EventBookingConfirmationActivity.requestCode);
    }
}
