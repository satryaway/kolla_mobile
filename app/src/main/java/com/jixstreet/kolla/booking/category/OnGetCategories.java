package com.jixstreet.kolla.booking.category;

/**
 * Created by satryaway on 3/8/2017.
 * satryaway@gmail.com
 */

public interface OnGetCategories {
    void onSuccess(BookingCategoryJson.Response response);
    void onFailure(String message);
}
