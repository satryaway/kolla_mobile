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
import com.jixstreet.kolla.booking.BookingDetailActivity;
import com.jixstreet.kolla.booking.BookingDetailActivity_;
import com.jixstreet.kolla.booking.category.BookingCategory;
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
public class RoomListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final int OFFSET = 10;

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.toolbar_title_tv)
    protected TextView toolbarTitleTv;

    @ViewById(R.id.room_rv)
    protected RecyclerView roomRv;

    @ViewById(R.id.refresh_wrapper)
    protected SwipeRefreshLayout refreshWrapper;

    private RoomJson.Request roomParams;

    private RoomListAdapter roomListAdapter;
    private RoomJson roomJson;
    private BookingCategory bookingCategory;
    private EndlessRecyclerViewScrollListener scrollListener;

    @AfterViews
    void onViewsCreated() {
        getBookingDetail();

        roomJson = new RoomJson(this);
        bookingCategory = ActivityUtils.getParam(this, BookingCategory.paramKey, BookingCategory.class);
        ViewUtils.setToolbar(this, toolbar);
        initAdapter();
    }

    private void initAdapter() {
        roomListAdapter = new RoomListAdapter(this);
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
        if (roomParams != null && roomJson != null && bookingCategory != null) {
            roomParams.category = bookingCategory.id;
            roomParams.page = String.valueOf(page);
            roomJson.getRooms(roomParams, new OnGetRoom() {
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
            if (requestCode == BookingDetailActivity.requestCode) {
                roomParams = ActivityUtils.getResult(data, BookingDetailActivity.resultKey, RoomJson.Request.class);
                if (roomParams != null) {
                    onRefresh();
                }
            }
        } else {
            finish();
        }
    }

    @Click(R.id.setting_iv)
    void getBookingDetail() {
        ActivityUtils.startActivityWParamAndWait(this, BookingDetailActivity_.class,
                RoomJson.paramKey, roomParams, BookingDetailActivity.requestCode);
    }

    @Override
    public void onRefresh() {
        scrollListener.resetState();
        roomListAdapter.clearList();
        scrollListener.initScroll(roomRv);
    }
}
