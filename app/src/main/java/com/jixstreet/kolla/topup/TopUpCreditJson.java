package com.jixstreet.kolla.topup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.jixstreet.kolla.credit.GetBalanceJson;
import com.jixstreet.kolla.model.Balance;
import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.parent.DefaultRequest;
import com.jixstreet.kolla.parent.DefaultResponse;
import com.jixstreet.kolla.parent.ModelJson;

import java.util.ArrayList;

/**
 * Created by satryaway on 5/16/2017.
 * satryaway@gmail.com
 */

public class TopUpCreditJson extends ModelJson {
    private ProgressOkHttp<Response, Void> topUpHttp;
    private OnTopUp onTopUp;

    public TopUpCreditJson(Context context, String id) {
        super(context, id + "/topup");
        topUpHttp = new ProgressOkHttp<>(context, Response.class);
    }

    @Override
    public String getRoute() {
        return "/credit/";
    }

    @Override
    public void cancel() {
        topUpHttp.cancel();
    }

    public static class Request extends DefaultRequest {
        public String credit_id;
        public String trxid;
        public String mtrxid;
        public String price_final;
        public String payment_type;

        @Override
        public ArrayList<Pair<String, String>> getParams() {
            ArrayList<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("credit_id", credit_id));
            params.add(new Pair<>("trxid", trxid));
            params.add(new Pair<>("mtrxid", mtrxid));
            params.add(new Pair<>("price_final", price_final));
            params.add(new Pair<>("payment_type", payment_type));

            return params;
        }
    }

    public static class Response extends DefaultResponse {
        public Balance data;
    }

    public void post(Request request, OnTopUp onTopUp) {
        this.onTopUp = onTopUp;
        topUpHttp.post(ROUTE, request.getParams(), null,
                onTopUpFinished, "Requesting top up...", true);
    }

    private OnFinishedCallback<Response, Void> onTopUpFinished = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success && response != null) {
                if (RStatus.OK.equals(response.status)) {
                    onTopUp.onSuccess(response);
                } else onTopUp.onFailure(response.message);
            } else onTopUp.onFailure(errorMsg);
        }
    };
}
