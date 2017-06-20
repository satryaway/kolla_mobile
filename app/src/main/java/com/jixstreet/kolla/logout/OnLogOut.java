package com.jixstreet.kolla.logout;

import com.jixstreet.kolla.parent.DefaultResponse;

/**
 * Created by satryaway on 6/20/2017.
 * satryaway@gmail.com
 */

public interface OnLogOut {
    void onSuccess(DefaultResponse response);

    void onFailure(String message);
}
