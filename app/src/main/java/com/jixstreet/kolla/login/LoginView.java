package com.jixstreet.kolla.login;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.intro.IntroView;
import com.jixstreet.kolla.model.LoginJson;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

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

    private LoginJson loginJson;

    public LoginView(Context context) {
        super(context);
        loginJson = new LoginJson(context);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loginJson = new LoginJson(context);
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
            DialogUtils.makeSnackBar(CommonConstant.success, getContext(),
                    ((Activity) getContext()).getWindow().getDecorView(), response.message);
        }

        @Override
        public void onFailure(String text) {
            DialogUtils.makeSnackBar(CommonConstant.failed, getContext(),
                    ((Activity) getContext()).getWindow().getDecorView(), text);
        }
    };

    @Override
    public void changeVisibilityState() {
        super.changeVisibilityState(this);
    }

    @Click(R.id.close_login_page_iv)
    void closeLoginPage() {
        super.changeVisibilityState(this);
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
}
