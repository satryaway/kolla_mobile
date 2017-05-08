package com.jixstreet.kolla.booking.room.payment;

/**
 * Created by satryaway on 5/5/2017.
 * satryaway@gmail.com
 */

public interface OnKollaPay {
    void onSuccess(KollaPaymentJson.Response response);

    void onFailure(String message);
}
