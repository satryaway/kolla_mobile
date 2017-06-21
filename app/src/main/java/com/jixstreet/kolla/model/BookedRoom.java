package com.jixstreet.kolla.model;

import com.jixstreet.kolla.booking.room.Room;

/**
 * Created by satryaway on 6/21/2017.
 * satryaway@gmail.com
 */

public class BookedRoom {
    public String user_id;
    public String room_id;
    public String date;
    public String start_time;
    public String end_time;
    public String duration;
    public String total_guests;
    public String price;
    public String price_type;
    public String status;
    public String full_name;
    public String event_name;
    public String booking_type;
    public String created_at;
    public String updated_at;
    public String id;

    //Used to get list of booked room
    public Room room;
}
