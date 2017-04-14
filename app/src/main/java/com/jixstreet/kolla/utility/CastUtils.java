package com.jixstreet.kolla.utility;

import com.google.gson.Gson;

/**
 * Created by satryaway on 4/14/2017.
 * satryaway@gmail.com
 */

public class CastUtils {

    public static String toString(Object object){
        return new Gson().toJson(object);
    }

    public static synchronized <T> T fromString(String jsonString, Class<T> tClass) {
        return new Gson().fromJson(jsonString, tClass);
    }
}
