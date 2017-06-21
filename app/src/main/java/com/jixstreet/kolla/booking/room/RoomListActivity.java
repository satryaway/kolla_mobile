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
import com.jixstreet.kolla.booking.category.BookingEntity;
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
    //    private BookingCategory bookingCategory;
    private EndlessRecyclerViewScrollListener scrollListener;
    private Booking booking;

    @AfterViews
    void onViewsCreated() {
        booking = ActivityUtils.getParam(this, Booking.paramKey, Booking.class);
        if (booking == null) {
            finish();
            return;
        }

        roomJson = new RoomJson(this);
        //bookingCategory = ActivityUtils.getParam(this, BookingCategory.paramKey, BookingCategory.class);

        /*if (booking == null) booking = new Booking();
        if (booking.room == null) booking.room = new Room();
        if (booking.bookingRequest == null) booking.bookingRequest = new BookingJson.Request();
        if (booking.roomRequest == null) booking.roomRequest = new RoomJson.Request();
        booking.room.category = bookingCategory;*/

//        getBookingOption();
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

        if (booking.bookingCategory.id.equals(BookingEntity.COMMON_SPACE)
                || booking.bookingCategory.id.equals(BookingEntity.MEETING_ROOM))
            roomListAdapter.setAvailableSeatCount(booking.roomRequest.guest);

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
        if (roomList != null && booking.roomRequest != null) {
            roomListAdapter.addList(roomList, booking.roomRequest.bookingCategory);
            toolbarTitleTv.setText(getString(R.string.locations_available, roomListAdapter.getListSize()));
        }
    }

    private void getRooms(int page) {
        if (roomJson != null && booking != null) {
            booking.roomRequest.page = String.valueOf(page);
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

                    DialogUtils.makeSnackBar(CommonConstant.failed, RoomListActivity.this, message);
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
            if (requestCode == RoomDetailActivity.requestCode) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Click(R.id.setting_iv)
    void getBookingOption() {
        finish();
    }

    @Override
    public void onRefresh() {
        scrollListener.resetState();
        roomListAdapter.clearList();
        scrollListener.initScroll(roomRv);
    }

    @Override
    public void onSelect(Room room) {
        booking.room = room;
        ActivityUtils.startActivityWParamAndWait(this, RoomDetailActivity_.class,
                Booking.paramKey, booking, RoomDetailActivity.requestCode);
    }
}
