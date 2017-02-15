package com.jixstreet.kolla;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.jixstreet.kolla.model.LoginJson;
import com.jixstreet.kolla.network.OnFinishedCallback2;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.utility.Log;

/**
 * Created by satryaway on 2/15/2017.
 * satryaway@gmail.com
 */

public class MainActivity extends AppCompatActivity {

    private ProgressOkHttp<LoginJson.Response, Void> http;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        http = new ProgressOkHttp<>(this, LoginJson.Response.class);

        LoginJson.Request req = new LoginJson.Request();
        http.post(LoginJson.API, req.createParams(), null, onLoginDone, "Login", true);
    }

    private OnFinishedCallback2<LoginJson.Response, Void> onLoginDone
            = new OnFinishedCallback2<LoginJson.Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, LoginJson.Response response, Void tag, String errorMsg) {
            if (type == ResultType.Success && response != null) {

                boolean isLoginOk = RStatus.OK.equals(response.status);
                Log.d(getString(R.string.app_name), String.valueOf(isLoginOk));
            }
        }
    };

}
