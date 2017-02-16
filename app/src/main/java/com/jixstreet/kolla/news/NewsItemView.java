package com.jixstreet.kolla.news;

import android.content.Context;
import android.widget.LinearLayout;

import com.jixstreet.kolla.R;

import org.androidannotations.annotations.EViewGroup;

/**
 * Created by satryaway on 2/16/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.item_news)
public class NewsItemView extends LinearLayout {
    public NewsItemView(Context context) {
        super(context);
    }
}
