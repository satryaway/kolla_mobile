package com.jixstreet.kolla.topup;

/**
 * Created by satryaway on 5/8/2017.
 * satryaway@gmail.com
 */

public class CreditAmount {
    public static String paramKey = CreditAmount.class.getName().concat("1");

    public String id;
    public String kolla_credit;
    public String nominal;
    public String given_bonus;
    public String notes;
    public String is_active;
    public String created_at;
    public String updated_at;

    public String payment_type;
}
