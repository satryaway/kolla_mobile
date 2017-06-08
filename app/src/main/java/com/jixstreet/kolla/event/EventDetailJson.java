package com.jixstreet.kolla.event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.parent.DefaultResponse;
import com.jixstreet.kolla.parent.ModelJson;

/**
 * Created by satryaway on 6/8/2017.
 * satryaway@gmail.com
 */

public class EventDetailJson extends ModelJson {
    private ProgressOkHttp<Response, Void> httpEventDetail;
    private OnGetEventDetail onGetEventDetail;

    public EventDetailJson(Context context, String id) {
        super(context, id);
        httpEventDetail = new ProgressOkHttp<>(context, Response.class);
    }

    @Override
    public String getRoute() {
        return "events/";
    }

    @Override
    public void cancel() {

    }

    public class Response extends DefaultResponse {
        public Event data;
    }

    public void get(OnGetEventDetail onGetEventDetail) {
        this.onGetEventDetail = onGetEventDetail;
        httpEventDetail.get(ROUTE, null, null, onGetEventDetailDone, "Getting event detail...", true);
    }

    private OnFinishedCallback<Response, Void> onGetEventDetailDone = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success && response != null) {
                if (RStatus.OK.equals(response.status)) {
                    onGetEventDetail.onSuccess(response);
                } else onGetEventDetail.onFailure(response.message);
            } else onGetEventDetail.onFailure(errorMsg);
        }
    };
}
