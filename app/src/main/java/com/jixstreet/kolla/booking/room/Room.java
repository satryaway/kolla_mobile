package com.jixstreet.kolla.booking.room;

import com.jixstreet.kolla.booking.category.BookingCategory;

/**
 * Created by satryaway on 3/7/2017.
 * satryaway@gmail.com
 */

public class Room {
    public String id;
    public String category_id;
    public String title;
    public String address;
    public String available_seats;
    public String images;
    public String maps_coordinate;
    public String facilities;
    public String price;
    public String created_at;
    public String updated_at;
    public BookingCategory bookingCategory;
    public String imageUrl;
    public String description;
    public boolean isFullBooked;

    public Room(String imageUrl, String title, String description, String available_seats, boolean isFullBooked) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.available_seats = available_seats;
        this.isFullBooked = isFullBooked;
    }
}
