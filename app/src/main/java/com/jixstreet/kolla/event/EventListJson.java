package com.jixstreet.kolla.event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.parent.DefaultPagingResponse;
import com.jixstreet.kolla.parent.DefaultRequest;
import com.jixstreet.kolla.parent.DefaultResponse;
import com.jixstreet.kolla.parent.ModelJson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 6/8/2017.
 * satryaway@gmail.com
 */

public class EventListJson extends ModelJson {
    private OnGetEventList onGetEventList;
    private ProgressOkHttp<Response, Void> httpEventList;

    public EventListJson(Context context) {
        super(context);
        httpEventList = new ProgressOkHttp<>(context, Response.class);
    }

    @Override
    public String getRoute() {
        return "/events";
    }

    @Override
    public void cancel() {

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

        public class Data extends DefaultPagingResponse {
            public List<Event> data;
        }
    }

    public void get(Request request, OnGetEventList onGetEventList) {
        this.onGetEventList = onGetEventList;
        httpEventList.get(ROUTE, request.getParams(), null, onGetEventDone, "Retrieving events...", true);
    }

    private OnFinishedCallback<Response, Void> onGetEventDone = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success && response != null) {
                if (RStatus.OK.equals(response.status))
                    onGetEventList.onSuccess(response);
                else onGetEventList.onFailure(response.message);
            } else onGetEventList.onFailure(errorMsg);
        }
    };
}
