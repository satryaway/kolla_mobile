package com.jixstreet.kolla.booking;

/**
 * Created by satryaway on 5/4/2017.
 * satryaway@gmail.com
 */

public interface OnBooking {
    void onSuccess(BookingJson.Response response);

    void onFailure(String message);
}
