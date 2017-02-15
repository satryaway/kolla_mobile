package com.jixstreet.kolla.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.utility.ImageUtils;


/**
 * Custom progress dialog with animated image. No message, title, or buttons.
 *
 * @author Dwi C, 9/14/2016.
 */
public class CustomLoadingDialog extends Dialog {

    private boolean cancelable = true;

    public CustomLoadingDialog(@NonNull Context context,
                               boolean cancelable,
                               @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

        this.cancelable = cancelable;
        init();
    }

    @Override
    protected void onStop() {
        Window window = getWindow();
        if (window != null)
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onStop();
    }

    @Override
    public void onBackPressed() {
        // NOTE: setCancelable() also determine the effect of pressing back button on dialog.
        // however on some phone that didn't work, so we also try to override this method.
        if (cancelable)
            cancel();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.d_loading_view);

        Window window = getWindow();
        if (window != null) {
            View root = window.getDecorView();
            ImageUtils.loadLocalGifImage(getContext(), root, R.id.imgLoading, R.drawable.loading);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // we must prevent screen off to prevent transaction http from being stopped/cancel().
            // currently only transaction progress that set this dialog as non-cancelable.
            // so we assume that if this dialog is non-cancelable, screen must not turned off.
            if (!cancelable)
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        // code below is to remove window border
        // getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // disable closing dialog when touching outside window dialog.
        setCanceledOnTouchOutside(false);
    }

    /**
     * Static helper to create and show custom progress dialog.
     *
     * @return Instance of dialog created.
     */
    public static CustomLoadingDialog show(@NonNull Context context,
                                           boolean cancelable,
                                           @Nullable OnCancelListener cancelListener) {
        CustomLoadingDialog dialog = new CustomLoadingDialog(context, cancelable, cancelListener);
        dialog.show();

        return dialog;
    }

}
