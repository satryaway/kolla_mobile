package com.jixstreet.kolla.event;

/**
 * Created by satryaway on 6/9/2017.
 * satryaway@gmail.com
 */

public interface OnEventBooking {
    void onSuccess(EventBookingJson.Response response);

    void onFailure(String message);
}
