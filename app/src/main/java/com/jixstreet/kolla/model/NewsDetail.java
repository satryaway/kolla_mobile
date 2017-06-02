package com.jixstreet.kolla.model;

/**
 * Created by satryaway on 2/16/2017.
 * satryaway@gmail.com
 */

public class NewsDetail {
    public static String paramKey = NewsDetail.class.getClass().getName().concat("1");

    public String id;
    public String title;
    public String content;
    public String cover_image;
    public String created_at;
    public String updated_at;
}
