package com.jixstreet.kolla.profile.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.event.Event;
import com.jixstreet.kolla.event.EventDetailActivity;
import com.jixstreet.kolla.event.EventDetailActivity_;
import com.jixstreet.kolla.event.EventListAdapter;
import com.jixstreet.kolla.event.EventView;
import com.jixstreet.kolla.model.BookedEvent;
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

@EFragment(R.layout.fragment_booked_event)
public class BookedEventFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        EventView.OnEventSelectedListener {

    private static final int OFFSET = 10;
    private static final int STARTING_PAGE_INDEX = 1;
    private static final String EVENT = "event";

    @ViewById(R.id.list_rv)
    protected RecyclerView listRv;

    @ViewById(R.id.refresh_wrapper)
    protected SwipeRefreshLayout refreshWrapper;

    private EndlessRecyclerViewScrollListener scrollListener;
    private EventListAdapter eventListAdapter;
    private BookedEventJson bookedEventJson;
    private OnGetBookedEventListener onGetBookedEventListener;

    public static BookedEventFragment newInstance(UserData userData) {
        Bundle args = new Bundle();
        args.putString(EVENT, CastUtils.toString(userData));
        BookedEventFragment fragment = new BookedEventFragment_();
        fragment.setArguments(args);
        return fragment;
    }

    @AfterViews
    protected void onViewsCreated() {
        UserData userData = CastUtils.fromString(getArguments().getString(EVENT), UserData.class);
        if (userData != null) {
            bookedEventJson = new BookedEventJson(getContext(), userData.id);
            initAdapter();
            getData(STARTING_PAGE_INDEX);
        }
    }

    private void initAdapter() {
        eventListAdapter = new EventListAdapter(getActivity());
        eventListAdapter.setOnEventSelectedListener(this);
        LinearLayoutManager layoutManager = ViewUtils.getLayoutManager(getActivity(), true);
        scrollListener = getScrollListener(layoutManager, OFFSET);
        refreshWrapper.setOnRefreshListener(this);

        listRv.setClipToPadding(true);
        listRv.setLayoutManager(layoutManager);
        listRv.setItemAnimator(new DefaultItemAnimator());
        listRv.addOnScrollListener(scrollListener);

        listRv.setAdapter(eventListAdapter);
    }

    private EndlessRecyclerViewScrollListener getScrollListener(LinearLayoutManager layoutManager, int offset) {
        return new EndlessRecyclerViewScrollListener(layoutManager, offset, STARTING_PAGE_INDEX) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getData(page);
            }
        };
    }

    private void getData(int page) {
        BookedEventJson.Request request = new BookedEventJson.Request();
        request.page = String.valueOf(page);
        bookedEventJson.get(request, new OnGetBookedEvent() {
            @Override
            public void onSuccess(BookedEventJson.Response response) {
                onGetBookedEventListener.onGetBookedEvent(response.data.total);
                refreshWrapper.setRefreshing(false);
                eventListAdapter.addItems(getEvents(response.data.data));
            }

            @Override
            public void onFailure(String message) {
                if (refreshWrapper != null && getActivity() != null) {
                    refreshWrapper.setRefreshing(false);
                    DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(), message);
                }
            }
        });
    }

    private List<Event> getEvents(List<BookedEvent> data) {
        List<Event> eventList = new ArrayList<>();
        for (BookedEvent bookedEvent : data) {
            eventList.add(bookedEvent.event);
        }

        return eventList;
    }

    @Override
    public void onRefresh() {
        scrollListener.resetStateWithParams(STARTING_PAGE_INDEX);
        eventListAdapter.clearList();
        scrollListener.initScroll(listRv);
        getData(STARTING_PAGE_INDEX);
    }

    @Override
    public void onClick(Event event) {
        event.isActive = false;
        ActivityUtils.startActivityWParamAndWait(getActivity(), EventDetailActivity_.class,
                Event.paramKey, event, EventDetailActivity.requestCode);
    }

    public interface OnGetBookedEventListener {
        void onGetBookedEvent(String total);
    }

    public void setOnGetBookedEventListener(OnGetBookedEventListener onGetBookedEventListener) {
        this.onGetBookedEventListener = onGetBookedEventListener;
    }
}
