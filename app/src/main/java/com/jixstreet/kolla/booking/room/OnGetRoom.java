package com.jixstreet.kolla.booking.room;

/**
 * Created by satryaway on 4/10/2017.
 * satryaway@gmail.com
 */

public interface OnGetRoom {
    void onSuccess(RoomJson.Response response);

    void onFailure(String message);
}
