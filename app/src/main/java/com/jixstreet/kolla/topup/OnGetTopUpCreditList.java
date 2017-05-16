package com.jixstreet.kolla.topup;

/**
 * Created by satryaway on 5/8/2017.
 * satryaway@gmail.com
 */

public interface OnGetTopUpCreditList {
    void onSuccess(TopUpCreditListJson.Response response);

    void onFailure(String message);
}
