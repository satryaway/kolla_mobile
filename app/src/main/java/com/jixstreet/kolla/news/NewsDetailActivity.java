package com.jixstreet.kolla.news;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.model.NewsDetail;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DateUtils;
import com.jixstreet.kolla.utility.ImageUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

@EActivity(R.layout.activity_news_detail)
public class NewsDetailActivity extends AppCompatActivity {
    public static int requestCode = ActivityUtils.getRequestCode(NewsDetailActivity.class, "1");

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.appbar)
    protected AppBarLayout appBarLayout;

    @ViewById(R.id.date_tv)
    protected TextView dateTv;

    @ViewById(R.id.title_tv)
    protected TextView titleTv;

    @ViewById(R.id.summary_tv)
    protected TextView summaryTv;

    @ViewById(R.id.description_tv)
    protected TextView descriptionTv;

    @ViewById(R.id.bottom_sheet)
    protected NestedScrollView bottomSheet;

    @ViewById(R.id.faded_layout)
    protected RelativeLayout fadedLayout;

    @AnimationRes(R.anim.fade_in_effect)
    protected Animation fadeIn;

    @AnimationRes(R.anim.fade_out_effect)
    protected Animation fadeOut;

    @ViewById(R.id.news_cover_iv)
    protected ImageView newsCoverIv;

    private BottomSheetBehavior mBottomSheetBehavior;
    private NewsDetail newsDetail;

    @AfterViews
    protected void onViewsCreated() {
        newsDetail = ActivityUtils.getParam(this, NewsDetail.paramKey, NewsDetail.class);
        initToolbar();
        initBottomSheet();

        if (newsDetail != null) {
            setData();
        }else finish();
    }

    private void setData() {
        ViewUtils.setTextView(dateTv, getString(R.string.posted_on_s, DateUtils.getDateTimeStrFromMillis(newsDetail.created_at, "")));
        ViewUtils.setTextView(titleTv, newsDetail.title);
        ViewUtils.setTextView(summaryTv, newsDetail.title);
        ViewUtils.setTextView(descriptionTv, newsDetail.content);
        ImageUtils.loadImage(this, newsDetail.cover_image, newsCoverIv);
    }

    private void initToolbar() {
        ViewUtils.setToolbar(this, toolbar);
        appBarLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    private void initBottomSheet() {
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);
    }

    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            switch (newState) {
                case BottomSheetBehavior.STATE_EXPANDED:
                    setFadedLayout(true);
                    break;
                case BottomSheetBehavior.STATE_COLLAPSED:
                    setFadedLayout(false);
                    break;
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    private void setFadedLayout(boolean isShowing) {
        fadedLayout.startAnimation(isShowing ? fadeIn : fadeOut);
        fadedLayout.setVisibility(isShowing ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Click(R.id.expand_iv)
    protected void expand() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
