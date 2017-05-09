package com.jixstreet.kolla.payment;

import com.jixstreet.kolla.booking.room.RoomDetailJson;

/**
 * Created by satryaway on 4/12/2017.
 * satryaway@gmail.com
 */

public interface OnGetRoomDetail {
    void onSuccess(RoomDetailJson.Response response);

    void onFailure(String message);
}
