package com.jixstreet.kolla.network;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;

import com.jixstreet.kolla.utility.Log;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;

/**
 * Http call to REST API with optional progress dialog. <br />
 *
 * @param <T> Type of tag that can be included on each call.
 * @author satryaway@gmail.com
 */
public class ProgressOkHttp<R, T> extends BaseHttp<R, T> {

    private static final String RETRY = "Retry";

    @Nullable
    private OnFinishedCallback2<R, T> mCallback;

    @Nullable
    private ResponseHandler<R, T> mResponseHandler;

    @Nullable
    private Request mPrevRequest;

    public ProgressOkHttp(@NonNull Context context, @NonNull Class<R> type) {
        super(context, type);
    }

    @Override
    public void cancel() {
        super.cancel();
        if (mResponseHandler != null)
            mResponseHandler.cleanupDialog(mHandler);
    }

    /**
     * Retry previous request using the same response handler.
     */
    @Override
    public void retry() {
        if (mResponseHandler == null || mPrevRequest == null)
            return;
        mClient.newCall(mPrevRequest).enqueue(mResponseHandler);
        Log.d(RETRY, mPrevRequest.toString());
    }

    /**
     * For derived class
     */
    protected ResponseHandler<R, T> createResponseHandler(@Nullable T tag,
                                                          @Nullable RunnableDialog<R, T> runnableDialog) {
        return new ResponseHandler<>(this, tag, internalCallback, runnableDialog);
    }

    private void initResponseHandler(@Nullable T tag,
                                     @Nullable OnFinishedCallback2<R, T> callback,
                                     @Nullable String msg,
                                     boolean cancelable) {
        mCallback = callback;

        RunnableDialog<R, T> runnableDialog = null;
        if (msg != null) {
            runnableDialog = new RunnableDialog<>(this, msg, cancelable, onProgressCanceled);
            // if network not finished within 400ms then start showing dialog
            mHandler.postDelayed(runnableDialog, 400);
        }

        mResponseHandler = createResponseHandler(tag, runnableDialog);
    }

    /**
     * Post using application/x-www-form-urlencoded. <br />
     * See http://stackoverflow.com/questions/4007969
     *
     * @param params     Key-Value pairs. Both Key and Value are String.
     * @param msg        if not null means display progress dialog
     * @param cancelable if true, you can cancel progress dialog, and handle the event
     *                   using callback
     */
    public void post(@NonNull String route,
                     @Nullable ArrayList<Pair<String, String>> params,
                     @Nullable T tag,
                     @Nullable OnFinishedCallback2<R, T> callback,
                     @Nullable String msg,
                     boolean cancelable) {
        if (!isOnline(tag, callback))
            return;
        String url = prepareRoute(route);
        initResponseHandler(tag, callback, msg, cancelable);

        RequestBody requestBody = createPostRequestBody(params);
        if (TextUtils.isEmpty(url) || requestBody == null)
            return;

        executePost(url, requestBody, fromParams(params));
    }

    /**
     * Post using multipart/form-data, normally used for file upload. <br />
     * See http://stackoverflow.com/questions/4007969
     *
     * @param params     Key-Value pairs. Key is String. Value can contain either String or File.
     * @param msg        if not null means display progress dialog
     * @param cancelable if true, you can cancel progress dialog, and handle the event
     *                   using callback
     */
    public void postF(@NonNull String route,
                      @Nullable ArrayList<? extends Pair<String, ?>> params,
                      @Nullable T tag,
                      @Nullable OnFinishedCallback2<R, T> callback,
                      @Nullable String msg,
                      boolean cancelable) {
        if (!isOnline(tag, callback))
            return;
        String url = prepareRoute(route);
        initResponseHandler(tag, callback, msg, cancelable);

        RequestBody requestBody = createPostRequestBodyMP(params);
        if (TextUtils.isEmpty(url) || requestBody == null)
            return;

        executePost(url, requestBody, fromParams(params));
    }

    private void executePost(@NonNull String url,
                             @NonNull RequestBody requestBody,
                             @Nullable String params) {
        if (mResponseHandler == null)
            return;

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .tag(this)
                .build();

        if (mTimeSinceEnabled < 0)
            mTimeSinceEnabled = System.nanoTime();
        mResponseHandler.dispatchTime = System.nanoTime();

        mClient.newCall(request).enqueue(mResponseHandler);
        Log.d("POST", url + ", " + params);

        mPrevRequest = request;
    }

    /**
     * @param msg        if not null means display progress dialog
     * @param cancelable if true, you can cancel progress dialog, and handle the event
     *                   using callback
     */
    public void get(@NonNull String route,
                    @Nullable ArrayList<Pair<String, String>> params,
                    @Nullable T tag,
                    @Nullable OnFinishedCallback2<R, T> callback,
                    @Nullable String msg,
                    boolean cancelable) {
        if (!isOnline(tag, callback))
            return;
        String url = prepareRoute(route);
        initResponseHandler(tag, callback, msg, cancelable);

        Request request = createGetRequest(url, params);
        if (request == null || mResponseHandler == null)
            return;

        if (mTimeSinceEnabled < 0)
            mTimeSinceEnabled = System.nanoTime();
        mResponseHandler.dispatchTime = System.nanoTime();

        mClient.newCall(request).enqueue(mResponseHandler);
        Log.d("GET", url + ", " + fromParams(params));

        mPrevRequest = request;
    }

    /**
     * Callback is redirected here first to do proper cleanup for mResponseHandler.
     * NOTE that this method will run from handler.post(Runnable()).
     */
    protected OnFinishedCallback2<R, T> internalCallback = new OnFinishedCallback2<R, T>() {
        @Override
        public void handle(@NonNull ResultType type, R response, T tag, String errorMsg) {
            // TODO CRITICAL: check ResultType, handle when fail
            if (mCallback != null)
                mCallback.handle(type, response, tag, errorMsg);
        }
    };

    /**
     * When user cancel progress dialog. Must return result with cancel type.
     */
    private OnCancelListener onProgressCanceled = new OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            mClient.cancel(this);

            String errorMsg = "Dibatalkan oleh user";

            if (mResponseHandler != null) {
                mResponseHandler.cleanupDialog(mHandler);
                mResponseHandler.handleResult(ResultType.Cancel, null, errorMsg);
            }
            // clear after use
            mTimeSinceEnabled = -1L;
        }
    };

}
