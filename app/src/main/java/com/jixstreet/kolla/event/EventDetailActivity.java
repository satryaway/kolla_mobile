package com.jixstreet.kolla.event;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
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
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_event_detail)
public class EventDetailActivity extends AppCompatActivity implements FriendThumbView.OnThumbClickListener {
    private static final int OFFSET = 10;
    private int swipeCount = 0;

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.appbar)
    protected AppBarLayout appBarLayout;

    @ViewById(R.id.content_wrapper)
    protected SwipeLayout swipeLayout;

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

    private Bundle bundle;
    private LatLng latlng = new LatLng(23.4352, 111.54322);
    private FriendThumbListAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bundle = savedInstanceState;
    }

    @AfterViews
    protected void onViewsCreated() {
        initToolbar();
        initSwipeLayout();
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

    private void initSwipeLayout() {
        swipeLayout.addDrag(SwipeLayout.DragEdge.Bottom, findViewById(R.id.bottom_wrapper));
        swipeLayout.setRightSwipeEnabled(false);
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

    @Click(R.id.show_upper_iv)
    protected void showUpperInfo() {
        swipeLayout.close();
    }
}
