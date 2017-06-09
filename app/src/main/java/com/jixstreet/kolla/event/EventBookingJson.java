package com.jixstreet.kolla.event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.parent.DefaultRequest;
import com.jixstreet.kolla.parent.DefaultResponse;
import com.jixstreet.kolla.parent.ModelJson;

import java.util.ArrayList;

/**
 * Created by satryaway on 6/9/2017.
 * satryaway@gmail.com
 */

public class EventBookingJson extends ModelJson {
    private ProgressOkHttp<Response, Void> httpEventBooking;
    private OnEventBooking onEventBooking;

    public EventBookingJson(Context context, String id) {
        super(context, id + "/book");
        httpEventBooking = new ProgressOkHttp<>(context, Response.class);
    }

    @Override
    public String getRoute() {
        return "/events/";
    }

    @Override
    public void cancel() {
    }

    public static class Request extends DefaultRequest {
        public String guests;

        @Override
        public ArrayList<Pair<String, String>> getParams() {
            ArrayList<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("guests", guests));

            return params;
        }
    }

    public static class Response extends DefaultResponse {
        public Event data;
    }

    public void post(Request request, OnEventBooking onEventBooking) {
        this.onEventBooking = onEventBooking;
        httpEventBooking.post(ROUTE, request.getParams(), null, onEventBookingFinished,
                "Booking event...", true);
    }

    private OnFinishedCallback<Response, Void> onEventBookingFinished = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success && response != null) {
                if (RStatus.OK.equals(response.status)) {
                    onEventBooking.onSuccess(response);
                } else onEventBooking.onFailure(response.message);
            } else onEventBooking.onFailure(errorMsg);
        }
    };
}
