package com.jixstreet.kolla.credit;

/**
 * Created by satryaway on 4/17/2017.
 * satryaway@gmail.com
 */

public interface OnCheckBalance {
    void onSuccess(CheckBalanceJson.Response response);

    void onFailure(String message);
}
