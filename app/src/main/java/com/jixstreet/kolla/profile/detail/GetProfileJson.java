package com.jixstreet.kolla.profile.detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jixstreet.kolla.model.UserData;
import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.parent.DefaultResponse;
import com.jixstreet.kolla.parent.ModelJson;

/**
 * Created by satryaway on 6/20/2017.
 * satryaway@gmail.com
 */

public class GetProfileJson extends ModelJson {
    private OnGetProfile onGetProfile;
    private ProgressOkHttp<Response, Void> httpProfile;

    public GetProfileJson(Context context, String id) {
        super(context, id);
        httpProfile = new ProgressOkHttp<>(context, Response.class);
    }

    @Override
    public String getRoute() {
        return "/users/";
    }

    @Override
    public void cancel() {
        httpProfile.cancel();
    }

    public static class Response extends DefaultResponse {
        public UserData data;
    }

    public void get(OnGetProfile onGetProfile) {
        this.onGetProfile = onGetProfile;
        httpProfile.get(ROUTE, null, null, onGetProfileFinished, "Getting profile...", true);
    }

    private OnFinishedCallback<Response, Void> onGetProfileFinished = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success) {
                if (RStatus.OK.equals(response.status))
                    onGetProfile.onSuccess(response);
                else onGetProfile.onFailure(response.message);
            } else onGetProfile.onFailure(errorMsg);
        }
    };
}
