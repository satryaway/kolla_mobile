package com.jixstreet.kolla.booking;

import com.jixstreet.kolla.booking.category.BookingCategory;
import com.jixstreet.kolla.booking.room.Room;
import com.jixstreet.kolla.booking.room.RoomJson;
import com.jixstreet.kolla.payment.KollaPaymentJson;

/**
 * Created by satryaway on 4/13/2017.
 * satryaway@gmail.com
 */

public class Booking {
    public static String paramKey = Booking.class.getName().concat("1");
    public Room room;
    public BookingCategory bookingCategory;
    public RoomJson.Request roomRequest;
    public BookingJson.Request bookingRequest;
    public BookingJson.Response bookingResponse;
}
