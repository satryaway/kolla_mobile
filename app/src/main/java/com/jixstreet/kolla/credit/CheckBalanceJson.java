package com.jixstreet.kolla.credit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.parent.DefaultRequest;
import com.jixstreet.kolla.parent.DefaultResponse;
import com.jixstreet.kolla.parent.ModelJson;

import java.util.ArrayList;

/**
 * Created by satryaway on 4/17/2017.
 * satryaway@gmail.com
 */

public class CheckBalanceJson extends ModelJson {
    private final ProgressOkHttp<Response, Void> http;
    private OnCheckBalance onCheckBalance;

    public CheckBalanceJson(Context context) {
        super(context);
        http = new ProgressOkHttp<>(context, Response.class);
    }

    @Override
    public String getRoute() {
        return "credit/check";
    }

    @Override
    public void cancel() {

    }

    public static class Request extends DefaultRequest {
        public String room_id;
        public String amount;

        @Override
        public ArrayList<Pair<String, String>> getParams() {
            ArrayList<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("room_id", room_id));
            params.add(new Pair<>("amount", amount));

            return params;
        }
    }

    public static class Response extends DefaultResponse {
        public Data data;

        public class Data {
            public String user_id;
            public String main_credit;
            public String bonus_credit;
            public String notes;
            public String created_at;
            public String updated_at;
            public String status;
        }
    }

    public void get(Request request, OnCheckBalance onCheckBalance) {
        this.onCheckBalance = onCheckBalance;
        http.get(ROUTE, request.getParams(), null,
                onCheckBalanceFinished, "Checking balance...", true);
    }

    private OnFinishedCallback<Response, Void> onCheckBalanceFinished =
            new OnFinishedCallback<Response, Void>() {
                @Override
                public void handle(@NonNull ResultType type, @Nullable Response response,
                                   @Nullable Void tag, @Nullable String errorMsg) {
                    if (type == ResultType.Success && response != null) {
                        if (RStatus.OK.equals(response.status)) {
                            onCheckBalance.onSuccess(response);
                        } else onCheckBalance.onFailure(response.message);
                    } else onCheckBalance.onFailure(errorMsg);
                }
            };
}
