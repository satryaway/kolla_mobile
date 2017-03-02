package com.jixstreet.kolla.register;

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
 * Created by satryaway on 2/23/2017.
 * satryaway@gmail.com
 */

public class RegisterJson extends ModelJson {
    private final ProgressOkHttp<Response, Void> req;
    private OnRegister onRegister;

    @Override
    public String getRoute() {
        return "/users";
    }

    @Override
    public void cancel() {
        req.cancel();
    }

    public RegisterJson(Context context) {
        super(context);
        req = new ProgressOkHttp<>(context, Response.class);
    }

    public interface OnRegister {
        void onSuccess(RegisterJson.Response response);

        void onFailure(String text);
    }

    public static class Request extends DefaultRequest {
        public String name;
        public String email;
        public String password;
        public String password_confirmation;

        @Override
        public ArrayList<Pair<String, String>> getParams() {
            ArrayList<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("name", name));
            params.add(new Pair<>("email", email));
            params.add(new Pair<>("password", password));
            params.add(new Pair<>("password_confirmation", password_confirmation));
            return params;
        }
    }

    public static class Response extends DefaultResponse {
        public UserData data;
    }

    public void post(Request request, OnRegister onRegister) {
        this.onRegister = onRegister;
        req.post(ROUTE, request.getParams(), null, onRegisterDone,
                "Registering...", true);
    }

    private OnFinishedCallback<Response, Void> onRegisterDone
            = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable RegisterJson.Response response, @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success && response != null) {
                if (RStatus.OK.equals(response.status))
                    onRegister.onSuccess(response);
                else
                    onRegister.onFailure(response.message);
            } else {
                onRegister.onFailure(errorMsg);
            }
        }
    };
}
