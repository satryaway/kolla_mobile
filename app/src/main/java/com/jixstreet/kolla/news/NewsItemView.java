package com.jixstreet.kolla.news;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 2/16/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.item_news)
public class NewsItemView extends LinearLayout {
    @ViewById(R.id.news_image_iv)
    protected ImageView newsImageIv;

    @ViewById(R.id.news_title_tv)
    protected TextView newsTitleTv;

    @ViewById(R.id.news_date_tv)
    protected TextView newsDateTv;

    public NewsItemView(Context context) {
        super(context);
    }
}
