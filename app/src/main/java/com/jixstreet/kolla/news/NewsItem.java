package com.jixstreet.kolla.news;

import android.support.v7.widget.RecyclerView;

/**
 * Created by satryaway on 2/16/2017.
 * satryaway@gmail.com
 */

public class NewsItem {
    public String title;
    public String date;
    public String imageUrl;

    public NewsItem(String title, String date, String imageUrl) {
        this.title = title;
        this.date = date;
        this.imageUrl = imageUrl;
    }
}
