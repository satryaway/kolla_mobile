package com.jixstreet.kolla.intro;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.utility.TextUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.res.AnimationRes;

/**
 * Created by satryaway on 2/28/2017.
 * satryaway@gmail.com
 */

@EViewGroup
public abstract class IntroView extends LinearLayout {

    public abstract void changeVisibilityState();

    @AnimationRes(R.anim.fade_in_effect)
    protected Animation fadeIn;

    @AnimationRes(R.anim.fade_out_effect)
    protected Animation fadeOut;

    public OnRegisterHereClicked onRegisterHereClicked;
    public OnLoginHereClicked onLoginHereClicked;

    public interface OnRegisterHereClicked {
        void onRegister();
    }

    public interface OnLoginHereClicked {
        void onLogin();
    }

    public void setOnRegisterHereClicked(OnRegisterHereClicked onRegisterHereClicked) {
        this.onRegisterHereClicked = onRegisterHereClicked;
    }

    public void setOnLoginHereClicked(OnLoginHereClicked onLoginHereClicked) {
        this.onLoginHereClicked = onLoginHereClicked;
    }

    public IntroView(Context context) {
        super(context);
    }

    public IntroView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void changeVisibilityState(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            ViewUtils.hideSoftKeyboard((Activity) getContext());
            view.startAnimation(fadeOut);
            view.setVisibility(View.INVISIBLE);
        } else {
            view.startAnimation(fadeIn);
            view.setVisibility(View.VISIBLE);
        }
    }

    public boolean isTextFilled(View view) {
        if (((EditText) view).getText().toString().isEmpty()) {
            ((EditText) view).setError(getContext().getString(R.string.field_required));
            return false;
        }

        return true;
    }

    public boolean isValidEmail(View view) {
        if (isTextFilled(view)) {
            if (TextUtils.isEmailValid(((EditText) view).getText().toString()))
                return true;

            ((EditText) view).setError(getContext().getString(R.string.email_not_valid));
        }

        return false;
    }
}
