package com.jixstreet.kolla.profile.following;

/**
 * Created by satryaway on 6/21/2017.
 * satryaway@gmail.com
 */

public interface OnGetFollowedUser {
    void onSuccess(FollowedUserJson.Response response);

    void onFailure(String message);
}
