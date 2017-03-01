package com.jixstreet.kolla.login;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.Main2Activity;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.intro.IntroView;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

/**
 * Created by satryaway on 2/28/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.view_login)
public class LoginView extends IntroView {
    @ViewById(R.id.email_et)
    protected EditText emailEt;

    @ViewById(R.id.password_et)
    protected EditText passwordEt;

    @ViewById(R.id.forgot_password_tv)
    protected TextView forgotPasswordTv;

    private LoginJson loginJson;
    private FacebookLoginJson facebookLoginJson;

    public LoginView(Context context) {
        super(context);
        init(context);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        loginJson = new LoginJson(context);
        facebookLoginJson = new FacebookLoginJson(context);
    }

    private boolean isLoginFormValidated() {
        int count = 0;
        if (isValidEmail(emailEt)) count++;
        if (isTextFilled(passwordEt)) count++;

        return count == 2;
    }

    private LoginJson.OnLogin onLogin = new LoginJson.OnLogin() {
        @Override
        public void onSuccess(LoginJson.Response response) {
            LoginJson.Response.saveData(getContext(), response.access_token, response.data);
            DialogUtils.makeSnackBar(CommonConstant.success, getContext(),
                    ((Activity) getContext()).getWindow().getDecorView(), response.message);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityUtils.startActivity((Activity)getContext(), Main2Activity.class);
                }
            }, 1500);
        }

        @Override
        public void onFailure(String text) {
            DialogUtils.makeSnackBar(CommonConstant.failed, getContext(),
                    ((Activity) getContext()).getWindow().getDecorView(), text);
        }
    };

    public void handleFacebookLogin(JSONObject object, String token) {
        FacebookLoginJson.Request request = new FacebookLoginJson.Request();
        request.email = object.optString("email");
        request.user_token = token;
        facebookLoginJson.post(request, new FacebookLoginJson.OnFacebookLogin() {
            @Override
            public void onSuccess(FacebookLoginJson.Response response) {
                LoginJson.Response.saveData(getContext(), response.access_token, response.data);
                DialogUtils.makeSnackBar(CommonConstant.success, getContext(),
                        ((Activity) getContext()).getWindow().getDecorView(), response.message);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityUtils.startActivity((Activity)getContext(), Main2Activity.class);
                    }
                }, 1500);
            }

            @Override
            public void onFailure(String message) {
                DialogUtils.makeSnackBar(CommonConstant.failed, getContext(),
                        ((Activity) getContext()).getWindow().getDecorView(), message);
            }
        });
    }

    @Override
    public void changeVisibilityState() {
        super.changeVisibilityState(this);
    }

    @Click(R.id.close_login_page_iv)
    void closeLoginPage() {
        super.changeVisibilityState(this);
    }

    @Click(R.id.register_here_tv)
    void showRegisterPage() {
        super.changeVisibilityState(this);
        loginInterface.onRegisterHereClicked();
    }

    @Click(R.id.login_tv)
    void doLogin() {
        if (isLoginFormValidated()) {
            ViewUtils.hideSoftKeyboard((Activity) getContext());
            LoginJson.Request request = new LoginJson.Request();
            request.email = emailEt.getText().toString();
            request.password = passwordEt.getText().toString();

            loginJson.post(request, onLogin);
        }
    }

    @Click(R.id.fb_login_tv)
    void doFacebookLogin() {
        loginInterface.onFacebookLoginClicked();
    }
}
