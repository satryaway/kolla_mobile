package com.jixstreet.kolla.intro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.login.LoginView;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.register.RegisterView;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.Log;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.Arrays;

@EActivity(R.layout.activity_intro)
public class IntroActivity extends AppCompatActivity implements IntroView.LoginInterface,
        IntroView.RegisterInterface {

    public static String paramsCode = IntroActivity.class.getName().concat("1");

    @ViewById(R.id.background_vp)
    protected ViewPager backgroundVp;

    @ViewById(R.id.tabs)
    protected TabLayout tabLayout;

    @ViewById(R.id.login_view)
    protected LoginView loginView;

    @ViewById(R.id.register_view)
    protected RegisterView registerView;

    private CallbackManager callbackManager;

    @AfterViews
    void onViewsCreated() {
        collectIntent();
        modifyStatusBar();
        initUI();
        initPager();
    }

    private void collectIntent() {
        String param = ActivityUtils.getParam(this, paramsCode, String.class);
        if (param != null && param.equals(RStatus.SESSION_EXPIRED)) {
            DialogUtils.makeSnackBar(CommonConstant.failed, this, getWindow().getDecorView(),
                    getString(R.string.session_expired));
        }
    }

    private void initUI() {
        loginView.setLoginInterface(this);
        registerView.setRegisterInterface(this);
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

    private void initFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));
        loginManager.registerCallback(callbackManager, facebookCallback);
    }

    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("login", loginResult.toString());
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), createGraphRequestCallback());

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,first_name,last_name,email,gender,birthday,location");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            DialogUtils.makeSnackBar(CommonConstant.failed, IntroActivity.this,
                    getWindow().getDecorView(), "Login Cancelled");
        }

        @Override
        public void onError(FacebookException error) {
            DialogUtils.makeSnackBar(CommonConstant.failed, IntroActivity.this,
                    getWindow().getDecorView(), error.getMessage());
        }
    };

    @NonNull
    private GraphRequest.GraphJSONObjectCallback createGraphRequestCallback() {
        return new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object.optString("email").isEmpty()) {
                    DialogUtils.makeSnackBar(CommonConstant.failed, IntroActivity.this,
                            getWindow().getDecorView(), getString(R.string.check_your_facebook_privacy));
                    return;
                }

                loginView.handleFacebookLogin(object, response.getRequest().getAccessToken().getToken());
            }
        };
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
    public void onRegisterHereClicked() {
        registerView.changeVisibilityState();
    }

    @Override
    public void onFacebookLoginClicked() {
        initFacebook();
    }

    @Override
    public void onLogin() {
        loginView.changeVisibilityState();
    }
}
