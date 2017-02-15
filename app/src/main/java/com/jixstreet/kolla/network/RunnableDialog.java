package com.jixstreet.kolla.network;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jixstreet.kolla.ui.dialog.CustomLoadingDialog;
import com.jixstreet.kolla.utility.Log;

/**
 * A scheduler for dialog that can be added into or removed from Handler's {@link android.os.MessageQueue}.
 *
 * @author Dwi on 02-Aug-16.
 */
public class RunnableDialog<R, T> implements Runnable {
    @NonNull
    private final BaseHttp<R, T> parent;

    /**
     * Current dialog didn't show any message, but we kept it here for use by other dialog.
     */
    @NonNull
    private final String message;

    private final boolean cancelable;

    @Nullable
    private final OnCancelListener onProgressCanceled;

    @Nullable
    public CustomLoadingDialog dlgProgress;

    public RunnableDialog(@NonNull BaseHttp<R, T> parent,
                          @NonNull String message,
                          boolean cancelable,
                          @Nullable OnCancelListener onProgressCanceled) {
        this.parent = parent;
        this.message = message;
        this.cancelable = cancelable;
        this.onProgressCanceled = onProgressCanceled;
    }

    @Override
    public void run() {
        Context context = parent.contextWR.get();
        if (context == null) {
            Log.d("RunnableDialog", "Context is null");
            return;
        }
        dlgProgress = CustomLoadingDialog.show(context, cancelable, onProgressCanceled);
    }

    public void dismiss() {
        // if there are any Leaked window warning or View not attached to window manager exception,
        // ensure that http.cancel() is called from activity onPause().
        if (dlgProgress != null)
            dlgProgress.dismiss();
    }
}
