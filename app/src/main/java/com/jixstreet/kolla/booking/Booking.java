package com.jixstreet.kolla.booking;

import com.jixstreet.kolla.booking.room.Room;
import com.jixstreet.kolla.booking.room.RoomJson;
import com.jixstreet.kolla.booking.room.payment.KollaPaymentJson;

/**
 * Created by satryaway on 4/13/2017.
 * satryaway@gmail.com
 */

public class Booking {
    public static String paramKey = Booking.class.getName().concat("1");
    public Room room;
    public RoomJson.Request roomRequest;
    public BookingJson.Request bookingRequest;
    public BookingJson.Response bookingResponse;
    public KollaPaymentJson.Response kollaPaymentResponse;

    public void setBookingRequest(Booking booking) {
        BookingJson.Request bookingRequest = booking.bookingRequest;
        RoomJson.Request roomRequest = booking.roomRequest;
        bookingRequest.date = roomRequest.date;
        bookingRequest.time= roomRequest.time;
        bookingRequest.duration = roomRequest.duration;
        bookingRequest.guest = roomRequest.guest;
    }
}
