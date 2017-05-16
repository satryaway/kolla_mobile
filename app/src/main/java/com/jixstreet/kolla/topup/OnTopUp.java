package com.jixstreet.kolla.topup;

/**
 * Created by satryaway on 5/16/2017.
 * satryaway@gmail.com
 */

public interface OnTopUp {
    void onSuccess(TopUpCreditJson.Response response);

    void onFailure(String message);
}
