package com.jixstreet.kolla.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;

import com.jixstreet.kolla.utility.Log;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;

/**
 * Http call to REST API in background. <br />
 * No progress dialog. <br />
 * Result will never return when canceled. <br />
 *
 * @param <T> Type of tag that can be included on each call.
 * @author Dwi C.
 */
public class BackGroundOkHttp<R, T> extends BaseHttp<R, T> {

    @Nullable
    private ResponseHandler<R, T> mResponseHandler;

    /**
     * Previous request, can be POST or GET.
     */
    @Nullable
    private Request mPrevRequest;

    public BackGroundOkHttp(@NonNull Context context, @NonNull Class<R> type) {
        super(context, type);
    }

    /**
     * Retry previous request using the same response handler.
     */
    @Override
    public void retry() {
        if (mResponseHandler == null || mPrevRequest == null)
            return;
        mClient.newCall(mPrevRequest).enqueue(mResponseHandler);
        Log.d("RETRY", mPrevRequest.toString());
    }

    protected ResponseHandler<R, T> createResponseHandler(@Nullable T tag,
                                                          @Nullable OnFinishedCallback<R, T> callback) {
        return new ResponseHandler<>(this, tag, callback, null);
    }

    private void initResponseHandler(@Nullable T tag,
                                     @Nullable OnFinishedCallback<R, T> callback) {
        mResponseHandler = createResponseHandler(tag, callback);
    }

    /**
     * Post using application/x-www-form-urlencoded. <br />
     * See http://stackoverflow.com/questions/4007969
     *
     * @param params Key-Value pairs. Both Key and Value are String.
     */
    public void post(@NonNull String route,
                     @Nullable ArrayList<Pair<String, String>> params,
                     @Nullable T tag,
                     @Nullable OnFinishedCallback<R, T> callback) {
        if (!isOnline(tag, callback))
            return;
        String url = prepareRoute(route);
        initResponseHandler(tag, callback);

        RequestBody requestBody = createPostRequestBody(params);
        if (TextUtils.isEmpty(url) || requestBody == null)
            return;

        executePost(url, requestBody, fromParams(params));
    }

    /**
     * Post using multipart/form-data, normally used for file upload.
     * See http://stackoverflow.com/questions/4007969
     *
     * @param params Key-Value pairs. Key is String. Value can contain either String or File.
     */
    public void postF(@NonNull String route,
                      @Nullable ArrayList<? extends Pair<String, ?>> params,
                      @Nullable T tag,
                      @Nullable OnFinishedCallback<R, T> callback) {
        if (!isOnline(tag, callback))
            return;
        String url = prepareRoute(route);
        initResponseHandler(tag, callback);

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
//                .cacheControl(CacheControl.FORCE_NETWORK) // disable cache
                .build();

        if (mTimeSinceEnabled < 0)
            mTimeSinceEnabled = System.nanoTime();
        mResponseHandler.dispatchTime = System.nanoTime();

        mClient.newCall(request).enqueue(mResponseHandler);
        Log.d("POST", url + ", " + params);

        mPrevRequest = request;
    }

    public void get(@NonNull String route,
                    @Nullable ArrayList<Pair<String, String>> params,
                    @Nullable T tag,
                    @Nullable OnFinishedCallback<R, T> callback) {
        if (!isOnline(tag, callback))
            return;
        String url = prepareRoute(route);
        initResponseHandler(tag, callback);

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

}
