package com.jixstreet.kolla.event;

/**
 * Created by satryaway on 6/8/2017.
 * satryaway@gmail.com
 */

public interface OnGetEventDetail {
    void onSuccess(EventDetailJson.Response response);

    void onFailure(String message);
}
