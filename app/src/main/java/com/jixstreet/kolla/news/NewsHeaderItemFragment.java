package com.jixstreet.kolla.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 2/22/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_news_header_item)
public class NewsHeaderItemFragment extends Fragment {

    @ViewById(R.id.news_header_item_iv)
    protected ImageView newsHeaderItemIv;

    @ViewById(R.id.news_header_item_title_tv)
    protected TextView newsHeaderTitleTv;

    @ViewById(R.id.news_header_item_date_tv)
    protected TextView newsHeaderDateTv;

    private static final String POSITION = "position";

    public static NewsHeaderItemFragment newInstance(int position) {
        NewsHeaderItemFragment fragment = new NewsHeaderItemFragment_();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);

        return fragment;
    }

    @AfterViews
    void OnViewsCreated() {
        int position = getArguments().getInt(POSITION);
        NewsHeaderItem newsHeaderItem = Seeder.getNewsHeaderList().get(position);
        newsHeaderItemIv.setImageResource(newsHeaderItem.image);
        newsHeaderTitleTv.setText(newsHeaderItem.title);
        newsHeaderDateTv.setText(newsHeaderItem.date);
    }
}
