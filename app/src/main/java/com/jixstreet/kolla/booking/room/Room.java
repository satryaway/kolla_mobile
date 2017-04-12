package com.jixstreet.kolla.booking.room;

import com.jixstreet.kolla.booking.category.BookingCategory;

import java.util.List;

/**
 * Created by satryaway on 3/7/2017.
 * satryaway@gmail.com
 */

public class Room {
    public String id;
    public String category_id;
    public String name;
    public String description;
    public String location;
    public String map;
    public String size;
    public String measurement;
    public String price;
    public String price_type;
    public String created_at;
    public String updated_at;

    //Extras for detail
    public BookingCategory category;

    public List<Facility> facilities;
    public List<String> images;
    public String imageUrl;
    public boolean isFullBooked;

    public Room(String imageUrl, String name, String description, String measurement, boolean isFullBooked) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.measurement = measurement;
        this.isFullBooked = isFullBooked;
    }

    public class Facility {
        public String id;
        public String name;
        public String icon;
        public String created_at;
        public String updated_at;
    }


}
