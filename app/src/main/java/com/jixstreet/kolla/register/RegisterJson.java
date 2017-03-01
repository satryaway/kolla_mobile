package com.jixstreet.kolla.register;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.jixstreet.kolla.model.UserData;
import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.RDefault;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;

import java.util.ArrayList;

/**
 * Created by satryaway on 2/23/2017.
 * satryaway@gmail.com
 */

public class RegisterJson {
    public static final transient String API = "/users";
    private final Context context;
    private final ProgressOkHttp<Response, Void> req;
    private OnRegister onRegister;

    public RegisterJson(Context context) {
        this.context = context;
        req = new ProgressOkHttp<>(context, Response.class);
    }

    public interface OnRegister {
        void onSuccess(RegisterJson.Response response);

        void onFailure(String text);
    }

    public static class Request {
        public String name;
        public String email;
        public String password;
        public String password_confirmation;

        public ArrayList<Pair<String, String>> createParams() {
            ArrayList<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("name", name));
            params.add(new Pair<>("email", email));
            params.add(new Pair<>("password", password));
            params.add(new Pair<>("password_confirmation", password_confirmation));
            return params;
        }
    }

    public static class Response extends RDefault {
        public String access_token;
        public UserData data;
    }

    public void post(Request request, OnRegister onRegister) {
        this.onRegister = onRegister;
        req.post(API, request.createParams(), null, onRegisterDone,
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

    public void cancel() {
        req.cancel();
    }
}
