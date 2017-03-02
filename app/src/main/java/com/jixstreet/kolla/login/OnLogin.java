package com.jixstreet.kolla.login;

/**
 * Created by satryaway on 3/2/2017.
 * satryaway@gmail.com
 */

public interface OnLogin {
    void onSuccess(LoginJson.Response response);

    void onFailure(String text);
}
