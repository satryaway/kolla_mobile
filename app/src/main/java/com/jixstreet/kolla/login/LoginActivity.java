package com.jixstreet.kolla.login;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jixstreet.kolla.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    @ViewById(R.id.background_vp)
    protected ViewPager backgroundVp;

    @ViewById(R.id.tabs)
    protected TabLayout tabLayout;

    private LoginPagerAdapter loginPagerAdapter;

    @AfterViews
    void onViewsCreated() {
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
        loginPagerAdapter = new LoginPagerAdapter(this, getSupportFragmentManager());
        backgroundVp.setAdapter(loginPagerAdapter);
        tabLayout.setupWithViewPager(backgroundVp);
    }
}
