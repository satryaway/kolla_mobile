package com.jixstreet.kolla.news;

/**
 * Created by satryaway on 3/2/2017.
 * satryaway@gmail.com
 */

public interface OnGetNews  {
    void onSucceed(NewsJson.Response response);

    void onFailure(String message);
}
