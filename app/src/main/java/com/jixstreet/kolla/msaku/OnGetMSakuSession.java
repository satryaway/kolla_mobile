package com.jixstreet.kolla.msaku;

/**
 * Created by satryaway on 5/12/2017.
 * satryaway@gmail.com
 */

public interface OnGetMSakuSession {
    void onSuccess(MSakuSessionJson.Response response);

    void onFailure(String message);
}
