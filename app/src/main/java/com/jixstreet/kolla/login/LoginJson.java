package com.jixstreet.kolla.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.jixstreet.kolla.parent.DefaultRequest;
import com.jixstreet.kolla.parent.ModelJson;
import com.jixstreet.kolla.model.UserData;
import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.RDefault;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.prefs.CPrefs;

import java.util.ArrayList;

/**
 *
 */
public class LoginJson extends ModelJson {
    private OnLogin onLogin;
    private ProgressOkHttp<Response, Void> req;

    @Override
    public String getRoute() {
        return "/auth/login";
    }

    @Override
    public Class getChildClass() {
        return LoginJson.class;
    }

    @Override
    public void cancel() {
        req.cancel();
    }

    public LoginJson(Context context) {
        super(context);
        req = new ProgressOkHttp<>(context, Response.class);
    }

    public static class Request extends DefaultRequest {
        public String email;
        public String password;

        @Override
        public ArrayList<Pair<String, String>> getParams() {
            ArrayList<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("email", email));
            params.add(new Pair<>("password", password));
            return params;
        }
    }

    public static class Response extends RDefault {
        public String access_token;
        public UserData data;

        public static void saveData(Context ctx, String access_token, UserData data) {
            saveAccessToken(ctx, access_token);
            saveUserData(ctx, data);
        }

        public static void saveAccessToken(Context ctx, String accessToken) {
            CPrefs.write(ctx, PrefKey, accessToken, String.class);
        }

        public static void clearAccessToken(Context ctx) {
            CPrefs.write(ctx, PrefKey, null, String.class);
        }

        public static String getAccessToken(Context ctx) {
            return CPrefs.read(ctx, PrefKey, String.class);
        }

        public static void saveUserData(Context ctx, UserData data) {
            CPrefs.write(ctx, UserData.PrefKey, data, UserData.class);
        }
    }

    public void post(Request request, OnLogin onLogin) {
        this.onLogin = onLogin;
        req.post(ROUTE, request.getParams(), null, onLoginDone,
                "Logging in...", true);
    }

    private OnFinishedCallback<Response, Void> onLoginDone
            = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, Response response, Void tag, String errorMsg) {
            if (type == ResultType.Success && response != null) {
                if (RStatus.OK.equals(response.status))
                    onLogin.onSuccess(response);
                else
                    onLogin.onFailure(response.message);
            } else {
                onLogin.onFailure(errorMsg);
            }
        }
    };
}
