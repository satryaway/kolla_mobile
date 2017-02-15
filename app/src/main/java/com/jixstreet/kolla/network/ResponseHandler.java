package com.jixstreet.kolla.network;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jixstreet.kolla.utility.Log;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Response handler to be used by http client callback.
 *
 * @param <R> Type of object that will be returned by GSON serializer. For
 *            example: Object, Item[], LinkedTreeMap, LoginJson.Response, etc.
 * @param <T> Type of tag that can be included.
 * @author Dwi on 02-Aug-16.
 */
public class ResponseHandler<R, T> implements Callback {
    public long dispatchTime = System.nanoTime();

    @NonNull
    protected final BaseHttp<R, T> parent;

    /**
     * NOTE: For some reason we cannot set this as weak reference, otherwise most of time will
     * become null.
     */
    @Nullable
    protected final T tag;

    @Nullable
    protected final OnFinishedCallback2<R, T> callback;

    @Nullable
    protected final RunnableDialog<R, T> runnableDialog;

    public ResponseHandler(@NonNull BaseHttp<R, T> parent,
                           @Nullable T tag,
                           @Nullable OnFinishedCallback2<R, T> callback,
                           @Nullable RunnableDialog<R, T> runnableDialog) {
        this.parent = parent;
        this.tag = tag;
        this.callback = callback;
        this.runnableDialog = runnableDialog;
    }

    @Override
    public void onResponse(final Response response) throws IOException {
        onStringHandlerFinished(ResultType.Success, response, "");
    }

    @Override
    public void onFailure(Request request, final IOException e) {
        onStringHandlerFinished(ResultType.Fail, null, e.getMessage());
    }

    protected void onStringHandlerFinished(@NonNull ResultType rt,
                                           @Nullable Response response,
                                           @NonNull String errorMsg) {
        if (response != null) {
            try {
                Log.d("Net", "Getting response from: " + response.request().url().toString());
            } catch (Exception e) {
                Log.d("Net", "Source url is not available");
            }
        }

        cleanupDialog(parent.mHandler);

        if (parent.mTimeSinceEnabled < 0) {
            Log.w("Net", "Http Client is disabled");
            return;
        }
        if (dispatchTime < parent.mTimeSinceEnabled) {
            Log.e("Net", "Encountered old ResponseHandler from previous dispatcher");
            return;
        }

        ConvertResult<R> cr = convertResponseToR(rt, response);
        if (cr.success)
            handleResult(rt, cr.result, errorMsg);
        else
            // conversion failure should return fail, null object, and appropriate message
            handleResult(ResultType.Fail, null, "Gangguan server, response tidak dikenal.");
    }

    /**
     * Method to convert response. Will fail if: <br />
     * - ResultType isn't Success. <br />
     * - Response is empty or cannot be converted. <br />
     * - Session expired.
     */
    @NonNull
    protected ConvertResult<R> convertResponseToR(@NonNull ResultType rt, @Nullable Response response) {
        String strResult = "";

        ConvertResult<R> cr = new ConvertResult<>();
        cr.success = false;

        if (rt == ResultType.Success && response != null) {
            try {
                strResult = response.body().string();
                Log.d("Net", strResult);

                // PERFORMANCE WARNING: nested try here, each calling Gson().fromJson() twice
                if (!isStatusExpired(strResult)) {
                    cr.result = new Gson().fromJson(strResult, parent.mType);
                    cr.success = true;
                }

            } catch (Exception e) {
                // don't show internal error to user
                Log.printStackTrace(e);
                Log.d("Net", "Response= " + strResult);
            }
        }

        return cr;
    }

    public void handleResult(@NonNull final ResultType type,
                             @Nullable final R response,
                             @Nullable final String errorMsg) {
        if (parent.mTimeSinceEnabled < 0)
            return;

        parent.mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.handle(type, response, tag, errorMsg);
            }
        });
    }

    /**
     * Dismiss dialog (if any) and remove it from handler.
     * Can also be called from dialog cancel event.
     */
    public void cleanupDialog(Handler handler) {
        if (runnableDialog != null && handler != null) {
            handler.removeCallbacks(runnableDialog);
            runnableDialog.dismiss();
        }
    }

    public boolean isStatusExpired(@Nullable final String strResult) {
        if (TextUtils.isEmpty(strResult))
            return false;

        RDefault response;
        try {
            response = new Gson().fromJson(strResult, RDefault.class);
        } catch (JsonSyntaxException e) {
            Log.d("Net", "This API response doesn't have 'status' field.");
            return false;
        }

        if (response != null && RStatus.SESSION_EXPIRED.equals(response.status)) {
            Log.d("Net", "Encountered expired token, returned to Login.");

            Context context = parent.contextWR.get();
            if (context != null) {
                //Return to login page
            } else {
                Log.d("ResponseHandler", "Context is null");
            }

            return true;
        }
        return false;
    }


    protected static class ConvertResult<S> {
        public boolean success; // true if conversion is a success
        public S result;
    }

}
