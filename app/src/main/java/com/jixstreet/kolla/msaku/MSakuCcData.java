package com.jixstreet.kolla.msaku;

/**
 * Created by satryaway on 5/12/2017.
 * satryaway@gmail.com
 */

public class MSakuCcData {
    public String imsi;
    public String first_name;
    public String last_name;
    public String email;
    public String phone;
    public String cc_number;
    public String exp_month;
    public String exp_year;
    public String address;
    public String city;
    public String zip;
    public String cvv;

    public MSakuCcData(String imsi, String first_name, String last_name, String email, String phone,
                       String cc_number, String exp_month, String exp_year, String address,
                       String city, String zip, String cvv) {
        this.imsi = imsi;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.cc_number = cc_number;
        this.exp_month = exp_month;
        this.exp_year = exp_year;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.cvv = cvv;
    }
}
