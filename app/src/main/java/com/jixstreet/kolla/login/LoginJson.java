package com.jixstreet.kolla.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Pair;

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
public class LoginJson {
    public static final transient String API = "/auth/login";
    public static final transient String PrefKey = LoginJson.class.getName().concat("1");
    private final Context context;
    private OnLogin onLogin;
    private ProgressOkHttp<Response, Void> req;

    public LoginJson(Context context) {
        this.context = context;
        req = new ProgressOkHttp<>(context, Response.class);
    }

    public interface OnLogin {
        void onSuccess(Response response);

        void onFailure(String text);
    }

    public static class Request {
        public String email;
        public String password;

        public ArrayList<Pair<String, String>> createParams() {
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
        req.post(API, request.createParams(), null, onLoginDone,
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

    public void cancel() {
        req.cancel();
    }

}
