package com.jixstreet.kolla.news;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 2/16/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_news)
public class NewsFragment extends Fragment {

    @ViewById(R.id.news_rv)
    protected RecyclerView newsRv;

    public static NewsFragment newInstance() {
        return new NewsFragment_();
    }

    @AfterViews
    void onViewsCreated() {
        NewsListAdapter newsListAdapter = new NewsListAdapter();
        LinearLayoutManager layoutManager = ViewUtils.getLayoutManager(getActivity(), true);
        newsRv.setNestedScrollingEnabled(true);
        newsRv.setClipToPadding(true);
        newsRv.setLayoutManager(layoutManager);
        newsRv.setItemAnimator(new DefaultItemAnimator());
        newsRv.setAdapter(newsListAdapter);
    }
}
