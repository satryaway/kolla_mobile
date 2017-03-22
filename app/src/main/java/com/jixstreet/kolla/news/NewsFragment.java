package com.jixstreet.kolla.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.tools.EndlessRecyclerViewScrollListener;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 2/16/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_news)
public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int OFFSET = 10;
    @ViewById(R.id.news_rv)
    protected RecyclerView newsRv;

    @ViewById(R.id.refresh_wrapper)
    protected SwipeRefreshLayout refreshWrapper;

    private NewsJson newsJson;
    private NewsListAdapter newsListAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    public static NewsFragment newInstance() {
        return new NewsFragment_();
    }

    @AfterViews
    public void onViewsCreated() {
        newsJson = new NewsJson(getActivity());
        initAdapter();
    }

    private void initAdapter() {
        newsListAdapter = new NewsListAdapter(getActivity());
        LinearLayoutManager layoutManager = ViewUtils.getLayoutManager(getActivity(), true);
        scrollListener = getScrollListener(layoutManager, OFFSET);
        refreshWrapper.setOnRefreshListener(this);

        ViewUtils.setRecyclerViewDivider(newsRv, layoutManager);
        newsRv.setNestedScrollingEnabled(false);
        newsRv.setClipToPadding(true);
        newsRv.setLayoutManager(layoutManager);
        newsRv.setItemAnimator(new DefaultItemAnimator());
        newsRv.addOnScrollListener(scrollListener);

        newsRv.setAdapter(newsListAdapter);
    }

    private EndlessRecyclerViewScrollListener getScrollListener(LinearLayoutManager layoutManager, int offset) {
        return new EndlessRecyclerViewScrollListener(layoutManager, offset) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getNews(page);
            }
        };
    }

    private void getNews(int page) {
        NewsJson.Request request = new NewsJson.Request();
        request.page = String.valueOf(page);
        newsJson.get(request, new OnGetNews() {
            @Override
            public void onSucceed(NewsJson.Response response) {
                newsListAdapter.addNews(response.data.data);
                refreshWrapper.setRefreshing(false);
            }

            @Override
            public void onFailure(String message) {
                refreshWrapper.setRefreshing(false);
                DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(),
                        getActivity().getWindow().getDecorView(), message);
            }
        });
    }

    @Override
    public void onRefresh() {
        scrollListener.resetState();
        newsListAdapter.clearNews();
        scrollListener.initScroll(newsRv);
    }
}
