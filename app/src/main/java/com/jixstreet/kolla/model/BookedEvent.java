package com.jixstreet.kolla.model;

import com.jixstreet.kolla.event.Event;

/**
 * Created by satryaway on 6/21/2017.
 * satryaway@gmail.com
 */

public class BookedEvent {
    public String id;
    public String event_id;
    public String user_id;
    public String total_guest;
    public String total_price;
    public String status;
    public String created_at;
    public String updated_at;
    public Event event;
}
