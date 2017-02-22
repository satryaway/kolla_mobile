package com.jixstreet.kolla.login;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.model.LoginJson;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.TextUtils;
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

    @ViewById(R.id.register_wrapper)
    protected ViewGroup registerWrapper;

    @ViewById(R.id.forgot_password_tv)
    protected TextView forgotPasswordTv;

    @ViewById(R.id.email_et)
    protected EditText emailEt;

    @ViewById(R.id.password_et)
    protected EditText passwordEt;

    private Animation fadeInAnimation;
    private Animation fadeOutAnimation;
    private LoginJson loginJson;

    @AfterViews
    void onViewsCreated() {
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_effect);
        fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out_effect);

        initUI();
        modifyStatusBar();
        initPager();
    }

    private void initUI() {
        loginJson = new LoginJson(this);
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

    private void changeRegisterPageVisibility() {
        if (registerWrapper.getVisibility() == View.VISIBLE) {
            ViewUtils.hideSoftKeyboard(this);
            registerWrapper.startAnimation(fadeOutAnimation);
            registerWrapper.setVisibility(View.INVISIBLE);
        } else {
            registerWrapper.startAnimation(fadeInAnimation);
            registerWrapper.setVisibility(View.VISIBLE);
        }
    }

    private boolean isValidated() {
        int count = 0;
        if (emailEt.getText().toString().isEmpty())
            emailEt.setError(getString(R.string.field_required));
        else {
            if (!TextUtils.isEmailValid(emailEt.getText().toString()))
                emailEt.setError(getString(R.string.email_not_valid));
            else count++;
        }

        if (passwordEt.getText().toString().isEmpty())
            passwordEt.setError(getString(R.string.field_required));
        else count++;

        return count == 2;
    }

    private LoginJson.OnLogin onLogin = new LoginJson.OnLogin() {
        @Override
        public void onSuccess(LoginJson.Response response) {
            DialogUtils.makeSnackBar(CommonConstant.success, IntroActivity.this,
                    getWindow().getDecorView(), response.message);
        }

        @Override
        public void onFailure(String text) {
            DialogUtils.makeSnackBar(CommonConstant.failed, IntroActivity.this,
                    getWindow().getDecorView(), text);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        loginJson.cancel();
    }

    @Override
    public void onBackPressed() {
        if (loginWrapper.getVisibility() == View.VISIBLE) {
            changeLoginPageVisibility();
            return;
        }

        if (registerWrapper.getVisibility() == View.VISIBLE) {
            changeRegisterPageVisibility();
            return;
        }

        super.onBackPressed();
    }

    @Click(R.id.login_tv)
    void doLogin() {
        ViewUtils.hideSoftKeyboard(this);
        if (isValidated()) {
            LoginJson.Request request = new LoginJson.Request();
            request.email = emailEt.getText().toString();
            request.password = passwordEt.getText().toString();

            loginJson.post(request, onLogin);
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

    @Click(R.id.show_register_page_tv)
    void openRegisterPasge() {
        changeRegisterPageVisibility();
    }

    @Click(R.id.close_register_page_iv)
    void closeRegisterPage() {
        changeRegisterPageVisibility();
    }

    @Click(R.id.register_here_tv)
    void showRegisterPage() {
        changeLoginPageVisibility();
        changeRegisterPageVisibility();
    }

    @Click(R.id.login_here_tv)
    void showLoginPage() {
        changeLoginPageVisibility();
        changeRegisterPageVisibility();
    }
}
