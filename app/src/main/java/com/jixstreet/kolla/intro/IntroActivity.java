package com.jixstreet.kolla.intro;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.login.LoginView;
import com.jixstreet.kolla.model.RegisterJson;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.TextUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_intro)
public class IntroActivity extends AppCompatActivity {

    @ViewById(R.id.activity_login)
    protected RelativeLayout activityLogin;

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

    @ViewById(R.id.name_register_et)
    protected EditText nameRegisterEt;

    @ViewById(R.id.email_register_et)
    protected EditText emailRegisterEt;

    @ViewById(R.id.password_register_et)
    protected EditText passwordRegisterEt;

    @ViewById(R.id.password_confirmation_register_et)
    protected EditText passwordConfirmationRegisterEt;

    @ViewById(R.id.login_view)
    protected LoginView loginView;

    private Animation fadeInAnimation;
    private Animation fadeOutAnimation;
    private RegisterJson registerJson;

    @AfterViews
    void onViewsCreated() {
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_effect);
        fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out_effect);

        initUI();
        modifyStatusBar();
        initPager();
    }

    private void initUI() {
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

    /*private void changeLoginPageVisibility() {
        if (loginWrapper.getVisibility() == View.VISIBLE) {
            ViewUtils.hideSoftKeyboard(this);
            loginWrapper.startAnimation(fadeOutAnimation);
            loginWrapper.setVisibility(View.INVISIBLE);
        } else {
            loginWrapper.startAnimation(fadeInAnimation);
            loginWrapper.setVisibility(View.VISIBLE);
        }
    }*/

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

    private boolean isRegisterFormValidated() {
        int count = 0;
        if (nameRegisterEt.getText().toString().isEmpty())
            nameRegisterEt.setError(getString(R.string.field_required));
        else count++;

        String email = emailRegisterEt.getText().toString();
        if (email.isEmpty())
            emailRegisterEt.setError(getString(R.string.field_required));
        else {
            if (!TextUtils.isEmailValid(email))
                emailRegisterEt.setError(getString(R.string.email_not_valid));
            else count++;
        }

        String password = passwordRegisterEt.getText().toString();
        String passwordConfirmation = passwordConfirmationRegisterEt.getText().toString();
        if (passwordRegisterEt.getText().toString().isEmpty())
            passwordRegisterEt.setError(getString(R.string.field_required));
        else count++;

        if (!password.equals(passwordConfirmation))
            passwordConfirmationRegisterEt.setError(getString(R.string.password_not_match));
        else count++;

        return count == 4;
    }

    private RegisterJson.OnRegister onRegister = new RegisterJson.OnRegister() {
        @Override
        public void onSuccess(RegisterJson.Response response) {
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
    }

    @Override
    public void onBackPressed() {
        if (loginView.getVisibility() == View.VISIBLE) {
            loginView.changeVisibilityState();
            return;
        }

        /*if (registerWrapper.getVisibility() == View.VISIBLE) {
            changeRegisterPageVisibility();
            return;
        }*/

        super.onBackPressed();
    }

    @Click(R.id.register_tv)
    void doRegister() {
        if (isRegisterFormValidated()) {
            ViewUtils.hideSoftKeyboard(this);
            RegisterJson.Request request = new RegisterJson.Request();
            request.name = nameRegisterEt.getText().toString();
            request.email = emailRegisterEt.getText().toString();
            request.password = passwordRegisterEt.getText().toString();
            request.password_confirmation = passwordConfirmationRegisterEt.getText().toString();

            registerJson.post(request, onRegister);
        }
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
        changeRegisterPageVisibility();
    }

    @Click(R.id.close_register_page_iv)
    void closeRegisterPage() {
        changeRegisterPageVisibility();
    }

    @Click(R.id.register_here_tv)
    void showRegisterPage() {
        loginView.changeVisibilityState();
        changeRegisterPageVisibility();
    }

    @Click(R.id.login_here_tv)
    void showLoginPage() {
        loginView.changeVisibilityState();
        changeRegisterPageVisibility();
    }
}
