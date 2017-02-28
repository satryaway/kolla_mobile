package com.jixstreet.kolla.intro;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.login.LoginView;
import com.jixstreet.kolla.register.RegisterView;
import com.jixstreet.kolla.utility.TextUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_intro)
public class IntroActivity extends AppCompatActivity implements IntroView.OnRegisterHereClicked,
        IntroView.OnLoginHereClicked {

    @ViewById(R.id.background_vp)
    protected ViewPager backgroundVp;

    @ViewById(R.id.tabs)
    protected TabLayout tabLayout;

    @ViewById(R.id.login_view)
    protected LoginView loginView;

    @ViewById(R.id.register_view)
    protected RegisterView registerView;

    @AfterViews
    void onViewsCreated() {
        modifyStatusBar();
        initUI();
        initPager();
    }

    private void initUI() {
        loginView.setOnRegisterHereClicked(this);
        registerView.setOnLoginHereClicked(this);
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
        IntroPagerAdapter loginPagerAdapter = new IntroPagerAdapter(this, getSupportFragmentManager());
        backgroundVp.setAdapter(loginPagerAdapter);
        tabLayout.setupWithViewPager(backgroundVp);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (loginView.getVisibility() == View.VISIBLE) {
            loginView.changeVisibilityState();
            return;
        }

        if (registerView.getVisibility() == View.VISIBLE) {
            registerView.changeVisibilityState();
            return;
        }

        super.onBackPressed();
    }

    @Click(R.id.login_wrapper)
    void doNothing() {
    }

    @Click(R.id.register_wrapper)
    void doNotDoAnything() {
    }

    @Click(R.id.show_login_page_tv)
    void openLoginPage() {
        loginView.changeVisibilityState();
    }

    @Click(R.id.show_register_page_tv)
    void openRegisterPasge() {
        registerView.changeVisibilityState();
    }

    @Override
    public void onRegister() {
        registerView.changeVisibilityState();
    }

    @Override
    public void onLogin() {
        loginView.changeVisibilityState();
    }
}
