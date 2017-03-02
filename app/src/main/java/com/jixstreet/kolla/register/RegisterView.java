
package com.jixstreet.kolla.register;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.EditText;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.MainActivity_;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.intro.IntroView;
import com.jixstreet.kolla.login.LoginJson;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 2/28/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.view_register)
public class RegisterView extends IntroView {

    @ViewById(R.id.name_register_et)
    protected EditText nameRegisterEt;

    @ViewById(R.id.email_register_et)
    protected EditText emailRegisterEt;

    @ViewById(R.id.password_register_et)
    protected EditText passwordRegisterEt;

    @ViewById(R.id.password_confirmation_register_et)
    protected EditText passwordConfirmationRegisterEt;

    private RegisterJson registerJson;

    public RegisterView(Context context) {
        super(context);
        registerJson = new RegisterJson(context);
    }

    public RegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        registerJson = new RegisterJson(context);
    }

    @Override
    public void changeVisibilityState() {
        super.changeVisibilityState(this);
    }

    private boolean isRegisterFormValidated() {
        int count = 0;
        if (isTextFilled(nameRegisterEt)) count++;
        if (isValidEmail(emailRegisterEt)) count++;
        if (isTextFilled(passwordRegisterEt)) count++;

        String password = passwordRegisterEt.getText().toString();
        String passwordConfirmation = passwordConfirmationRegisterEt.getText().toString();

        if (!password.equals(passwordConfirmation))
            passwordConfirmationRegisterEt.setError(getContext().getString(R.string.password_not_match));
        else count++;

        return count == 4;
    }

    private RegisterJson.OnRegister onRegister = new RegisterJson.OnRegister() {
        @Override
        public void onSuccess(RegisterJson.Response response) {
            LoginJson.Response.saveData(getContext(), response.access_token, response.data);
            DialogUtils.makeSnackBar(CommonConstant.success, getContext(),
                    ((Activity) getContext()).getWindow().getDecorView(), response.message);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityUtils.startActivity((Activity)getContext(), MainActivity_.class);
                    ((Activity)getContext()).finish();
                }
            }, 1500);
        }

        @Override
        public void onFailure(String text) {
            DialogUtils.makeSnackBar(CommonConstant.failed, getContext(),
                    ((Activity) getContext()).getWindow().getDecorView(), text);
        }
    };

    @Click(R.id.register_tv)
    void doRegister() {
        if (isRegisterFormValidated()) {
            ViewUtils.hideSoftKeyboard((Activity) getContext());
            RegisterJson.Request request = new RegisterJson.Request();
            request.name = nameRegisterEt.getText().toString();
            request.email = emailRegisterEt.getText().toString();
            request.password = passwordRegisterEt.getText().toString();
            request.password_confirmation = passwordConfirmationRegisterEt.getText().toString();

            registerJson.post(request, onRegister);
        }
    }

    @Click(R.id.close_register_page_iv)
    void closeRegisterPage() {
        super.changeVisibilityState(this);
    }

    @Click(R.id.login_here_tv)
    void showLoginPage() {
        super.changeVisibilityState(this);
        registerInterface.onLogin();
    }

    @Click(R.id.fb_register_tv)
    void registerUserByFacebook() {
        registerInterface.onFacebookLoginClicked();
    }
}
