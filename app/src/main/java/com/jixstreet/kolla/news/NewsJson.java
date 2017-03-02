package com.jixstreet.kolla.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.model.NewsDetail;
import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.parent.DefaultResponse;
import com.jixstreet.kolla.parent.DefaultRequest;
import com.jixstreet.kolla.parent.ModelJson;
import com.jixstreet.kolla.utility.StringUtils;

import java.util.ArrayList;

/**
 * Created by satryaway on 3/2/2017.
 * satryaway@gmail.com
 */

public class NewsJson extends ModelJson {

    private final ProgressOkHttp<Response, Void> req;
    private OnGetNews onGetNews;

    @Override
    public String getRoute() {
        return "/news";
    }

    @Override
    public void cancel() {
    }

    public NewsJson(Context context) {
        super(context);
        req = new ProgressOkHttp<>(context, Response.class);
    }

    public static class Request extends DefaultRequest {
        public String page;

        @Override
        public ArrayList<Pair<String, String>> getParams() {
            ArrayList<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("page", page));
            return params;
        }
    }

    public static class Response extends DefaultResponse {
        public Data data;
    }

    public class Data {
        public String total;
        public String per_page;
        public String last_page;
        public String next_page_url;
        public String prev_page_url;
        public String from;
        public String to;
        public ArrayList<NewsDetail> data;
    }

    public void get(Request request, OnGetNews onGetNews) {
        this.onGetNews = onGetNews;
        req.get(ROUTE, request.getParams(), null, onGetNewsDone,
                "Fetching News...", true);
    }

    private OnFinishedCallback<Response, Void> onGetNewsDone = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success && response != null) {
                if (response.status.equals(RStatus.OK))
                    onGetNews.onSucceed(response);
                else onGetNews.onFailure(response.message);
            } else {
                onGetNews.onFailure(errorMsg);
            }
        }
    };
}
