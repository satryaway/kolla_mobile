package com.jixstreet.kolla.topup;

import com.jixstreet.kolla.msaku.MSakuSuccessfulTransaction;

/**
 * Created by satryaway on 5/12/2017.
 * satryaway@gmail.com
 */

public class TopUp {
    public static final String paramKey = TopUp.class.getName().concat("1");

    public CreditAmount creditAmount;
    public MSakuSuccessfulTransaction transaction;
    public TopUpSuccessInformation topUpSuccessInformation;
    public String message;
}
