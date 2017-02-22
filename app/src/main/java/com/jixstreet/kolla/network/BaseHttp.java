package com.jixstreet.kolla.network;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.jixstreet.kolla.DynamicConfig;
import com.jixstreet.kolla.utility.Convert;
import com.jixstreet.kolla.utility.StringUtils;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by satryaway on 2/15/2017.
 * satryaway@gmail.com
 */

public abstract class BaseHttp<R, T> {

    private static final String NETWORK_UNAVAILABLE = "Network Unavailable";
    private static final String BASE_HTTP = "BaseHttp";
    private static final java.lang.String CONTEXT_NULL = "Context is null";

    @NonNull
    protected final WeakReference<Context> contextWR;
    @NonNull
    protected final OkHttpClient mClient;
    @NonNull
    protected final Class<R> mType;
    @NonNull
    protected Handler mHandler;

    protected volatile long mTimeSinceEnabled = -1L;

    public BaseHttp(@NonNull Context context, @NonNull Class<R> type) {
        this(context, type, OkHttpFactoryFP.instance());
    }

    public BaseHttp(@NonNull Context context, @NonNull Class<R> type, @NonNull OkHttpClient client) {
        mClient = client;
        contextWR = new WeakReference<>(context);
        mType = type;
        mHandler = new Handler();
    }

    public void cancel() {
        mTimeSinceEnabled = -1L;
        mClient.cancel(this);
    }

    public abstract void retry();

    protected boolean isOnline(@Nullable T tag, @Nullable OnFinishedCallback2<R, T> callback) {
        Context context = contextWR.get();
        if (context == null) {
            Log.d(BASE_HTTP, CONTEXT_NULL); // could cause subtle bug later, just log it here
            return false;
        }

        if (!SysUtils.isOnline(context)) {
            if (callback != null)
                callback.handle(ResultType.Fail, null, tag, NETWORK_UNAVAILABLE);
            return false;
        }
        return true;
    }

    @Nullable
    public String prepareRoute(@NonNull String route) {
        Context context = contextWR.get();
        if (context == null) {
            Log.d(BASE_HTTP, CONTEXT_NULL); // could cause subtle bug later, just log it here
            return null;
        }

        if (route.startsWith("http://") || route.startsWith("https://"))
            return route;
        else
            return StringUtils.combineUrl(DynamicConfig.getBaseUrl(context), route);
    }

    /**
     * This method is used for GET and debug string.
     */
    @NonNull
    protected String fromParams(@Nullable ArrayList<? extends Pair<String, ?>> params) {
        if (params == null)
            return "";
        StringBuilder sb = new StringBuilder(params.size());
        for (Pair<String, ?> pair : params) {

            if (pair.second instanceof String) {
                sb.append(pair.first).append('=').append(
                        Convert.as(String.class, pair.second)).append('&');

            } else if (pair.second instanceof File) {
                File file = Convert.as(File.class, pair.second);
                if (file == null || !file.isFile())
                    continue;
                sb.append(pair.first).append("=File(").append(file.getAbsolutePath()).append(")&");
            }
        }
        sb.setLength(Math.max(sb.length() - 1, 0));
        return sb.toString();
    }

    /**
     * Create application/x-www-form-urlencoded request body from params.
     */
    @Nullable
    protected RequestBody createPostRequestBody(@Nullable ArrayList<Pair<String, String>> params) {
        RequestBody requestBody = null;
        if (params != null) {
            FormEncodingBuilder feb = new FormEncodingBuilder();
            for (Pair<String, String> pair : params) {
                feb.add(pair.first, (pair.second != null) ? pair.second : "");
            }
            requestBody = feb.build();
        }
        return requestBody;
    }

    /**
     * Create multipart request body from params.
     */
    @Nullable
    protected RequestBody createPostRequestBodyMP(@Nullable ArrayList<? extends Pair<String, ?>> params) {
        RequestBody requestBody = null;
        if (params != null) {
            MultipartBuilder mb = new MultipartBuilder();
            mb.type(MultipartBuilder.FORM);
            for (Pair<String, ?> pair : params) {

                if (pair.second instanceof File) {
                    File file = Convert.as(File.class, pair.second);
                    if (file == null || !file.isFile())
                        continue;
                    RequestBody rb = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                    mb.addFormDataPart(pair.first, file.getName(), rb);

                } else if (pair.second instanceof String) {
                    String second = Convert.as(String.class, pair.second);
                    if (second != null)
                        mb.addFormDataPart(pair.first, second);
                }
            }
            requestBody = mb.build();
        }
        return requestBody;
    }

    @Nullable
    protected Request createGetRequest(@Nullable String url,
                                       @Nullable ArrayList<Pair<String, String>> params) {
        if (TextUtils.isEmpty(url))
            return null;

        HttpUrl httpUrl;
        if (params != null)
            httpUrl = HttpUrl.parse(url + "?" + fromParams(params));
        else
            httpUrl = HttpUrl.parse(url);

        if (httpUrl == null)
            return null;

        return new Request.Builder()
                .url(httpUrl)
                .tag(this)
                .build();
    }

}
