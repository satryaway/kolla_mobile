package com.jixstreet.kolla.model;

import android.content.Context;

import com.jixstreet.kolla.credit.CheckBalanceJson;
import com.jixstreet.kolla.login.LoginJson;
import com.jixstreet.kolla.parent.ModelJson;

/**
 * Created by satryaway on 3/1/2017.
 * satryaway@gmail.com
 */

public class UserData {
    public static String PrefKey = UserData.class.getName().concat("1");

    public String id;
    public String name;
    public String email;
    public String phone_no;
    public String company;
    public String job_title;
    public String facebook_id;
    public String linkedin_id;
    public String profile_picture;
    public String has_web_access;
    public String user_type;
    public String created_at;
    public String updated_at;
    public String deleted_at;
    public String full_name;
    public Balance credit;
}
