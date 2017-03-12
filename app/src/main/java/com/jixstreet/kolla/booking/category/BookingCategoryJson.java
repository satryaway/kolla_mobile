package com.jixstreet.kolla.booking.category;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.parent.DefaultPagingResponse;
import com.jixstreet.kolla.parent.DefaultResponse;
import com.jixstreet.kolla.parent.ModelJson;

import java.util.ArrayList;

/**
 * Created by satryaway on 3/8/2017.
 * satryaway@gmail.com
 */

public class BookingCategoryJson extends ModelJson {
    public static final String prefKey = BookingCategoryJson.class.getName().concat("1");
    private final ProgressOkHttp<Response, Void> req;
    private OnGetCategories onGetCategories;

    @Override
    public String getRoute() {
        return "/categories";
    }

    @Override
    public void cancel() {
        req.cancel();
    }

    public BookingCategoryJson(Context context) {
        super(context);
        req = new ProgressOkHttp<>(context, Response.class);
    }

    public class Response extends DefaultResponse {
        public Data data;
    }

    public class Data extends DefaultPagingResponse {
        ArrayList<BookingCategory> data;
    }

    public void get(OnGetCategories onGetCategories) {
        this.onGetCategories = onGetCategories;
        req.get(ROUTE, null, null, onGetCategoriesDone,
                "Fetching categories...", true);
    }

    private OnFinishedCallback<Response, Void> onGetCategoriesDone = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success && response != null) {
                if (response.status.equals(RStatus.OK)) {
                    onGetCategories.onSuccess(response);
                } else {
                    onGetCategories.onFailure(response.message);
                }
            } else {
                onGetCategories.onFailure(errorMsg);
            }
        }
    };
}
