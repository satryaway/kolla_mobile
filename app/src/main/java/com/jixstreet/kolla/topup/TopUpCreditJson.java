package com.jixstreet.kolla.topup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jixstreet.kolla.network.OnFinishedCallback;
import com.jixstreet.kolla.network.ProgressOkHttp;
import com.jixstreet.kolla.network.RStatus;
import com.jixstreet.kolla.network.ResultType;
import com.jixstreet.kolla.parent.DefaultResponse;
import com.jixstreet.kolla.parent.ModelJson;

import java.util.List;

/**
 * Created by satryaway on 5/8/2017.
 * satryaway@gmail.com
 */

public class TopUpCreditJson extends ModelJson {
    private ProgressOkHttp<Response, Void> httpGetTopUpList;
    private OnGetTopUpCreditList onGetTopUpCreditList;

    public TopUpCreditJson(Context context) {
        super(context);
        httpGetTopUpList = new ProgressOkHttp<>(context, Response.class);
    }

    @Override
    public String getRoute() {
        return "/credit/topup/available";
    }

    public static class Response extends DefaultResponse {
        public List<CreditAmount> data;
    }

    public void getList(OnGetTopUpCreditList onGetTopUpCreditList) {
        this.onGetTopUpCreditList = onGetTopUpCreditList;
        httpGetTopUpList.get(ROUTE, null, null,
                onGetListFinishsed, "Fetching Top Up Credit List...", true);
    }

    private OnFinishedCallback<Response, Void> onGetListFinishsed = new OnFinishedCallback<Response, Void>() {
        @Override
        public void handle(@NonNull ResultType type, @Nullable Response response,
                           @Nullable Void tag, @Nullable String errorMsg) {
            if (ResultType.Success == type && response != null) {
                if (RStatus.OK.equals(response.status))
                    onGetTopUpCreditList.onSuccess(response);
                else onGetTopUpCreditList.onFailure(response.message);
            } else onGetTopUpCreditList.onFailure(errorMsg);
        }
    };

    @Override
    public void cancel() {
        httpGetTopUpList.cancel();
    }
}
