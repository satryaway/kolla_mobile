package com.jixstreet.kolla.event;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
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
import com.jixstreet.kolla.Friend.Friend;
import com.jixstreet.kolla.Friend.FriendThumbListAdapter;
import com.jixstreet.kolla.Friend.FriendThumbView;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.tools.EndlessRecyclerViewScrollListener;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

@EActivity(R.layout.activity_event_detail)
public class EventDetailActivity extends AppCompatActivity
        implements FriendThumbView.OnThumbClickListener{

    private static final int OFFSET = 10;
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

    @ViewById(R.id.datetime_tv)
    protected TextView dateTimeTv;

    @ViewById(R.id.title_tv)
    protected TextView titleTv;

    @ViewById(R.id.location_tv)
    protected TextView locationTv;

    @ViewById(R.id.price_tv)
    protected TextView priceTv;

    @ViewById(R.id.list_rv)
    protected RecyclerView listRv;

    @ViewById(R.id.scrollView)
    protected NestedScrollView scrollView;

    @ViewById(R.id.bottom_sheet)
    protected NestedScrollView bottomSheet;

    @ViewById(R.id.faded_layout)
    protected RelativeLayout fadedLayout;

    @AnimationRes(R.anim.fade_in_effect)
    protected Animation fadeIn;

    @AnimationRes(R.anim.fade_out_effect)
    protected Animation fadeOut;

    private Bundle bundle;
    private LatLng latlng = new LatLng(23.4352, 111.54322);
    private FriendThumbListAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bundle = savedInstanceState;
    }

    @AfterViews
    protected void onViewsCreated() {
        initToolbar();
        initBottomSheet();
        initMap();
        initAdapter();

        //TODO : this is dummy
        setDummy();
    }

    private void setDummy() {
        ViewUtils.setTextView(descriptionTv, getString(R.string.dummy_long_text));
        ViewUtils.setTextView(buyNowTv, getString(R.string.buy_now_s, "Rp. 1.000.000"));
        ViewUtils.setTextView(dateTimeTv, "25 August 2017, 11:00 - 16:00");
        ViewUtils.setTextView(locationTv, "Kolla Space - 7 Eleven Sabang, 2nd Floor KH. Agus Salim, Jakarta Pusat");
        ViewUtils.setTextView(priceTv, "Rp.100.000/guest");
        ViewUtils.setTextView(notesTv, "Most Valuable");
        ViewUtils.setTextView(titleTv, getString(R.string.dummy_short_text));
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
        scrollListener = getScrollListener(layoutManager, OFFSET);

        listRv.setNestedScrollingEnabled(false);
        listRv.setClipToPadding(true);
        listRv.setLayoutManager(layoutManager);
        listRv.setItemAnimator(new DefaultItemAnimator());
        listRv.addOnScrollListener(scrollListener);

        listRv.setAdapter(adapter);
    }

    private EndlessRecyclerViewScrollListener getScrollListener(LinearLayoutManager layoutManager, int offset) {
        return new EndlessRecyclerViewScrollListener(layoutManager, offset) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            }
        };
    }

    private void initMap() {
        mapView.onCreate(bundle);
        mapView.onResume();

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

            googleMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title("Marker in Sydney")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12.0f));
        }
    };

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
    public void onClick(Friend friend) {
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

    private void setFadedLayout(boolean isShowing) {
        fadedLayout.startAnimation(isShowing ? fadeIn : fadeOut);
        fadedLayout.setVisibility(isShowing ? View.VISIBLE : View.INVISIBLE);
    }
}
