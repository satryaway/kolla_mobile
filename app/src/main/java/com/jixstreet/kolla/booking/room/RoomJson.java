package com.jixstreet.kolla.booking.room;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.jixstreet.kolla.booking.category.BookingCategory;
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
 * Created by satryaway on 3/22/2017.
 * satryaway@gmail.com
 */

public class RoomJson extends ModelJson {
    public static final String paramKey = RoomJson.class.getName().concat("1");
    private ProgressOkHttp<Response, Void> httpRoom;
    private OnGetRoom onGetRoom;

    public RoomJson(Context context) {
        super(context);
        httpRoom = new ProgressOkHttp<>(context, Response.class);
    }

    @Override
    public String getRoute() {
        return "/rooms";
    }

    @Override
    public void cancel() {
        httpRoom.cancel();
    }

    public static class Request extends DefaultRequest {
        public static String paramKey = Request.class.getName().concat("1");
        public BookingCategory bookingCategory;

        public String category;
        public String location;
        public String date;
        public String time;
        public String duration;
        public String guest;
        public String page;
        public boolean isInitial = true;

        @Override
        public ArrayList<Pair<String, String>> getParams() {
            ArrayList<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("category", category));
            params.add(new Pair<>("location", location));
            params.add(new Pair<>("date", date));
            params.add(new Pair<>("time", time));
            params.add(new Pair<>("duration", duration));
            params.add(new Pair<>("guest", guest));
            params.add(new Pair<>("page", page));
            return params;
        }
    }

    public class Response extends DefaultResponse {
        public Data data;
    }

    public class Data extends DefaultPagingResponse {
        public List<Room> data;
    }

    public void getRooms(Request request, OnGetRoom onGetRoom) {
        this.onGetRoom = onGetRoom;
        httpRoom.get(ROUTE, request.getParams(), null,
                onGetRoomFinished, "Fetching Rooms", true);
    }

    private OnFinishedCallback<Response, Void> onGetRoomFinished = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success && response != null) {
                if (response.status.equals(RStatus.OK)) {
                    onGetRoom.onSuccess(response);
                } else
                    onGetRoom.onFailure(response.message);
            } else {
                onGetRoom.onFailure(errorMsg);
            }
        }
    };
}
