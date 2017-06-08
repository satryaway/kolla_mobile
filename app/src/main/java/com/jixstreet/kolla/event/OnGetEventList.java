package com.jixstreet.kolla.event;

/**
 * Created by satryaway on 6/8/2017.
 * satryaway@gmail.com
 */

public interface OnGetEventList {
    void onSuccess(EventListJson.Response response);

    void onFailure(String message);
}
