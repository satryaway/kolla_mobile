package com.jixstreet.kolla.news;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.model.NewsDetail;
import com.jixstreet.kolla.utility.DateUtils;
import com.jixstreet.kolla.utility.ImageUtils;

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

    public NewsDetail newsDetail;

    public NewsItemView(Context context) {
        super(context);
    }

    public void setNewsDetail(NewsDetail newsDetail) {
        this.newsDetail = newsDetail;
        setValue();
    }

    private void setValue() {
        newsTitleTv.setText(newsDetail.title);
        newsDateTv.setText(DateUtils.getDateTimeStr(Long.valueOf(newsDetail.created_at), ""));
        ImageUtils.loadImage(getContext(), newsDetail.cover_image, newsImageIv);
    }
}
