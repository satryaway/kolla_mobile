package com.jixstreet.kolla.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.login.LoginJson;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;

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

    private NewsJson newsJson;
    private NewsListAdapter newsListAdapter;

    public static NewsFragment newInstance() {
        return new NewsFragment_();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newsJson = new NewsJson(getActivity());
        initAdapter();
        getNews();
    }

    private void initAdapter() {
        newsListAdapter = new NewsListAdapter(getActivity());
        LinearLayoutManager layoutManager = ViewUtils.getLayoutManager(getActivity(), true);
        ViewUtils.setRecyclerViewDivider(newsRv, layoutManager);
        newsRv.setNestedScrollingEnabled(true);
        newsRv.setClipToPadding(true);
        newsRv.setLayoutManager(layoutManager);
        newsRv.setItemAnimator(new DefaultItemAnimator());
        newsRv.setAdapter(newsListAdapter);
    }

    private void getNews() {
        NewsJson.Request request = new NewsJson.Request();
        request.page = "1";
        newsJson.get(request, new OnGetNews() {
            @Override
            public void onSucceed(NewsJson.Response response) {
                LoginJson.Response.saveAccessToken(getContext(), response.access_token);
                newsListAdapter.setNews(response.data.data);
            }

            @Override
            public void onFailure(String message) {
                DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(),
                        getActivity().getWindow().getDecorView(), message);
            }
        });
    }
}
