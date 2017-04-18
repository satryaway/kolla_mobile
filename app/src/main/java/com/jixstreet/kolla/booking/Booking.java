package com.jixstreet.kolla.booking;

import com.jixstreet.kolla.booking.room.Room;
import com.jixstreet.kolla.booking.room.RoomJson;

/**
 * Created by satryaway on 4/13/2017.
 * satryaway@gmail.com
 */

public class Booking {
    public static String paramKey = Booking.class.getName().concat("1");
    public Room room;
    public RoomJson.Request roomRequest;
}
