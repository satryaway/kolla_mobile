package com.jixstreet.kolla.news;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 2/22/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.view_news_header)
public class NewsHeaderView extends RelativeLayout {

    @ViewById(R.id.news_header_vp)
    protected ViewPager newsHeaderVp;

    @ViewById(R.id.tabsLayout)
    protected TabLayout tabs;

    private Context context;

    public NewsHeaderView(Context context) {
        super(context);
        this.context = context;
    }

    public void setView() {
        NewsHeaderPagerAdapter newsHeaderPagerAdapter = new NewsHeaderPagerAdapter(context,
                ((AppCompatActivity)context).getSupportFragmentManager());
        newsHeaderVp.setAdapter(newsHeaderPagerAdapter);
        tabs.setupWithViewPager(newsHeaderVp);
    }

    public class NewsHeaderPagerAdapter extends FragmentPagerAdapter {
        private final Context context;

        public NewsHeaderPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return NewsHeaderItemFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return Seeder.getNewsHeaderList().size();
        }
    }
}
