package com.jixstreet.kolla.booking.room;

import android.content.Intent;
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
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.content_room_list)
public class RoomListActivity extends AppCompatActivity {
    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.toolbar_title_tv)
    protected TextView toolbarTitleTv;

    @ViewById(R.id.room_rv)
    protected RecyclerView roomRv;

    private RoomJson.Request roomParams;

    private RoomListAdapter roomListAdapter;
    private RoomJson roomJson;
    private BookingCategory bookingCategory;

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
        roomRv.setNestedScrollingEnabled(false);
        roomRv.setClipToPadding(true);
        roomRv.setLayoutManager(layoutManager);
        roomRv.setItemAnimator(new DefaultItemAnimator());
        roomRv.setAdapter(roomListAdapter);
    }

    private void setValue(List<Room> roomList) {
        if (roomList != null && bookingCategory != null) {
            roomListAdapter.setRooms(roomList, bookingCategory);
            toolbarTitleTv.setText(getString(R.string.locations_available, roomList.size()));
        }
    }

    private void getRooms() {
        if (roomParams != null && roomJson != null && bookingCategory != null) {
            roomParams.category = bookingCategory.id;
            roomJson.getRooms(roomParams, new OnGetRoom() {
                @Override
                public void onSuccess(RoomJson.Response response) {
                    setValue(response.data.data);
                }

                @Override
                public void onFailure(String message) {
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
                    getRooms();
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
}
