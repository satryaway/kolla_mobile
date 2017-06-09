package com.jixstreet.kolla.event;

import com.jixstreet.kolla.model.UserData;

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
    public String start_datetime;
    public String end_datetime;
    public String booking_fee;
    public String payment_type;
    public String notes;
    public String available_capacity;
    public String created_at;
    public String updated_at;
    public List<Image> images;

    //Event Detail
    public List<Tag> tags;
    public List<WhoComes> who_comes;

    public class Image {
        public String id;
        public String event_id;
        public String file;
        public String caption;
        public String created_at;
        public String updated;
    }

    public class Tag {
        public String id;
        public String name;
        public String created_at;
        public String updated_at;
    }

    public class WhoComes {
        public String id;
        public String event_id;
        public String user_id;
        public UserData user;
        public List<Guest> guests;
    }
}
