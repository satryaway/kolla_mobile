package com.jixstreet.kolla.profile.detail;

/**
 * Created by satryaway on 6/20/2017.
 * satryaway@gmail.com
 */

public interface OnGetProfile {
    void onSuccess(GetProfileJson.Response response);

    void onFailure(String message);
}
