package com.jixstreet.kolla.booking.room;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.Booking;
import com.jixstreet.kolla.booking.BookingOptionActivity;
import com.jixstreet.kolla.booking.BookingOptionActivity_;
import com.jixstreet.kolla.booking.category.BookingCategory;
import com.jixstreet.kolla.booking.room.detail.RoomDetailActivity;
import com.jixstreet.kolla.booking.room.detail.RoomDetailActivity_;
import com.jixstreet.kolla.tools.EndlessRecyclerViewScrollListener;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.content_room_list)
public class RoomListActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener, OnRoomSelected {
    private static final int OFFSET = 10;
    public static int requestCode = ActivityUtils.getRequestCode(RoomListActivity.class, "1");

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.toolbar_title_tv)
    protected TextView toolbarTitleTv;

    @ViewById(R.id.room_rv)
    protected RecyclerView roomRv;

    @ViewById(R.id.refresh_wrapper)
    protected SwipeRefreshLayout refreshWrapper;

    private RoomListAdapter roomListAdapter;
    private RoomJson roomJson;
    private BookingCategory bookingCategory;
    private EndlessRecyclerViewScrollListener scrollListener;
    private Booking booking;

    @AfterViews
    void onViewsCreated() {
        bookingCategory = ActivityUtils.getParam(this, BookingCategory.paramKey, BookingCategory.class);
        roomJson = new RoomJson(this);

        if (booking == null) booking = new Booking();
        if (booking.room == null) booking.room = new Room();
        if (booking.roomRequest == null) booking.roomRequest = new RoomJson.Request();
        booking.room.category = bookingCategory;

        getBookingOption();
        ViewUtils.setToolbar(this, toolbar);
        initAdapter();
    }

    private void initAdapter() {
        roomListAdapter = new RoomListAdapter(this);
        roomListAdapter.setOnRoomSelected(this);
        LinearLayoutManager layoutManager = ViewUtils.getLayoutManager(this, true);
        scrollListener = getScrollListener(layoutManager, OFFSET);
        refreshWrapper.setOnRefreshListener(this);

        roomRv.setNestedScrollingEnabled(false);
        roomRv.setClipToPadding(true);
        roomRv.setLayoutManager(layoutManager);
        roomRv.setItemAnimator(new DefaultItemAnimator());
        roomRv.addOnScrollListener(scrollListener);

        roomRv.setAdapter(roomListAdapter);
    }

    private EndlessRecyclerViewScrollListener getScrollListener(LinearLayoutManager layoutManager, int offset) {
        return new EndlessRecyclerViewScrollListener(layoutManager, offset) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getRooms(page);
            }
        };
    }

    private void setValue(List<Room> roomList) {
        if (roomList != null && bookingCategory != null) {
            roomListAdapter.addList(roomList, bookingCategory);
            toolbarTitleTv.setText(getString(R.string.locations_available, roomListAdapter.getListSize()));
        }
    }

    private void getRooms(int page) {
        if (booking.roomRequest != null && roomJson != null && bookingCategory != null) {
            booking.roomRequest.page = String.valueOf(page);
            booking.roomRequest.category = bookingCategory.id;
            roomJson.getRooms(booking.roomRequest, new OnGetRoom() {
                @Override
                public void onSuccess(RoomJson.Response response) {
                    refreshWrapper.setRefreshing(false);
                    setValue(response.data.data);
                }

                @Override
                public void onFailure(String message) {
                    if (refreshWrapper != null)
                        refreshWrapper.setRefreshing(false);

                    DialogUtils.makeSnackBar(CommonConstant.failed, RoomListActivity.this,
                            getWindow().getDecorView(), message);
                }
            });
        }
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
            if (requestCode == BookingOptionActivity.requestCode) {
                booking.roomRequest = ActivityUtils.getResult(data, BookingOptionActivity.resultKey, RoomJson.Request.class);
                if (booking.roomRequest != null) {
                    onRefresh();
                }
            } else if (requestCode == RoomDetailActivity.requestCode) {
                setResult(RESULT_OK);
                finish();
            }
        } else {
            finish();
        }
    }

    @Click(R.id.setting_iv)
    void getBookingOption() {
        ActivityUtils.startActivityWParamAndWait(this, BookingOptionActivity_.class,
                Booking.paramKey, booking, BookingOptionActivity.requestCode);
    }

    @Override
    public void onRefresh() {
        scrollListener.resetState();
        roomListAdapter.clearList();
        scrollListener.initScroll(roomRv);
    }

    @Override
    public void onSelect(Room room) {
        if (booking == null) return;
        booking.room = room;
        booking.room.category = bookingCategory;
        ActivityUtils.startActivityWParamAndWait(this, RoomDetailActivity_.class,
                Booking.paramKey, booking, RoomDetailActivity.requestCode);
    }
}
