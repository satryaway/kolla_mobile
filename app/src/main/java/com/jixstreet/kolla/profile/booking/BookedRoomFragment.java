package com.jixstreet.kolla.profile.booking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.Booking;
import com.jixstreet.kolla.booking.room.BookedRoomListAdapter;
import com.jixstreet.kolla.booking.room.OnRoomSelected;
import com.jixstreet.kolla.booking.room.Room;
import com.jixstreet.kolla.booking.room.detail.RoomDetailActivity;
import com.jixstreet.kolla.booking.room.detail.RoomDetailActivity_;
import com.jixstreet.kolla.model.BookedRoom;
import com.jixstreet.kolla.model.UserData;
import com.jixstreet.kolla.tools.EndlessRecyclerViewScrollListener;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.CastUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 6/21/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_booked_room)
public class BookedRoomFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        OnRoomSelected {

    private static final int OFFSET = 10;
    private static final int STARTING_PAGE_INDEX = 1;
    private static final String ROOM = "room";

    @ViewById(R.id.list_rv)
    protected RecyclerView listRv;

    @ViewById(R.id.refresh_wrapper)
    protected SwipeRefreshLayout refreshWrapper;

    private EndlessRecyclerViewScrollListener scrollListener;
    private BookedRoomListAdapter roomListAdapter;
    private BookedRoomJson bookedRoomJson;
    private OnGetBookedRoomDoneListener onGetBookedRoomDoneListener;

    public static BookedRoomFragment newInstance(UserData userData) {
        Bundle args = new Bundle();
        args.putString(ROOM, CastUtils.toString(userData));
        BookedRoomFragment fragment = new BookedRoomFragment_();
        fragment.setArguments(args);
        return fragment;
    }

    @AfterViews
    protected void onViewsCreated() {
        UserData userData = CastUtils.fromString(getArguments().getString(ROOM), UserData.class);
        if (userData != null) {
            bookedRoomJson = new BookedRoomJson(getActivity(), userData.id);
            initAdapter();
            getData(STARTING_PAGE_INDEX);
        }
    }

    private void initAdapter() {
        roomListAdapter = new BookedRoomListAdapter(getActivity());
        roomListAdapter.setOnRoomSelected(this);
        LinearLayoutManager layoutManager = ViewUtils.getLayoutManager(getActivity(), true);
        scrollListener = getScrollListener(layoutManager, OFFSET);
        refreshWrapper.setOnRefreshListener(this);

        listRv.setClipToPadding(true);
        listRv.setLayoutManager(layoutManager);
        listRv.setItemAnimator(new DefaultItemAnimator());
        listRv.addOnScrollListener(scrollListener);

        listRv.setAdapter(roomListAdapter);
    }

    private EndlessRecyclerViewScrollListener getScrollListener(LinearLayoutManager layoutManager, int offset) {
        return new EndlessRecyclerViewScrollListener(layoutManager, offset, STARTING_PAGE_INDEX) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getData(page);
            }
        };
    }

    private void getData(final int page) {
        BookedRoomJson.Request request = new BookedRoomJson.Request();
        request.page = String.valueOf(page);
        bookedRoomJson.get(request, new OnGetBookedRooms() {
            @Override
            public void onSuccess(BookedRoomJson.Response response) {
                if(page == 1) {
                    onGetBookedRoomDoneListener.onGetBookedRoom(response.data.total);
                }
                refreshWrapper.setRefreshing(false);
                setValue(getRooms(response.data.data));
            }

            @Override
            public void onFailure(String message) {
                if (refreshWrapper != null)
                    refreshWrapper.setRefreshing(false);

                DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(), message);
            }
        });
    }

    private List<Room> getRooms(List<BookedRoom> data) {
        List<Room> roomList = new ArrayList<>();
        for (BookedRoom bookedRoom : data) {
            roomList.add(bookedRoom.room);
        }

        return roomList;
    }

    private void setValue(List<Room> roomList) {
        roomListAdapter.addList(roomList);
    }

    @Override
    public void onRefresh() {
        scrollListener.resetStateWithParams(STARTING_PAGE_INDEX);
        roomListAdapter.clearList();
        scrollListener.initScroll(listRv);
        getData(STARTING_PAGE_INDEX);
    }

    @Override
    public void onSelect(Room room) {
        Booking booking = new Booking();
        room.isOnlyView = true;
        booking.room = room;

        ActivityUtils.startActivityWParamAndWait(this, RoomDetailActivity_.class,
                Booking.paramKey, booking, RoomDetailActivity.requestCode);
    }

    public interface OnGetBookedRoomDoneListener {
        void onGetBookedRoom(String total);
    }

    public void setOnGetBookedRoomDoneListener(OnGetBookedRoomDoneListener onGetBookedRoomDoneListener) {
        this.onGetBookedRoomDoneListener = onGetBookedRoomDoneListener;
    }
}
