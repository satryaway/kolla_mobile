package com.jixstreet.kolla.credit;

import android.content.Context;
import android.util.Pair;

import com.jixstreet.kolla.parent.DefaultRequest;
import com.jixstreet.kolla.parent.DefaultResponse;
import com.jixstreet.kolla.parent.ModelJson;

import java.util.ArrayList;

/**
 * Created by satryaway on 4/17/2017.
 * satryaway@gmail.com
 */

public class CheckBalanceJson extends ModelJson {
    public CheckBalanceJson(Context context) {
        super(context);
    }

    @Override
    public String getRoute() {
        return "credit/check";
    }

    @Override
    public void cancel() {

    }

    public static class Request extends DefaultRequest {
        public String room_id;
        public String amount;

        @Override
        public ArrayList<Pair<String, String>> getParams() {
            ArrayList<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("room_id", room_id));
            params.add(new Pair<>("amount", amount));

            return params;
        }
    }

    public static class Response extends DefaultResponse {
        public Data data;

        public class Data {
            public String user_id;
            public String main_credit;
            public String bonus_credit;
            public String notes;
            public String created_at;
            public String updated_at;
        }
    }
}
