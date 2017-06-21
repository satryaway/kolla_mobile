package com.jixstreet.kolla.profile.booking;

/**
 * Created by satryaway on 6/21/2017.
 * satryaway@gmail.com
 */

public interface OnGetBookedRooms {
    void onSuccess(BookedRoomJson.Response response);

    void onFailure(String message);
}
