package com.jixstreet.kolla.logout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.parent.DefaultRequest;
import com.jixstreet.kolla.parent.DefaultResponse;
import com.jixstreet.kolla.parent.ModelJson;

/**
 * Created by satryaway on 6/20/2017.
 * satryaway@gmail.com
 */

public class LogOutJson extends ModelJson {
    private ProgressOkHttp<DefaultResponse, Void> logoutHttp;
    private OnLogOut onLogOut;

    public LogOutJson(Context context) {
        super(context);
        logoutHttp = new ProgressOkHttp<>(context, DefaultResponse.class);
    }

    @Override
    public String getRoute() {
        return "/auth/logout";
    }

    @Override
    public void cancel() {
        logoutHttp.cancel();
    }

    public void logOut(OnLogOut onLogOut) {
        this.onLogOut = onLogOut;
        logoutHttp.get(ROUTE, null, null, onLogoutFinished, "Logging out...", true);
    }

    private OnFinishedCallback<DefaultResponse, Void> onLogoutFinished = new OnFinishedCallback<DefaultResponse, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable DefaultResponse response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success) {
                if (RStatus.OK.equals(response.status))
                    onLogOut.onSuccess(response);
                else onLogOut.onFailure(response.message);
            } else onLogOut.onFailure(errorMsg);
        }
    };
}
