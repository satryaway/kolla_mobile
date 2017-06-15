package com.jixstreet.kolla.event;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.friend.FriendThumbListAdapter;
import com.jixstreet.kolla.friend.FriendThumbView;
import com.jixstreet.kolla.model.UserData;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DateUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ImageUtils;
import com.jixstreet.kolla.utility.PermissionUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_event_detail)
public class EventDetailActivity extends AppCompatActivity
        implements FriendThumbView.OnThumbClickListener {

    public static int requestCode = ActivityUtils.getRequestCode(EventDetailActivity.class, "1");

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.appbar)
    protected AppBarLayout appBarLayout;

    @ViewById(R.id.map_view)
    protected MapView mapView;

    @ViewById(R.id.description_tv)
    protected TextView descriptionTv;

    @ViewById(R.id.buy_now_tv)
    protected TextView buyNowTv;

    @ViewById(R.id.notes_tv)
    protected TextView notesTv;

    @ViewById(R.id.start_date_tv)
    protected TextView startDateTv;

    @ViewById(R.id.end_date_tv)
    protected TextView endDateTv;

    @ViewById(R.id.title_tv)
    protected TextView titleTv;

    @ViewById(R.id.location_tv)
    protected TextView locationTv;

    @ViewById(R.id.price_tv)
    protected TextView priceTv;

    @ViewById(R.id.list_rv)
    protected RecyclerView listRv;

    @ViewById(R.id.bottom_sheet)
    protected NestedScrollView bottomSheet;

    @ViewById(R.id.faded_layout)
    protected RelativeLayout fadedLayout;

    @AnimationRes(R.anim.fade_in_effect)
    protected Animation fadeIn;

    @AnimationRes(R.anim.fade_out_effect)
    protected Animation fadeOut;

    @ViewById(R.id.guest_count_tv)
    protected TextView guestCountTv;

    @ViewById(R.id.event_image_iv)
    protected ImageView eventImageIv;

    @ViewById(R.id.button_wrapper)
    protected RelativeLayout buttonWrapper;

    private Bundle bundle;
    private LatLng latlng;
    private FriendThumbListAdapter adapter;
    private BottomSheetBehavior mBottomSheetBehavior;
    private Event event;
    private EventDetailJson eventDetailJson;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bundle = savedInstanceState;
    }

    @AfterViews
    protected void onViewsCreated() {
        event = ActivityUtils.getParam(this, Event.paramKey, Event.class);
        if (event == null) {
            finish();
            return;
        }

        eventDetailJson = new EventDetailJson(this, event.id);
        initToolbar();
        initBottomSheet();
        initAdapter();
        setData();
        setActiveButton();
        initMap();
        getEventDetail();
    }

    private void setData() {
        ViewUtils.setTextView(descriptionTv, event.description);
        ViewUtils.setTextView(buyNowTv, getString(R.string.buy_now_s, event.booking_fee));
        ViewUtils.setTextView(startDateTv, getString(R.string.start_date_s,
                DateUtils.getDateTimeStrFromMillis(event.start_datetime, "dd MMM yyyy, hh:mm")));
        ViewUtils.setTextView(endDateTv, getString(R.string.end_date_s,
                DateUtils.getDateTimeStrFromMillis(event.end_datetime, "dd MMM yyyy, hh:mm")));
        ViewUtils.setTextView(locationTv, event.location);
        ViewUtils.setTextView(priceTv, getString(R.string.s_per_guest, event.booking_fee));
        ViewUtils.setTextView(notesTv, event.notes);
        ViewUtils.setTextView(titleTv, event.name);

        if (event.images.size() > 0)
            ImageUtils.loadImage(this, event.images.get(0).file, eventImageIv);
    }

    private void setActiveButton() {
        ViewUtils.setVisibility(buttonWrapper, event.isActive ? View.VISIBLE : View.GONE);
    }

    private void initToolbar() {
        ViewUtils.setToolbar(this, toolbar);
        appBarLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    private void initBottomSheet() {
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);
    }

    private void initAdapter() {
        adapter = new FriendThumbListAdapter();
        adapter.setOnThumbClickListener(this);
        LinearLayoutManager layoutManager = ViewUtils.getHorizontalLayoutManager(this);

        listRv.setNestedScrollingEnabled(false);
        listRv.setClipToPadding(true);
        listRv.setLayoutManager(layoutManager);
        listRv.setItemAnimator(new DefaultItemAnimator());

        listRv.setAdapter(adapter);
    }

    private void initMap() {
        mapView.onCreate(bundle);
        mapView.onResume();

        String[] latLngArray = event.map.split(",");
        if (latLngArray[0] != null && latLngArray[1] != null) {
            Double lat = Double.valueOf(latLngArray[0]);
            Double lng = Double.valueOf(latLngArray[1]);

            latlng = new LatLng(lat, lng);
        } else return;

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(onMapReadyCallBack);
    }

    private OnMapReadyCallback onMapReadyCallBack = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            EventDetailActivity.this.googleMap = googleMap;
            disableLocation();
            googleMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(event.name)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12.0f));
        }
    };

    //This method is used to avoid google Map memory leak
    private void disableLocation() {
        if (googleMap == null) return;
        if (ActivityCompat.checkSelfPermission(EventDetailActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(EventDetailActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            PermissionUtils.requestPermissions(EventDetailActivity.this, PermissionUtils.PERMISSIONS_LOCATION,
                    PermissionUtils.REQUEST_LOCATION, getString(R.string.grant_permission_warning));

            return;
        }

        googleMap.setMyLocationEnabled(false);
    }

    private void getEventDetail() {
        eventDetailJson.get(new OnGetEventDetail() {
            @Override
            public void onSuccess(EventDetailJson.Response response) {
                event = response.data;
                setFullData();
            }

            @Override
            public void onFailure(String message) {
                DialogUtils.makeSnackBar(CommonConstant.failed, EventDetailActivity.this, message);
            }
        });
    }

    private void setFullData() {
        setData();
        adapter.setItemList(getPeople());
        ViewUtils.setTextView(guestCountTv, getString(R.string.s_people_already_book,
                String.valueOf(adapter.getItemCount())));
    }

    private List<UserData> getPeople() {
        List<UserData> list = new ArrayList<>();
        for (Guest who_come : event.who_comes) {
            if (who_come.profile_picture != null)
                list.add(who_come);
        }

        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EventGuestInputActivity.requestCode) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.REQUEST_LOCATION: {
                if (grantResults.length < 1)
                    return;
                if (!PermissionUtils.checkPermissions(EventDetailActivity.this,
                        PermissionUtils.PERMISSIONS_LOCATION)) {

                    DialogUtils.makeToast(this, "Permission denied");
                    finish();
                } else {
                    mapView.getMapAsync(onMapReadyCallBack);
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onClick(UserData friend) {
    }

    @Click(R.id.expand_iv)
    protected void expand() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            switch (newState) {
                case BottomSheetBehavior.STATE_EXPANDED:
                    setFadedLayout(true);
                    break;
                case BottomSheetBehavior.STATE_COLLAPSED:
                    setFadedLayout(false);
                    break;
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        disableLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disableLocation();
        if (googleMap != null)
            googleMap.clear();
    }

    @Click(R.id.buy_now_tv)
    protected void inputGuest() {
        ActivityUtils.startActivityWParamAndWait(this, EventGuestInputActivity_.class,
                Event.paramKey, event, EventGuestInputActivity.requestCode);
    }

    private void setFadedLayout(boolean isShowing) {
        fadedLayout.startAnimation(isShowing ? fadeIn : fadeOut);
        fadedLayout.setVisibility(isShowing ? View.VISIBLE : View.INVISIBLE);
    }


}
