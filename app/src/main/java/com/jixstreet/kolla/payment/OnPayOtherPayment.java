package com.jixstreet.kolla.payment;

import com.jixstreet.kolla.booking.Booking;

/**
 * Created by satryaway on 4/14/2017.
 * satryaway@gmail.com
 */

public interface OnPayOtherPayment {
    void onPay(Booking booking);
}
