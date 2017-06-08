package com.jixstreet.kolla.event;

import com.jixstreet.kolla.booking.room.Room;

import java.util.List;

/**
 * Created by satryaway on 5/17/2017.
 * satryaway@gmail.com
 */

public class Event {
    public static final String paramKey = Event.class.getName().concat("1");

    public String id;
    public String name;
    public String description;
    public String location;
    public String room_id;
    public String map;
    public String date;
    public String start_time;
    public String end_time;
    public String booking_fee;
    public String notes;
    public String available_capacity;
    public String created_at;
    public String updated_at;
    public List<Image> images;

    public class Image {
        public String id;
        public String event_id;
        public String file;
        public String caption;
        public String created_at;
        public String updated;
    }
}
