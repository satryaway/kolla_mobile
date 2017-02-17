package com.jixstreet.kolla.login;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_intro)
public class IntroActivity extends AppCompatActivity {

    @ViewById(R.id.background_vp)
    protected ViewPager backgroundVp;

    @ViewById(R.id.tabs)
    protected TabLayout tabLayout;

    @ViewById(R.id.login_wrapper)
    protected ViewGroup loginWrapper;

    private IntroPagerAdapter loginPagerAdapter;
    private Animation fadeInAnimation;
    private Animation fadeOutAnimation;

    @AfterViews
    void onViewsCreated() {
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_effect);
        fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out_effect);

        modifyStatusBar();
        initPager();
    }

    private void modifyStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initPager() {
        loginPagerAdapter = new IntroPagerAdapter(this, getSupportFragmentManager());
        backgroundVp.setAdapter(loginPagerAdapter);
        tabLayout.setupWithViewPager(backgroundVp);
    }

    private void changeLoginPageVisibility() {
        if (loginWrapper.getVisibility() == View.VISIBLE) {
            ViewUtils.hideSoftKeyboard(this);
            loginWrapper.startAnimation(fadeOutAnimation);
            loginWrapper.setVisibility(View.INVISIBLE);
        } else {
            loginWrapper.startAnimation(fadeInAnimation);
            loginWrapper.setVisibility(View.VISIBLE);
        }
    }

    @Click(R.id.login_wrapper)
    void doNothing() {
    }

    @Click(R.id.show_login_page_tv)
    void openLoginPage() {
        changeLoginPageVisibility();
    }

    @Click(R.id.close_login_page_iv)
    void closeLoginPage() {
        changeLoginPageVisibility();
    }
}
