package com.jixstreet.kolla.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jixstreet.kolla.R;

/**
 * Created by satryaway on 2/16/2017.
 * satryaway@gmail.com
 */

public class ViewUtils {

    public static ViewGroup getRootView(@NonNull Activity activity) {
        ViewGroup content = Convert.as(ViewGroup.class, activity.findViewById(android.R.id.content));
        if (content == null)
            return null;

        return Convert.as(ViewGroup.class, content.getChildAt(0));
    }

    public static LinearLayoutManager getLayoutManager(Context context, final boolean isVertical) {
        return new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return isVertical;
            }
        };
    }

    public static LinearLayoutManager getHorizontalLayoutManager(Context context) {
       return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }

    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void makeStatusBarTransparent(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setRecyclerViewDivider(RecyclerView recyclerView, LinearLayoutManager layoutManager) {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public static void setAnimation(Context context, View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_top);
        viewToAnimate.startAnimation(animation);
    }

    public static void setToolbar(AppCompatActivity activity, Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    public static void setToolbarNoUpButton(AppCompatActivity activity, Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    public static void setTextView(TextView textView, String text) {
        if (textView != null && text != null) {
            textView.setText(text);
        }
    }

    public static void setTextViewByVisibility(TextView textView, String text) {
        if (textView != null && text != null) {
            textView.setText(text);
            if (text.isEmpty()) {
                setVisibility(textView, View.GONE);
            }
        }
    }

    public static void setVisibility(View view, int visibility) {
        if (view != null)
            view.setVisibility(visibility);
    }

    public static String getTextFromEditText(EditText editText) {
        if (editText != null)
            return editText.getText().toString();
        else return "";
    }

    public static String getTextFromTextView(TextView textView) {
        if (textView != null)
            return textView.getText().toString();
        else return "";
    }

    public static void setError(EditText editText, String string) {
        if (editText != null && !string.isEmpty())
            editText.setError(string);
    }

    public static void setEditTextHint(EditText editText, String string) {
        if (editText != null) {
            editText.setHint(string.isEmpty() ? "" : string);
        }
    }

    public static void setTextIntoEditText(EditText editText, String s) {
        if (editText != null) {
            editText.setText(s == null ? "" : s);
        }
    }

    public static void setEditTextEditability(EditText editText, boolean editable) {
        if (editText != null) {
            editText.setEnabled(editable);
        }
    }
}
