package com.jixstreet.kolla.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.tools.EndlessRecyclerViewScrollListener;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 5/17/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_event)
public class EventFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        EventView.OnEventSelectedListener {

    private static final int OFFSET = 10;
    private static final int STARTING_PAGE_INDEX = 1;
    @ViewById(R.id.item_found_tv)
    protected TextView itemFoundTv;

    @ViewById(R.id.list_rv)
    protected RecyclerView listRv;

    @ViewById(R.id.refresh_wrapper)
    protected SwipeRefreshLayout refreshWrapper;

    private EndlessRecyclerViewScrollListener scrollListener;
    private EventListAdapter eventListAdapter;
    private EventListJson eventListJson;

    public static EventFragment newInstance() {
        Bundle args = new Bundle();
        EventFragment fragment = new EventFragment_();
        fragment.setArguments(args);
        return fragment;
    }

    @AfterViews
    protected void onViewsCreated() {
        eventListJson = new EventListJson(getContext());
        initAdapter();
        getData(STARTING_PAGE_INDEX);
    }

    private void initAdapter() {
        eventListAdapter = new EventListAdapter(getActivity());
        eventListAdapter.setOnEventSelectedListener(this);
        LinearLayoutManager layoutManager = ViewUtils.getLayoutManager(getActivity(), true);
        scrollListener = getScrollListener(layoutManager, OFFSET);
        refreshWrapper.setOnRefreshListener(this);

        listRv.setNestedScrollingEnabled(false);
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
        EventListJson.Request request = new EventListJson.Request();
        request.page = String.valueOf(page);
        eventListJson.get(request, new OnGetEventList() {
            @Override
            public void onSuccess(EventListJson.Response response) {
                refreshWrapper.setRefreshing(false);
                eventListAdapter.addItems(response.data.data);
                ViewUtils.setTextView(itemFoundTv, getString(R.string.event_found_s,
                        String.valueOf(eventListAdapter.getItemCount())));
            }

            @Override
            public void onFailure(String message) {
                refreshWrapper.setRefreshing(false);
                DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(), message);
            }
        });
    }

    @Override
    public void onRefresh() {
        scrollListener.resetState();
        eventListAdapter.clearList();
        scrollListener.initScroll(listRv);
    }

    @Override
    public void onClick(Event event) {
        ActivityUtils.startActivityWParamAndWait(getActivity(), EventDetailActivity_.class,
                Event.paramKey, event, EventDetailActivity.requestCode);
    }
}
