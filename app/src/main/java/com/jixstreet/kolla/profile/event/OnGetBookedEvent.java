package com.jixstreet.kolla.profile.event;

/**
 * Created by satryaway on 6/21/2017.
 * satryaway@gmail.com
 */

public interface OnGetBookedEvent {
    void onSuccess(BookedEventJson.Response response);

    void onFailure(String message);
}
