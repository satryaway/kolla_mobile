package com.jixstreet.kolla.booking.room;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.jixstreet.kolla.booking.room.payment.OnGetRoomDetail;
import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.parent.DefaultRequest;
import com.jixstreet.kolla.parent.DefaultResponse;
import com.jixstreet.kolla.parent.ModelJson;

import java.util.ArrayList;

/**
 * Created by satryaway on 4/12/2017.
 * satryaway@gmail.com
 */

public class RoomDetailJson extends ModelJson {
    private OnGetRoomDetail onGetRoomDetail;
    private ProgressOkHttp<Response, Void> http;

    public RoomDetailJson(Context context, String id) {
        super(context, id);
        http = new ProgressOkHttp<>(context, Response.class);
    }

    @Override
    public String getRoute() {
        return "/rooms/";
    }

    @Override
    public void cancel() {
        http.cancel();
    }

    public class Response extends DefaultResponse {
        public Room data;
    }

    public void getRoomDetail(OnGetRoomDetail onGetRoomDetail) {
        this.onGetRoomDetail = onGetRoomDetail;
        http.get(ROUTE, null, null, onGetRoomDetailFinished,
                "Fetching Room Detail", true);
    }

    private OnFinishedCallback<Response, Void> onGetRoomDetailFinished = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success && response != null) {
                if (RStatus.OK.equals(response.status)) {
                    onGetRoomDetail.onSuccess(response);
                } else onGetRoomDetail.onFailure(response.message);
            } else onGetRoomDetail.onFailure(errorMsg);
        }
    };
}
