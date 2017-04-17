package com.jixstreet.kolla.credit;

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
 * Created by satryaway on 4/17/2017.
 * satryaway@gmail.com
 */

public class GetBalanceJson extends ModelJson {
    private ProgressOkHttp<Response, Void> http;
    private OnGetBalance onGetBalance;

    public GetBalanceJson(Context context) {
        super(context);
        http = new ProgressOkHttp<>(context, Response.class);
    }

    @Override
    public String getRoute() {
        return "/credit/balance";
    }

    @Override
    public void cancel() {
        http.cancel();
    }

    public static class Response extends DefaultResponse {
        public Data data;
    }

    public class Data {
        public String user_id;
        public String main_credit;
        public String bonus_credit;
        public String notes;
        public String created_at;
        public String updated_at;
        public String status;
    }

    public void get(OnGetBalance onGetBalance) {
        this.onGetBalance = onGetBalance;
        http.get(ROUTE, null, null, onGetBalanceDone, "Getting balance credit...", true);
    }


    private OnFinishedCallback<Response, Void> onGetBalanceDone = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success && response != null) {
                if (RStatus.OK.equals(response.status))
                    onGetBalance.onSuccess(response);
                else onGetBalance.onFailure(response.message);
            } else onGetBalance.onFailure(errorMsg);
        }
    };
}
