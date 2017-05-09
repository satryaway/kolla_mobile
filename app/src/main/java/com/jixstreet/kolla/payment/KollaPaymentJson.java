package com.jixstreet.kolla.payment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.parent.DefaultResponse;
import com.jixstreet.kolla.parent.ModelJson;

import java.util.ArrayList;

/**
 * Created by satryaway on 5/5/2017.
 * satryaway@gmail.com
 */

public class KollaPaymentJson extends ModelJson {
    private ProgressOkHttp<Response, Void> httpPayment;
    private OnKollaPay onKollaPay;

    public KollaPaymentJson(Context context, String id) {
        super(context, id + "/kolla");
        httpPayment = new ProgressOkHttp<>(context, Response.class);
    }

    @Override
    public String getRoute() {
        return "/pay/book/";
    }

    @Override
    public void cancel() {
        httpPayment.cancel();
    }

    public static class Response extends DefaultResponse {
        public Data data;

        public class Data {
            public String book_id;
            public String total;
            public String payment_type;
            public String status;
            public String created_at;
            public String updated_at;
            public String id;
        }
    }

    public void payWithKolla(OnKollaPay onKollaPay) {
        this.onKollaPay = onKollaPay;
        httpPayment.post(ROUTE, new ArrayList<Pair<String, String>>(), null, onPayWithKollaFinished,
                "Processing Payment...", true);
    }

    private OnFinishedCallback<Response, Void> onPayWithKollaFinished = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success && response != null) {
                if (RStatus.OK.equals(response.status))
                    onKollaPay.onSuccess(response);
                else onKollaPay.onFailure(response.message);
            } else onKollaPay.onFailure(errorMsg);
        }
    };
}
