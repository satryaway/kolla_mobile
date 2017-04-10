package com.jixstreet.kolla.booking.category;

import com.jixstreet.kolla.parent.ModelJson;

/**
 * Created by satryaway on 3/6/2017.
 * satryaway@gmail.com
 */

public class BookingCategory {
    public static final String paramKey = BookingCategory.class.getName().concat("1");

    public String id;
    public String name;
    public String description;
    public String image;
    public String payment_type;
    public String created_at;
    public String updated_at;
}
