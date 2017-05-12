package com.jixstreet.kolla.msaku;

/**
 * Created by satryaway on 5/12/2017.
 * satryaway@gmail.com
 */

public class MSakuSessionData {
    public String session;
    public String card_hash;
    public String card_rsa;
    public String card_bin;

    public MSakuSessionData(String session, String card_hash, String card_rsa, String card_bin) {
        this.session = session;
        this.card_hash = card_hash;
        this.card_rsa = card_rsa;
        this.card_bin = card_bin;
    }
}
