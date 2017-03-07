package com.jixstreet.kolla.booking.room;

/**
 * Created by satryaway on 3/7/2017.
 * satryaway@gmail.com
 */

public class Room {
    public String imageUrl;
    public String title;
    public String description;
    public String seatCount;
    public boolean isFullBooked;

    public Room(String imageUrl, String title, String description, String seatCount, boolean isFullBooked) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.seatCount = seatCount;
        this.isFullBooked = isFullBooked;
    }
}
