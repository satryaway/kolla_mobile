package com.jixstreet.kolla.model;

import com.jixstreet.kolla.login.LoginJson;

/**
 * Created by satryaway on 3/1/2017.
 * satryaway@gmail.com
 */

public class UserData {
    public static final transient String PrefKey = UserData.class.getName().concat("1");

    public String id;
    public String name;
    public String email;
    public String facebook_id;
    public String linkedin_id;
    public String created_at;
    public String updated_at;
}
