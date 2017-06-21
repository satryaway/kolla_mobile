package com.jixstreet.kolla.profile.event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.jixstreet.kolla.model.BookedEvent;
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
 * Created by satryaway on 6/21/2017.
 * satryaway@gmail.com
 */

public class BookedEventJson extends ModelJson {

    private ProgressOkHttp<Response, Void> httpBookedEvent;
    private OnGetBookedEvent onGetBookedEvent;

    public BookedEventJson(Context context, String id) {
        super(context, id + "/book/events");
        httpBookedEvent = new ProgressOkHttp<>(context, Response.class);
    }

    @Override
    public String getRoute() {
        return "/user/";
    }

    @Override
    public void cancel() {
        httpBookedEvent.cancel();
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

    public class Data extends DefaultPagingResponse {
        public List<BookedEvent> data;
    }

    public void get(Request request, OnGetBookedEvent onGetBookedEvent) {
        this.onGetBookedEvent = onGetBookedEvent;
        httpBookedEvent.get(ROUTE, request.getParams(), null, onGetBookedEventDone,
                "Fetching booked events...", true);
    }

    private OnFinishedCallback<Response, Void> onGetBookedEventDone = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success) {
                if (RStatus.OK.equals(response.status))
                    onGetBookedEvent.onSuccess(response);
                else onGetBookedEvent.onFailure(response.message);
            } else onGetBookedEvent.onFailure(errorMsg);
        }
    };
}
