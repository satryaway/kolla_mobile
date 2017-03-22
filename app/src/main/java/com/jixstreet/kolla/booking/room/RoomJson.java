package com.jixstreet.kolla.booking.room;

import android.content.Context;
import android.util.Pair;

import com.jixstreet.kolla.parent.DefaultRequest;
import com.jixstreet.kolla.parent.ModelJson;

import java.util.ArrayList;

/**
 * Created by satryaway on 3/22/2017.
 * satryaway@gmail.com
 */

public class RoomJson extends ModelJson {
    public static final String paramKey = RoomJson.class.getName().concat("1");

    public RoomJson(Context context) {
        super(context);
    }

    @Override
    public String getRoute() {
        return "";
    }

    @Override
    public void cancel() {

    }

    public static class Request extends DefaultRequest {
        public String location;
        public String bookingDate;
        public String bookingTime;
        public String duration;
        public String guest;
        public boolean isInitial = true;

        @Override
        public ArrayList<Pair<String, String>> getParams() {
            ArrayList<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("location", location));
            return params;
        }
    }
}
