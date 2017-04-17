package com.jixstreet.kolla.credit;

/**
 * Created by satryaway on 4/17/2017.
 * satryaway@gmail.com
 */

public interface OnGetBalance {
    void onSuccess(GetBalanceJson.Response response);

    void onFailure(String message);
}
