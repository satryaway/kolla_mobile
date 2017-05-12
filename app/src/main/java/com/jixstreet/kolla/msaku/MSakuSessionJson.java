package com.jixstreet.kolla.msaku;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.parent.DefaultRequest;
import com.jixstreet.kolla.parent.DefaultResponse;
import com.jixstreet.kolla.parent.ModelJson;

import java.util.ArrayList;

/**
 * Created by satryaway on 5/12/2017.
 * satryaway@gmail.com
 */

public class MSakuSessionJson extends ModelJson {

    private OnGetMSakuSession onGetMSakuSession;
    private ProgressOkHttp<Response, Void> mSakuHttp;

    public MSakuSessionJson(Context context) {
        super(context);
        mSakuHttp = new ProgressOkHttp<>(context, Response.class);
    }

    @Override
    public String getRoute() {
        return "/msaku/session";
    }

    @Override
    public void cancel() {
        mSakuHttp.cancel();
    }

    public static class Request extends DefaultRequest {
        public MSakuCcData ccData;
        public MSakuSessionData sessionData;

        @Override
        public ArrayList<Pair<String, String>> getParams() {
            ArrayList<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("imsi", ccData.imsi));
            params.add(new Pair<>("first_name", ccData.first_name));
            params.add(new Pair<>("last_name", ccData.last_name));
            params.add(new Pair<>("email", ccData.email));
            params.add(new Pair<>("phone", ccData.phone));
            params.add(new Pair<>("cc_number", ccData.cc_number));
            params.add(new Pair<>("exp_month", ccData.exp_month));
            params.add(new Pair<>("exp_year", ccData.exp_year));
            params.add(new Pair<>("address", ccData.address));
            params.add(new Pair<>("city", ccData.city));
            params.add(new Pair<>("zip", ccData.zip));
            params.add(new Pair<>("cvv", ccData.cvv));

            params.add(new Pair<>("session", sessionData.session));
            params.add(new Pair<>("card_hash", sessionData.card_hash));
            params.add(new Pair<>("card_rsa", sessionData.card_rsa));
            params.add(new Pair<>("card_bin", sessionData.card_bin));

            return params;
        }
    }

    public static class Response extends DefaultResponse {
        public Data data;

        public class Data {
            public MSakuCcData reg_data;
            public MSakuSessionData cc_data;
            public MSakuOperatorData operator_data;
        }
    }

    public void saveData(Request request, OnGetMSakuSession onGetMSakuSession) {
        this.onGetMSakuSession = onGetMSakuSession;
        mSakuHttp.post(ROUTE, request.getParams(), null, onSaveSessionFinished,
                "Saving data....", true);
    }

    public void getData(OnGetMSakuSession onGetMSakuSession) {
        this.onGetMSakuSession = onGetMSakuSession;
        mSakuHttp.get(ROUTE, null, null, onSaveSessionFinished, "Getting data...", true);
    }

    private OnFinishedCallback<Response, Void> onSaveSessionFinished = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (type == ResultType.Success && response != null)
                onGetMSakuSession.onSuccess(response);
            else onGetMSakuSession.onFailure(errorMsg);
        }
    };
}
