package com.jixstreet.kolla.model;

/**
 * Created by satryaway on 2/16/2017.
 * satryaway@gmail.com
 */

public class NewsDetail {
    public String id;
    public String title;
    public String content;
    public String cover_image;
    public String created_at;
    public String updated_at;

    public NewsDetail() {
    }

    public NewsDetail(String title, String created_at, String cover_image) {
        this.title = title;
        this.created_at = created_at;
        this.cover_image = cover_image;
    }
}
