package com.jixstreet.kolla.booking;

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
 * Created by satryaway on 5/4/2017.
 * satryaway@gmail.com
 */

public class BookingJson extends ModelJson {
    private OnBooking onBooking;
    private ProgressOkHttp<Response, Void> httpBooking;

    public BookingJson(Context context, String id) {
        super(context, id + "/book");
        httpBooking = new ProgressOkHttp<>(context, Response.class);
    }

    @Override
    public String getRoute() {
        return "/rooms/";
    }

    @Override
    public void cancel() {

    }

    public static class Request extends DefaultRequest {
        public String date;
        public String time;
        public String duration;
        public String guest;
        public String event_name;
        public String booking_type;
        public String full_name;
        public String payment_type;
        public String location;

        @Override
        public ArrayList<Pair<String, String>> getParams() {
            ArrayList<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("date", date));
            params.add(new Pair<>("time", time));
            params.add(new Pair<>("duration", duration));
            params.add(new Pair<>("guest", guest));
            params.add(new Pair<>("event_name", event_name));
            params.add(new Pair<>("booking_type", booking_type));
            params.add(new Pair<>("full_name", full_name));
            params.add(new Pair<>("payment_type", payment_type));

            return params;
        }
    }

    public static class Response extends DefaultResponse {
        public Data data;

        public class Data {
            public String user_id;
            public String room_id;
            public String date;
            public String start_time;
            public String end_time;
            public String duration;
            public String total_guests;
            public String price;
            public String price_type;
            public String status;
            public String full_name;
            public String event_name;
            public String booking_type;
            public String created_at;
            public String updated_at;
            public String id;
        }
    }

    public void setBooking(Request request, OnBooking onBooking) {
        this.onBooking = onBooking;
        httpBooking.post(ROUTE, request.getParams(), null,
                onBookingFinished, "Booking...", true);
    }

    private OnFinishedCallback<Response, Void> onBookingFinished = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success && response != null) {
                if (RStatus.OK.equals(response.status))
                    onBooking.onSuccess(response);
                else onBooking.onFailure(response.message);
            } else onBooking.onFailure(errorMsg);

        }
    };
}
