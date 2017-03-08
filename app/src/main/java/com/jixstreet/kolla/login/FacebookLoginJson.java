package com.jixstreet.kolla.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.jixstreet.kolla.model.UserData;
import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.parent.DefaultResponse;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.parent.DefaultRequest;
import com.jixstreet.kolla.parent.ModelJson;

import java.util.ArrayList;

/**
 * Created by satryaway on 2/28/2017.
 * satryaway@gmail.com
 */

public class FacebookLoginJson extends ModelJson {
    private final ProgressOkHttp<Response, Void> req;
    public OnFacebookLogin onFacebookLogin;

    @Override
    public String getRoute() {
        return "/auth/login/facebook";
    }

    @Override
    public void cancel() {
        req.cancel();
    }

    public FacebookLoginJson(Context context) {
        super(context);
        req = new ProgressOkHttp<>(context, Response.class);
    }

    public interface OnFacebookLogin {
        void onSuccess(Response response);

        void onFailure(String message);
    }

    public static class Request extends DefaultRequest {
        public String email;
        public String user_token;

        @Override
        public ArrayList<Pair<String, String>> getParams() {
            ArrayList<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("email", email));
            params.add(new Pair<>("user_token", user_token));
            return params;
        }
    }

    public static class Response extends DefaultResponse {
        public UserData data;
        public String access_token;
    }

    public void post(Request request, OnFacebookLogin onFacebookLogin) {
        this.onFacebookLogin = onFacebookLogin;
        req.post(ROUTE, request.getParams(), null, onFacebookLoginDone,
                "Logging in...", true);
    }

    private OnFinishedCallback<Response, Void> onFacebookLoginDone
            = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type,
                           @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success && response != null) {
                if (RStatus.OK.equals(response.status))
                    onFacebookLogin.onSuccess(response);
                else
                    onFacebookLogin.onFailure(response.message);
            } else {
                onFacebookLogin.onFailure(errorMsg);
            }
        }
    };
}
